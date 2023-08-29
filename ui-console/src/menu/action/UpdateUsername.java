package menu.action;

import menu.DoesAction;
import menu.Utils;
import menu.builder.MainMenu;

public class UpdateUsername implements DoesAction {

    @Override
    public void DoAction() {
        String msg = "Enter a new username: ";
        String userInput = Utils.getInput(msg, false, true);

        if (userInput != null) {
            MainMenu.setUsername(userInput);
        }
    }
}
