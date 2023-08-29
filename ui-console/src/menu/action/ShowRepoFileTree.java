package menu.action;

import dto.TreeNode;
import magit.RepoAPI;
import menu.DoesAction;
import menu.utils.Utils;

public class ShowRepoFileTree implements DoesAction {
    @Override
    public void DoAction() {
        TreeNode root = RepoAPI.getRepoFileTree();

        if (root == null) {
            System.out.println("No repository set.");
            return;
        }

        Utils.printFileTree(root, false);

    }
}
