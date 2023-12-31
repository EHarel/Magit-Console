package menu.action;

import errors.exceptions.*;
import magit.RepoAPI;
import menu.DoesAction;
import menu.utils.Utils;

public class CreateBranch implements DoesAction {
    @Override
    public void DoAction() {
        String msg = "Enter branch name:";
        String branchName = Utils.getInput(msg, false);
        if (branchName == null) return;

        String resMsg;
        try {
            RepoAPI.createBranch(branchName);
            resMsg = "Branch created.";
        } catch (ExistingBranchException e) {
            resMsg = "ERROR! Branch already exists.";
        } catch (IllegalNameException e) {
            resMsg = "ERROR! Invalid name. " + e.getMessage();
        } catch (RepoNotSetException e) {
            resMsg = "ERROR! Repository not yet set.";
        }

        System.out.println();
        System.out.println(resMsg);
    }
}
