package menu.action;

import menu.DoesAction;
import magit.RepoAPI;

import java.util.Scanner;


public class CreateRepo implements DoesAction {
    private static final String backOption = "back";

    @Override
    public void DoAction() {
        System.out.println("Enter path of new repository: ");
        String pathStr = getPathStr();
        if (pathStr.equals(backOption)) return;

        int resCode = RepoAPI.createRepo(pathStr);

        String msg;
        switch (resCode) {
            case 0:
                msg = "Repo created.";
                break;
            case 1:
                msg = "Invalid path, repo not created.";
                break;
            case 2:
                msg = "Repo already exists.";
                break;
            default:
                msg = "Unknown issue.";
                break;
        }

        System.out.println(msg + "\n");
    }

    private String getPathStr() {
        boolean validInput = false;
        Scanner scanner = new Scanner(System.in);
        String userInput;

        /* TODO:
         * Add a way to go back? Like in getTargetName() method.
         * */
        do {
            System.out.println("Please enter a path for the repository. Write '" + backOption + "' to go back.");
            userInput = scanner.nextLine();
            userInput = userInput.trim();
            if (userInput == backOption) {
                validInput = true;
            } else if (userInput.trim().isEmpty()) {
                System.out.println("You cannot enter an empty path.");
            } else if (!isValidPath(userInput)) {
                System.out.println("Path format incorrect.");
            } else {
                validInput = true;
            }
        } while (!validInput);

        return userInput;
    }

    // TODO:
    private boolean isValidPath(String path) {
        return true;
    }
}
