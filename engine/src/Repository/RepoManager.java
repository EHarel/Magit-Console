package Repository;

import DataObjects.Commit;
import DataObjects.TreeNode;
import DataObjects.files.RepoFile;
import errors.codes.ErrorCodes;
import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedList;

public class RepoManager {
//    public static final int ERROR_UNKNOWN = 1;
//    public static final int ERROR_INVALID_PATH = 2;
//    public static final int ERROR_EXISTING_REPO = 3;
//    public static final int ERROR_EXISTING_BRANCH = 4;
//    public static final int ERROR_ILLEGAL_BRANCH_NAME = 5;

    private static final String MAGIT_DIR = ".magit";

    /**
     * @param repoPath
     * @return Error codes:
     * (*) 0 SUCCESS - repo created.
     * (*) INVALID PATH - a problem with the path string.
     * (*) REPO EXISTS - repo exists.
     */
    public static int createRepo(String repoPath) {
        int resCode = ErrorCodes.SUCCESS;

        if (!isValidPath(repoPath)) {
            return ErrorCodes.ERROR_INVALID_PATH;
        }

        if (isExistingRepo(repoPath)) {
            return ErrorCodes.ERROR_EXISTING_REPO;
        }

        String primaryBranchName = "main";

        // Create directory
        resCode = createDirectories(repoPath);
        createHeadBranch(repoPath, primaryBranchName);
        String headPath = FileManager.getHeadPath(repoPath);
        FileManager.writeToFile(headPath, primaryBranchName, false);

        return resCode;
    }

    public static int checkout(String repoPath, String branchName) {
        String branchCommitSha1 = FileManager.getBranchSha1(repoPath, branchName);
        Commit branchCommit = FileManager.getCommit(repoPath, branchCommitSha1); // TODO not done
        FileManager.deleteWC(repoPath);
        FileManager.unfoldCommit(repoPath, branchCommit);
        String headPath = FileManager.getHeadPath(repoPath);
        FileManager.writeToFile(headPath, branchName, false);

        return 0; // FIXME: change to exceptions
    }

    // TODO
    private static boolean isValidPath(String path) {
        if (path == null || path.trim().isEmpty()) {
            return false;
        }

        boolean res;
        try {
            Paths.get(path);
            res = true;
        } catch (Exception e) {
            res = false;
        }

        return res;
    }

    // TODO
    private static boolean isExistingRepo(String path) {
        String fullPath = appendToPath(path, MAGIT_DIR);

        return Files.exists(Paths.get(fullPath));
    }

    private static String appendToPath(String path, String addition) {
        path = path.trim();
        char lastChar = path.charAt(path.length() - 1);
        if (lastChar != '/' && lastChar != '\\') {
            path = path + "\\";
        }

        String fullPath = path + addition;

        return fullPath;
    }

    private static int createDirectories(String path) {
        int resCode;

        String fullPath = appendToPath(path, MAGIT_DIR);
        try {
            Files.createDirectories(Paths.get(fullPath));

            String objPath = appendToPath(fullPath, "objects");
            Files.createDirectories(Paths.get(objPath));

            String branchesPath = appendToPath(fullPath, "branches");
            Files.createDirectories(Paths.get(branchesPath));

            String headPath = appendToPath(branchesPath, "HEAD");
            Files.createFile(Paths.get(headPath));

            resCode = 0;
        } catch (IOException e) {
            e.printStackTrace();
            resCode = ErrorCodes.ERROR_UNKNOWN;
        }

        return resCode;
    }

    /**
     * When creating the main branch for the first time
     * the process is slightly different than just creating a new branch.
     * The main branch at first doesn't have a previous commit to point to.
     * @param repoPath
     * @param primaryBranchName
     */
    public static int createHeadBranch(String repoPath, String primaryBranchName) {

        // FIXME: duplicate code with createBranch
        int resCode = 0;

        if (!FileManager.isValidPath(repoPath)) return ErrorCodes.ERROR_INVALID_PATH;

        String branchesPath = repoPath + "/.magit/branches/";
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
     * @param repoPath
     * @param branchName
     * @return Result code. Potential errors:
     * (*) Illegal Branch Name
     * (*) Invalid Path
     * (*) Existing Branch
     * (*) Unknown
     */
    public static int createBranch(String repoPath, String branchName) {
        int resCode = 0;

        if (branchName.contains(" ")) return ErrorCodes.ERROR_ILLEGAL_BRANCH_NAME;
        if (!FileManager.isValidPath(repoPath)) return ErrorCodes.ERROR_INVALID_PATH;

        String branchesPath = repoPath + "/.magit/branches/";
        String branchPath = branchesPath + branchName;

        if (new File(branchPath).isFile()) return ErrorCodes.ERROR_EXISTING_BRANCH;

        try {
            Files.createFile(Paths.get(branchPath));
            String headBranchName = FileManager.getHeadBranchName(repoPath);
            String headBranchCommitSha1 = FileManager.getBranchSha1(repoPath, headBranchName);
            FileManager.writeToFile(branchPath, headBranchCommitSha1, false);
            resCode = 0;
        } catch (IOException e) {
            e.printStackTrace();
            resCode = ErrorCodes.ERROR_UNKNOWN;
        }

        return resCode;
    }

    public static int commit(String repoPath, String creator, String msg) {
        int resCode = 0;

        if (!isValidPath(repoPath)) {
            return ErrorCodes.ERROR_INVALID_PATH;
        }

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


//        String headBranch = getHeadBranch();
//        File branchFile = getBranch(headBranch);
//        String commitSHA1 = getSHA1FromBranch(branch);
//        File commitFile = getCommit(commitSHA1);

        TreeNode wcRoot = WorkingCopy.getWCTree(repoPath);
        Commit commit = createCommitData(wcRoot, creator, msg);

        Collection<RepoFile> changedFiles = getChangedFiles(wcRoot);
        zip(commit, changedFiles, repoPath);

        FileManager.advanceHeadBranch(repoPath, commit.getSha1());

        return resCode;
    }

    private static void zip(Commit commit, Collection<RepoFile> changedFiles, String mainDir) {
        String objectsDirPath = getObjectsDirPath(mainDir);
        String sha1 = commit.getSha1();
        String commitContent = commit.toString();
        String commitZipPath = objectsDirPath + "/" + sha1;
        FileManager.zip(commitZipPath, sha1, commitContent);

        for (RepoFile fd : changedFiles) {
            String fileSha1 = fd.getId();
            String zipName = fileSha1 + ".zip";
            String fileName = fd.getName() + ".txt";
            String content = fd.getContent();
            String zipPath = objectsDirPath + "/" + zipName;
            FileManager.zip(zipPath, fileName, content);
        }
    }

    private static Collection<RepoFile> getChangedFiles(TreeNode wcRoot) {
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

    private static void addToList(TreeNode treeNode, Collection<RepoFile> changedFiles) {
        RepoFile repoFile = treeNode.getRepoFile();
        changedFiles.add(repoFile);

        for (TreeNode childNode : treeNode.getChildren()) {
            addToList(childNode, changedFiles);
        }
    }


    private static Commit createCommitData(TreeNode wcRoot, String creator, String msg) {
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

    private static String getObjectsDirPath(String mainDirPath) {
        String objPath = mainDirPath + "/.magit/objects";

        return objPath;
    }
}
