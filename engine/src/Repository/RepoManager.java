package Repository;

import DataObjects.Commit;
import dto.TreeNode;
import dto.files.MetaData;
import dto.files.RepoFile;
import errors.exceptions.*;
import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class RepoManager {
    private String repoPath;
    private WorkingCopy workingCopy;
    private FileManager fileManager;

    public RepoManager() {
        repoPath = null;
        workingCopy = new WorkingCopy();
        fileManager = new FileManager();
    }

    public String getRepoPath() {
        return repoPath;
    }

    public void changeRepository(String newRepoPath) throws InvalidPathException, NoSuchRepoException {
        if (!FileManager.isValidPath(newRepoPath)) {
            throw new InvalidPathException(newRepoPath);
        }

        if (!FileManager.isExistingRepo(newRepoPath)) {
            String msg = "The directory " + newRepoPath + " is not a MAGit repository.";
            throw new NoSuchRepoException(msg);
        }

        repoPath = newRepoPath;
        workingCopy.setRepoPath(newRepoPath);
        fileManager.setRepoPath(newRepoPath);
    }

    /**
     * This function checks if a repository is currently set. If not, it throws an exception.
     */
    private void checkAndThrowUnsetRepo() throws RepoNotSetException {
        if (repoPath == null) throw new RepoNotSetException("No MAGit repository is set.");
    }

    public void createRepo(String repoPath) throws InvalidPathException, ExistingRepoException, IOException {
        if (!FileManager.isValidPath(repoPath)) throw new InvalidPathException(null);
        if (FileManager.isExistingRepo(repoPath)) throw new ExistingRepoException(null);

        createDirectories(repoPath);
        createHeadBranch(repoPath, FileManager.PRIMARY_BRANCH);
        String headPath = fileManager.getHeadFilePath();
        FileManager.writeToFile(headPath, FileManager.PRIMARY_BRANCH, false);
    }

    public void checkout(String branchName) throws RepoNotSetException, UnknownError, NoSuchBranchException {
        checkAndThrowUnsetRepo();

        try {
            String branchCommitSha1 = fileManager.getBranchSha1(branchName);
            Commit branchCommit = fileManager.getCommit(branchCommitSha1); // TODO not done
            FileManager.deleteWC(repoPath);
            fileManager.unfoldCommit(branchCommit);
            String headPath = fileManager.getHeadFilePath();
            FileManager.writeToFile(headPath, branchName, false);
        } catch (NoSuchFileException e) {
            throw new NoSuchBranchException(null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownError();
        }
    }

    private void createDirectories(String path) throws IOException {
        String fullPath = FileManager.appendToPath(path, FileManager.MAGIT_DIR);

        Path p1 = Files.createDirectories(Paths.get(fullPath));

        String objPath = FileManager.appendToPath(fullPath, "objects");
        Path p2 = Files.createDirectories(Paths.get(objPath));

        String branchesPath = FileManager.appendToPath(fullPath, "branches");
        Path p3 = Files.createDirectories(Paths.get(branchesPath));

        String headPath = FileManager.appendToPath(branchesPath, "HEAD");
        Path p4 = Files.createFile(Paths.get(headPath));
    }

    /**
     * When creating the main branch for the first time
     * the process is slightly different from just creating a new branch.
     * The main branch at first doesn't have a previous commit to point to.
     */
    public void createHeadBranch(String repoPath, String primaryBranchName) {
        // FIXME: duplicate code with createBranch

        if (!FileManager.isValidPath(repoPath)) return;

        String branchesPath = fileManager.getBranchesDirPath();
        String branchPath = branchesPath + primaryBranchName;

        if (new File(branchPath).isFile()) return;

        try {
            Files.createFile(Paths.get(branchPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createBranch(String branchName) throws RepoNotSetException, IllegalNameException, ExistingBranchException, UnknownError {
        checkAndThrowUnsetRepo();
        if (branchName.contains(" ")) throw new IllegalNameException("Name cannot contain spaces.");

        String branchPath = fileManager.getBranchesDirPath() + branchName;
        if (new File(branchPath).isFile()) throw new ExistingBranchException(null);

        try {
            Files.createFile(Paths.get(branchPath));
            String headBranchName = fileManager.getHeadBranchName();
            String headBranchCommitSha1 = fileManager.getBranchSha1(headBranchName);
            FileManager.writeToFile(branchPath, headBranchCommitSha1, false);
        } catch (IOException e) {
            e.printStackTrace();
            throw new UnknownError();
        }
    }

    public void commit(String creator, String msg) throws RepoNotSetException, UnknownError {
        checkAndThrowUnsetRepo();

        try {
            TreeNode repoFilesRoot = workingCopy.getRepoFileTree();
            Commit commit = createCommitData(repoFilesRoot, creator, msg);

            // Collection<RepoFile> changedFiles = getChangedFiles(repoFilesRoot);
            Collection<RepoFile> changedFiles = getWorkingCopy();
            zip(commit, changedFiles, repoPath);
            fileManager.advanceHeadBranch(commit.getSha1());
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownError();
        }
    }

    private void zip(Commit commit, Collection<RepoFile> changedFiles, String mainDir) {
        String objectsDirPath = fileManager.getObjectsDirPath();
        String sha1 = commit.getSha1();
        String commitContent = commit.toString();
        String commitZipPath = objectsDirPath + File.separator + sha1 + ".zip";
        FileManager.zip(commitZipPath, sha1, commitContent);

        for (RepoFile fd : changedFiles) {
            String fileSha1 = fd.getId();
            String zipName = fileSha1 + ".zip";
            String fileName = fd.getName();
            String content = fd.getContent();
            String zipPath = objectsDirPath + File.separator + zipName;
            FileManager.zip(zipPath, fileName, content);
        }
    }

    private Commit createCommitData(TreeNode wcRoot, String creator, String msg) {
        Commit commit = new Commit();

        commit.setMainDirSha1(wcRoot.getRepoFile().getId());

        // TODO: commit.setMainAncestorSha1();
        commit.setMainAncestorSha1("Temp primary ancestor");

        // TODO: commit.setSecondaryAncestorSha1();
        commit.setSecondaryAncestorSha1("Temp secondary ancestor");

        commit.setMsg(msg);

        String date = Utils.getCurrDate();
        commit.setDate(date);

        commit.setCreator(creator);

        return commit;
    }

    public String getActiveBranch() {
        try {
            return fileManager.getHeadBranchName();
        } catch (NoSuchFileException e) {
            return null;
        }
    }


    /*****************************************************
     ******************* TREE METHODS ********************
     *****************************************************/

    private TreeNode getPrevCommitFileTree() {
        TreeNode root;

        String headBranchCommitSha1 = fileManager.getHeadBranchCommitSha1();
        if (headBranchCommitSha1 == null || headBranchCommitSha1.isEmpty()) {
            root = workingCopy.getRepoFileTree();
        } else {
            root = getCommitFileTree(headBranchCommitSha1);
        }

        return root;
    }

    /**
     * This function receives the SHA1 of a certain commit and returns the file tree
     * pointed at by this commit.
     *
     * @param commitSha1
     * @return
     */
    private TreeNode getCommitFileTree(String commitSha1) {
        Commit commit = fileManager.getCommit(commitSha1);
        MetaData metaData = new MetaData();
        metaData.setId(commit.getMainDirSha1());
        metaData.setFileType(RepoFile.FileType.FOLDER);

        return fileManager.getFileTree(metaData);
    }

    /**
     * Returns the tree of all the files in the repository.
     *
     * @return null if no repository is set.
     */
    public TreeNode getRepoFileTree() {
        return workingCopy.getRepoFileTree();
    }

    /**
     * Returns all the changed files in the working copy.
     * (*) New files.
     * (*) Modified files.
     * (*) Deleted files.
     *
     * @return
     */
    public Collection<RepoFile> getWorkingCopy() {
        TreeNode repoFilesRoot = workingCopy.getRepoFileTree();
        String lastCommitSha1 = fileManager.getHeadBranchCommitSha1();
        Collection<RepoFile> changedFiles = new ArrayList<>();

        if (lastCommitSha1 == null || lastCommitSha1.isEmpty()) {
            changedFiles = getFilesFromTree(repoFilesRoot);

            return changedFiles;
        }

        TreeNode prevCommitRoot = getPrevCommitFileTree();

        changedFiles = getChangedFiles(prevCommitRoot, getRepoFileTree());

        return changedFiles;
    }

    /**
     * Returns all the files that have been changed since last commit.
     *
     * @return
     */
    private Collection<RepoFile> getChangedFiles(TreeNode prevCommitFilesRoot, TreeNode repoFilesRoot) {
        Collection<RepoFile> repoFiles = getFilesFromTree(repoFilesRoot);

        if (prevCommitFilesRoot == null) {
            return repoFiles;
        }

        Collection<RepoFile> changedFiles = new ArrayList<>();
        Collection<RepoFile> prevCommitFiles = getFilesFromTree(prevCommitFilesRoot);

        Collection<RepoFile> deletedFiles = getDeletedFiles(prevCommitFiles, repoFiles);
        Collection<RepoFile> modifiedFiles = getModifiedFiles(prevCommitFiles, repoFiles);
        Collection<RepoFile> newFiles = getNewFiles(prevCommitFiles, repoFiles);

        changedFiles.addAll(deletedFiles);
        changedFiles.addAll(modifiedFiles);
        changedFiles.addAll(newFiles);

        return changedFiles;
    }

    private Collection<RepoFile> getNewFiles(Collection<RepoFile> oldList, Collection<RepoFile> newList) {
        Collection<RepoFile> newFiles = new ArrayList<>();
        HashMap<String, RepoFile> name2OldFile = new HashMap<>();

        for (RepoFile oldFile :
                oldList) {
            name2OldFile.put(oldFile.getName(), oldFile);
        }

        // TODO: functional programming with filters?
        for (RepoFile newFile :
                newList) {
            if (!name2OldFile.containsKey(newFile.getName())) { // TODO: find better identifier, names can be changed
                newFile.setChangeType(RepoFile.ChangeType.NEW);
                newFiles.add(newFile);
            }
        }

        return newFiles;
    }

    private Collection<RepoFile> getModifiedFiles(Collection<RepoFile> oldList, Collection<RepoFile> newList) {
        Collection<RepoFile> modifiedFiles = new ArrayList<>();
        HashMap<String, RepoFile> name2NewFile = new HashMap<>();

        for (RepoFile newFile :
                newList) {
            name2NewFile.put(newFile.getName(), newFile);
        }

        // TODO: functional programming with filters?
        for (RepoFile oldFile :
                oldList) {
            RepoFile newFile = name2NewFile.get(oldFile.getName());

            if (newFile == null) continue;

            if (!newFile.getId().equals(oldFile.getId())) {
                newFile.setChangeType(RepoFile.ChangeType.MODIFIED);
                modifiedFiles.add(newFile);
            }
        }

        return modifiedFiles;
    }

    /**
     * Returns a collection of all the files that existed in oldList and no longer do in newList.
     *
     * @return
     */
    private Collection<RepoFile> getDeletedFiles(Collection<RepoFile> oldList, Collection<RepoFile> newList) {
        Collection<RepoFile> deletedFiles = new ArrayList<>();
        HashMap<String, RepoFile> name2NewFile = new HashMap<>();

        for (RepoFile newFile :
                newList) {
            name2NewFile.put(newFile.getName(), newFile);
        }

        // TODO: functional programming with filters?
        for (RepoFile oldFile :
                oldList) {
            if (!name2NewFile.containsKey(oldFile.getName())) { // TODO: find a better way to search, name alone isn't the best
                oldFile.setChangeType(RepoFile.ChangeType.DELETED);
                deletedFiles.add(oldFile);
            }
        }

        return deletedFiles;
    }

    private Collection<RepoFile> getFilesFromTree(TreeNode root) {
        if (root == null) return null;

        Collection<RepoFile> fileCollection = new ArrayList<>();
        addFilesToList(root, fileCollection);

        return fileCollection;
    }

    private void addFilesToList(TreeNode root, Collection<RepoFile> fileCollection) {
        if (root == null) return;

        fileCollection.add(root.getRepoFile());

        for (TreeNode childNode :
                root.getChildren()) {
            addFilesToList(childNode, fileCollection);
        }
    }
}

