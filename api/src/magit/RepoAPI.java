package magit;

import Repository.RepoManager;
import Repository.WorkingCopy;

public abstract class RepoAPI {
    public static int createRepo(String path) {
        return RepoManager.createRepo(path);
    }

    public static int createBranch(String repoPath, String branchName) {
        return RepoManager.createBranch(repoPath, branchName);
    }

    public static int checkout(String repoPath, String branchName) {
        return RepoManager.checkout(repoPath, branchName);
    }

    public static int commit(String path, String creator, String msg) {
        return RepoManager.commit(path, creator, msg);
    }

    public static void showWC(String path) {
        WorkingCopy.showWCTree(path);
    }


}
