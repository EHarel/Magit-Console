package menu.action;

import errors.exceptions.RepoNotSetException;
import magit.RepoAPI;
import menu.DoesAction;
import menu.builder.MainMenu;
import menu.utils.Utils;

public class Commit implements DoesAction {
    @Override
    public void DoAction() {
        String creator = getCreatorName();
        String commitMsg = getCommitMsg();
        if (commitMsg == null) return;

        String resMsg;
        try {
            RepoAPI.commit(creator, commitMsg);
            resMsg = "Commit successful.";
        } catch (RepoNotSetException e) {
            resMsg = "ERROR! Repository not set.";
        }

        System.out.println();
        System.out.println(resMsg);
    }

    private String getCommitMsg() {
        String msg = "Enter message for commit:";

        return Utils.getInput(msg, false);
    }

    private String getCreatorName() {
        return MainMenu.getUsername();
    }
}
