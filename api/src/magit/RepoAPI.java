package magit;

import dto.TreeNode;
import Repository.RepoManager;
import dto.files.RepoFile;
import errors.exceptions.*;

import java.io.IOException;
import java.util.Collection;

public abstract class RepoAPI {
    private static RepoManager repoManager = new RepoManager();

    public static String getLoadedRepoPath() {
        return repoManager.getRepoPath();
    }

    public static String getActiveBranch() {
        return repoManager.getActiveBranch();
    }

    public static void changeRepository(String newRepoPath) throws NoSuchRepoException, InvalidPathException {
        repoManager.changeRepository(newRepoPath);
    }

    public static void createRepo(String path) throws ExistingRepoException, IOException, InvalidPathException {
        repoManager.createRepo(path);
    }

    public static void createBranch(String branchName) throws ExistingBranchException, IllegalNameException, RepoNotSetException {
        repoManager.createBranch(branchName);
    }

    public static void checkout(String branchName) throws RepoNotSetException, NoSuchBranchException {
        repoManager.checkout(branchName);
    }

    public static void commit(String creator, String msg) throws RepoNotSetException {
        repoManager.commit(creator, msg);
    }

    /**
     *
     * @return null if no repository is set.
     */
    public static TreeNode getRepoFileTree() {
        return repoManager.getRepoFileTree();
    }

    /**
     * Returns all the changed files in working copy.
     *      (*) New files.
     *      (*) Modified files.
     *      (*) Deleted files.
     */
    public static Collection<RepoFile> getWorkingCopy() throws RepoNotSetException {
        return repoManager.getWorkingCopy();
    }

    public static TreeNode getLastCommitFileTree() throws RepoNotSetException {
        return repoManager.getLastCommitFileTree();
    }
}
