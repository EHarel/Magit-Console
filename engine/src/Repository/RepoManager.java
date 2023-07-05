package Repository;

import DataObjects.Commit;
import DataObjects.TreeNode;
import DataObjects.files.RepoFile;
import utils.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedList;

public class RepoManager {
    public static final int ERROR_INVALID_PATH = 1;
    public static final int ERROR_EXISTING_REPO = 2;
    public static final int ERROR_UNKNOWN = 3;

    private static final String MAGIT_DIR = ".magit";

    /**
     * @param path
     * @return (*) 0 - repo created.
     * (*) 1 - invalid path.
     * (*) 2 - repo exists.
     */
    public static int createRepo(String path) {
        int resCode = 0;

        if (!isValidPath(path)) {
            return ERROR_INVALID_PATH;
        }

        if (isExistingRepo(path)) {
            return ERROR_EXISTING_REPO;
        }

        // Create directory
        resCode = createDirectories(path);
        createBranch("main", path);
        changeBranch(path, "main");

        return resCode;
    }

    private static void changeBranch(String mainDirPath, String branchName) {
        // Switch the HEAD file to point to branchName
        // Find branch file through branch file

        String branchCommitSha1 = FileManager.getBranchSha1(mainDirPath, branchName);
        Commit branchCommit = FileManager.getCommit(mainDirPath, branchCommitSha1);

        // Go to the objects folder
        // Find the commit
        // Delete WC
        // Spread out the commit files
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
            resCode = ERROR_UNKNOWN;
        }

        return resCode;
    }

    private static void createBranch(String name, String mainDirPath) {
        String branchesPath = mainDirPath + ".magit/branches/";
        String branchPath = branchesPath + name;

        try {
            Files.createFile(Paths.get(branchPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int commit(String path, String creator, String msg) {
        int resCode = 0;

        if (!isValidPath(path)) {
            return ERROR_INVALID_PATH;
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

        TreeNode wcRoot = WorkingCopy.getWCTree(path);
        Commit commit = createCommitData(wcRoot, creator, msg);

        Collection<RepoFile> changedFiles = getChangedFiles(wcRoot);
        zip(commit, changedFiles, path);

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
        // TODO: commit.setSecondaryAncestorSha1();

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
