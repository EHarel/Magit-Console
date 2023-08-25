package menu.builder.interfaces;

import magit.RepoAPI;
import menu.GeneratesMessage;

public class RepoNameGenerator implements GeneratesMessage {
    @Override
    public String generateMessage() {
        String msg = "No repository loaded.";

        String loadedRepoName = RepoAPI.getLoadedRepoPath();
        if (loadedRepoName != null) {
            msg = "Active repository: " + loadedRepoName;
        }

        return msg;
    }
}
