package menu.action;

import menu.DoesAction;
import menu.builder.MainMenu;

import java.util.Scanner;

public class UpdateUsername implements DoesAction {

    @Override
    public void DoAction() {
        System.out.print("Enter a new username: ");

        String userInput;
        Scanner scanner = new Scanner(System.in);
        boolean validInput = false;

        do {
            userInput = scanner.nextLine();
            userInput = userInput.trim();
            if (userInput.isEmpty()) {
                System.out.print("You cannot enter a blank name. Enter a new name: ");
            } else {
                validInput = true;
            }
        } while (!validInput);

        MainMenu.setUsername(userInput);
    }
}
