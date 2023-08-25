package menu.utils;

import utils.SharedUtils;

import java.util.Scanner;

public abstract class Utils {
    private static final String backOption = "back";

    /**
     * This method reads user input. It allows the user to cancel and go back.
     * @param msg A message to the user regarding the sort of input to receive.
     * @return null if the user chose to go back.
     */
    public static String getInput(String msg, boolean allowEmptyStr ) {

        String userInput;
        Scanner scanner = new Scanner(System.in);
        boolean validInput = false;

        do {
            System.out.println(msg);
            System.out.println("Write '" + backOption + "' to go back.");

            userInput = scanner.nextLine();
            userInput = userInput.trim();
            if (!allowEmptyStr && userInput.isEmpty()) {
                System.out.println("Blank input is not allowed.");
            } else {
                validInput = true;
            }
        } while (!validInput);

        return userInput;
    }

    public static boolean isValidFilePath(String pathStr) {
        return SharedUtils.isValidFilePath(pathStr);
    }
}
