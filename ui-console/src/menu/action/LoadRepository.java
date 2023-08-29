package menu.action;

import errors.exceptions.InvalidPathException;
import errors.exceptions.NoSuchRepoException;
import magit.RepoAPI;
import menu.DoesAction;

public class LoadRepository implements DoesAction {
    @Override
    public void DoAction() {
        String msg = "Enter repository path:";
        String repoPath = menu.Utils.getInput(msg, false, true);
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
