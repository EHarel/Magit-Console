package menu.action;

import magit.RepoAPI;
import menu.DoesAction;
import menu.utils.Utils;

public class CreateBranch implements DoesAction {
    @Override
    public void DoAction() {
        String msg = "Enter repository path:";
        String repoPath = Utils.getInput(msg, false);
        if (repoPath == null) return;

        msg = "Enter branch name:";
        String branchName = Utils.getInput(msg, false);
        if (branchName == null) return;

        int resCode = RepoAPI.createBranch(repoPath, branchName);



        // TODO: find a proper way to communicate all the issues
        switch (resCode) {
            case 0:
                msg = "Branch created.";
                break;
            case 1:
                msg = "Invalid path, repo not created.";
                break;
            case 2:
                msg = "Repo already exists.";
                break;
            default:
                msg = "Unknown issue.";
                break;
        }

        System.out.println(msg + "\n");
    }
}
