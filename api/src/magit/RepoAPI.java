package magit;

import Repository.RepoManager;

public abstract class RepoAPI {
    public static int create(String path) {
        int resCode = RepoManager.createRepo(path);

        return resCode;
    }
}
