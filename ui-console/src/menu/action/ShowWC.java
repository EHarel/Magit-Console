package menu.action;

import magit.RepoAPI;
import menu.DoesAction;

public class ShowWC implements DoesAction {
    @Override
    public void DoAction() {
        RepoAPI.showWC();
    }
}
