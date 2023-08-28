package Repository;

import DataObjects.Commit;
import DataObjects.TreeNode;
import DataObjects.files.RepoFile;
import errors.codes.ErrorCodes;
import errors.exceptions.*;
import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedList;

public class RepoManager {
    private String repoPath = null;

    public String getRepoPath() { return repoPath; }

    public void changeRepository(String newRepoPath) throws InvalidPathException, NoSuchRepoException {
        if (!FileManager.isValidPath(newRepoPath)) {
            throw new InvalidPathException(newRepoPath);
        }

        if (!FileManager.isExistingRepo(newRepoPath)) {
            String msg = "The directory " + newRepoPath + " is not a MAGit repository.";
            throw new NoSuchRepoException(msg);
        }

        this.repoPath = newRepoPath;
    }

    /**
     * This function checks if a repository is currently set. If not, it throws an exception.
     */
    private void checkAndThrowSetRepo() throws RepoNotSetException {
        if (repoPath == null) throw new RepoNotSetException("No MAGit repository is set.");
    }


    /**
     * @param repoPath
     * @return Error codes:
     * (*) 0 SUCCESS - repo created.
     * (*) INVALID PATH - a problem with the path string.
     * (*) REPO EXISTS - repo exists.
     */
    public void createRepo(String repoPath) throws InvalidPathException, ExistingRepoException, IOException {
        if (!FileManager.isValidPath(repoPath)) throw new InvalidPathException(null);
        if (FileManager.isExistingRepo(repoPath)) throw new ExistingRepoException(null);

        String primaryBranchName = "main";

        // Create directory
        createDirectories(repoPath);
        createHeadBranch(repoPath, primaryBranchName);
        String headPath = FileManager.getHeadPath(repoPath);
        FileManager.writeToFile(headPath, primaryBranchName, false);
    }

    public void checkout(String branchName) throws RepoNotSetException, UnknownError, NoSuchBranchException {
        checkAndThrowSetRepo();

        try {
            String branchCommitSha1 = FileManager.getBranchSha1(repoPath, branchName);
            Commit branchCommit = FileManager.getCommit(repoPath, branchCommitSha1); // TODO not done
            FileManager.deleteWC(repoPath);
            FileManager.unfoldCommit(repoPath, branchCommit);
            String headPath = FileManager.getHeadPath(repoPath);
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
     * the process is slightly different than just creating a new branch.
     * The main branch at first doesn't have a previous commit to point to.
     *
     * @param repoPath
     * @param primaryBranchName
     */
    public int createHeadBranch(String repoPath, String primaryBranchName) {

        // FIXME: duplicate code with createBranch
        int resCode = 0;

        if (!FileManager.isValidPath(repoPath)) return ErrorCodes.ERROR_INVALID_PATH;

        String branchesPath = FileManager.getBranchesPath(repoPath);
        String branchPath = branchesPath + primaryBranchName;

        if (new File(branchPath).isFile()) return ErrorCodes.ERROR_EXISTING_BRANCH;

        try {
            Files.createFile(Paths.get(branchPath));
            resCode = 0;
        } catch (IOException e) {
            e.printStackTrace();
            resCode = ErrorCodes.ERROR_UNKNOWN;
        }

        return resCode;
    }

    /**
     * @param branchName
     * @return Result code. Potential errors:
     * (*) Illegal Branch Name
     * (*) Invalid Path
     * (*) Existing Branch
     * (*) Unknown
     */
    public void createBranch(String branchName) throws RepoNotSetException, IllegalNameException, ExistingBranchException, UnknownError {
        checkAndThrowSetRepo();
        if (branchName.contains(" ")) throw new IllegalNameException("Name cannot contain spaces.");

        String branchPath = FileManager.getBranchesPath(this.repoPath) + branchName;
        if (new File(branchPath).isFile()) throw new ExistingBranchException(null);

        try {
            Files.createFile(Paths.get(branchPath));
            String headBranchName = FileManager.getHeadBranchName(repoPath);
            String headBranchCommitSha1 = FileManager.getBranchSha1(repoPath, headBranchName);
            FileManager.writeToFile(branchPath, headBranchCommitSha1, false);
        } catch (IOException e) {
            e.printStackTrace();
            throw new UnknownError();
        }
    }

    public void commit(String creator, String msg) throws RepoNotSetException, UnknownError {
        checkAndThrowSetRepo();

        /**
         * SCAN WC:
         *      Scan all WC files and compare them to previous commit.
         *      Previous commit is pointed to via HEAD file.
         *          -   Go to head file.
         *          -   Read branch name in head file.
         *          -   Find branch file by its name.
         *          -   Read branch file SHA1 content (commit).
         *          -   Find commit file.
         *          -   'Open' commit file and unfold its tree.
         *
         */

        try {
            TreeNode wcRoot = WorkingCopy.getWCTree(repoPath);
            Commit commit = createCommitData(wcRoot, creator, msg);
            Collection<RepoFile> changedFiles = getChangedFiles(wcRoot);
            zip(commit, changedFiles, repoPath);
            FileManager.advanceHeadBranch(repoPath, commit.getSha1());
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownError();
        }
    }

    private void zip(Commit commit, Collection<RepoFile> changedFiles, String mainDir) {
        String objectsDirPath = FileManager.getObjectsPath(mainDir);
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

    private Collection<RepoFile> getChangedFiles(TreeNode wcRoot) {
        Collection<RepoFile> changedFiles = new LinkedList<>();

        // TODO:
        // Unfold the previous file tree from the last commit
        // Compare items
        // Add items that have changed to the list

        // FIXME quick and dirty
        // This just adds the whole wcRoot to the changed files
        addToList(wcRoot, changedFiles);

        return changedFiles;
    }

    private void addToList(TreeNode treeNode, Collection<RepoFile> changedFiles) {
        RepoFile repoFile = treeNode.getRepoFile();
        changedFiles.add(repoFile);

        for (TreeNode childNode : treeNode.getChildren()) {
            addToList(childNode, changedFiles);
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
            return FileManager.getHeadBranchName(repoPath);
        } catch (NoSuchFileException e) {
            return null;
        }
    }
}
