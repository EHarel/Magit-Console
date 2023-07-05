package magit;

import DataObjects.TreeNode;
import Repository.RepoManager;
import Repository.WorkingCopy;

public abstract class RepoAPI {
    public static int create(String path) {
        int resCode = RepoManager.createRepo(path);

        return resCode;
    }

    public static int commit(String path, String creator, String msg) {
        int resCode = RepoManager.commit(path, creator, msg);

        return resCode;
    }

    public static void showWC(String path) {
        WorkingCopy.showWCTree(path);
    }
}
