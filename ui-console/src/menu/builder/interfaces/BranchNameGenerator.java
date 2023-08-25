package menu.builder.interfaces;

import magit.RepoAPI;
import menu.GeneratesMessage;

public class BranchNameGenerator implements GeneratesMessage {
    @Override
    public String generateMessage() {
        String branchName = RepoAPI.getActiveBranch();
        String msg = null;

        if (branchName != null) {
            msg = "Active branch: " + branchName;
        }

        return msg;
    }
}
