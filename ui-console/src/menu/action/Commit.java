package menu.action;

import magit.RepoAPI;
import menu.DoesAction;
import menu.builder.MainMenu;
import menu.utils.Utils;

public class Commit implements DoesAction {
    @Override
    public void DoAction() {
        String repoPath = getRepoPath();
        if (repoPath == null) return;

        String creator = getCreatorName();

        String commitMsg = getCommitMsg();
        if (commitMsg == null) return;

        RepoAPI.commit(repoPath, creator, commitMsg);
    }

    private String getRepoPath() {
        String msg = "Please enter the path of the repository.";
        boolean validInput = false;
        String userInput = null;

        while (!validInput) {
            userInput = Utils.getInput(msg, false);
            if (userInput == null) return null;

            validInput = Utils.isValidPath(userInput);
        }

        return userInput;
    }

    private String getCommitMsg() {
        String msg = "Enter message for commit:";
        String userInput = Utils.getInput(msg, false);

        return userInput;
    }

    private String getCreatorName() {
        return MainMenu.getUsername();
    }
}
