package magit;

import Repository.RepoManager;
import Repository.WorkingCopy;
import errors.exceptions.*;

import java.io.IOException;

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

    public static void showWC() {
        String repoPath = repoManager.getRepoPath();
        WorkingCopy.showWCTree(repoPath);
    }
}
