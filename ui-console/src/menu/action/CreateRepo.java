package menu.action;

import menu.DoesAction;

public class CreateRepo implements DoesAction {

    @Override
    public void DoAction() {
        System.out.println("Enter path of new repository: ");
        String pathStr = getPathStr();
    }

    private String getPathStr() {
        String pathStr = "some path";

        boolean isValidPath = isValidPath();

        return pathStr;
    }

    // TODO:
    private boolean isValidPath() {
        return true;
    }
}
