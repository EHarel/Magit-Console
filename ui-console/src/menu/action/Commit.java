package menu.action;

import dto.files.RepoFile;
import errors.exceptions.RepoNotSetException;
import magit.RepoAPI;
import menu.DoesAction;
import menu.builder.MainMenu;

import java.util.Collection;

public class Commit implements DoesAction {
    @Override
    public void DoAction() {
        String resMsg;
        try {
            String creator = getCreatorName();

            System.out.println("These are the open changes:");

            Collection<RepoFile> changedFiles = RepoAPI.getWorkingCopy();

            String commitMsg = getCommitMsg();
            if (commitMsg == null) return;

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

        return menu.Utils.getInput(msg, false, false);
    }

    private String getCreatorName() {
        return MainMenu.getUsername();
    }
}
