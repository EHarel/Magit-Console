package menu.action;

import errors.exceptions.InvalidPathException;
import errors.exceptions.NoSuchRepoException;
import magit.RepoAPI;
import menu.DoesAction;
import menu.utils.Utils;

public class LoadRepository implements DoesAction {
    @Override
    public void DoAction() {
        String msg = "Enter repository path:";
        String repoPath = Utils.getInput(msg, false);
        if (repoPath == null) return;

        String resMsg;
        try {
            RepoAPI.changeRepository(repoPath);
            resMsg = "Repo loaded.";
        } catch (NoSuchRepoException e) {
            System.out.println();
            resMsg = "Repo doesn't exist.";
        } catch (InvalidPathException e) {
            resMsg = "Invalid path.";
        }

        System.out.println();
        System.out.println(resMsg);
    }
}
