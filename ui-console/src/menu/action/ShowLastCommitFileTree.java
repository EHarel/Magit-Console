package menu.action;

import dto.TreeNode;
import errors.exceptions.RepoNotSetException;
import magit.RepoAPI;
import menu.DoesAction;
import menu.utils.Utils;

public class ShowLastCommitFileTree implements DoesAction {
    @Override
    public void DoAction() {
        TreeNode root = null;
        try {
            root = RepoAPI.getLastCommitFileTree();

            String msg = "Would you like to show full file path as name? y/n";
            String userInput = menu.Utils.getInput(msg, true, false);

            boolean showFullPath = userInput.equals("y") || userInput.isEmpty();

            Utils.printFileTree(root, showFullPath);
        } catch (RepoNotSetException e) {
            Utils.repoNotSetMsg();
        }

    }
}
