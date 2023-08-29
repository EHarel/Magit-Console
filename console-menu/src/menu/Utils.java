package menu;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Utils {
    static public Scanner scanner = new Scanner(System.in);
    private static final String backOption = "back";

    static public boolean checkIfContinue() {
        
        System.out.println("Would you like to continue? y/n");
        
        String userInput;
        Scanner scanner = new Scanner(System.in);
        boolean validInput = false;
        boolean res = false;
        
        while (!validInput) {
            userInput = scanner.nextLine();
            userInput = userInput.trim();
            
            if (userInput.equals("y") || userInput.equals("n")) {
                validInput = true;
                res = userInput.equals("y");
            } else {
                System.out.println("Invalid input. Enter 'y' or 'n'.");
            }
        }
        
        return res;
    }

    /**
     *
     * @param msg To display to the user.
     * @param allowEmptyStr Determines if the user can input an empty string.
     * @param allowBackOption Determines if the user can go back.
     * @return null if the user chose to go back with the option allowed.
     */
    public static String getInput(String msg, boolean allowEmptyStr, boolean allowBackOption) {
        String userInput;
        Scanner scanner = new Scanner(System.in);
        boolean validInput = false;

        do {
            System.out.println(msg);

            if (allowBackOption) {
                System.out.println("Write '" + backOption + "' to go back.");
            }

            userInput = scanner.nextLine();
            userInput = userInput.trim();
            if (!allowEmptyStr && userInput.isEmpty()) {
                System.out.println("Blank input is not allowed.");
            } else {
                validInput = true;
            }
        } while (!validInput);

        if (allowBackOption && userInput.equals(backOption)) {
            userInput = null;
        }

        return userInput;
    }

    static public int ReadInt() {
        /* TODO
         * I had to insert an initial value (0) to userInput,
         * otherwise it wouldn't compile.
         * But this is a mostly wrong value (although one that's out of range and does nothing).
         * Find out a way to implement this method without an initial value.
         */
        int userInput = 0;

        boolean validInput = false;

        do {
            try {
                userInput = scanner.nextInt();
                validInput = true;
            } catch (InputMismatchException inputMismatchException) {
                System.out.println("Input isn't an integer. y u do dis? Try again, now with an integer (whole number)!");
                scanner.nextLine();
            } catch (Exception ignore) {

            }
        } while (!validInput);

        return userInput;
    }

    public static boolean isNullOrEmptyStr(String str) {
        return (str == null || str.trim().isEmpty());
    }
}
