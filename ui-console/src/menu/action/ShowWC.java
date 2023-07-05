package menu.action;

import magit.RepoAPI;
import menu.DoesAction;

public class ShowWC implements DoesAction {
    @Override
    public void DoAction() {
        String path = "c:/temp/repo4";

        RepoAPI.showWC(path);
    }
}
