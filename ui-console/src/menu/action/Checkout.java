package menu.action;

import errors.exceptions.NoSuchBranchException;
import errors.exceptions.RepoNotSetException;
import magit.RepoAPI;
import menu.DoesAction;
import menu.utils.Utils;

public class Checkout implements DoesAction {
    @Override
    public void DoAction() {
        String msg = "Enter branch name:";
        String branchName = Utils.getInput(msg, false);
        if (branchName == null) return;

        String resMsg;
        try {
            RepoAPI.checkout(branchName);
            resMsg = "Branch " + branchName + "checked out.";
        } catch (RepoNotSetException e) {
            resMsg = "ERROR! No repository set.";
        } catch (NoSuchBranchException e) {
            resMsg = "ERROR! Couldn't find branch " + branchName;
        }

        System.out.println();
        System.out.println(resMsg);
    }
}

