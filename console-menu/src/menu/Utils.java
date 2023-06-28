package menu;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Utils {
    static public Scanner scanner = new Scanner(System.in);

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
