package menu.utils;

import dto.TreeNode;
import dto.files.RepoFile;
import utils.SharedUtils;

import java.util.Collection;
import java.util.Scanner;

public abstract class Utils {
    private static final String backOption = "back";

    public static void printFiles(Collection<RepoFile> files) {
        for (RepoFile file : files) {
            String str =
                    "[" +
                            file.getChangeType().toString() +
                            "] " +
                            file.getName(); // TODO: change to full path
            System.out.println(str);
        }
    }

    public static void printFileTree(TreeNode root) {
        printTreeRec(root, 0);
    }

    private static void printTreeRec(TreeNode root, int level) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < level; i++) {
            for (int j = 0; j < 3; j++) {
                indent.append(" ");
            }
        }

        String input = indent + root.getRepoFile().getName() + " - " + root.getRepoFile().getFileType();
        System.out.println(input);

        Collection<TreeNode> children = root.getChildren();

        for (TreeNode tn :
                children) {
            printTreeRec(tn, level + 1);
        }
    }

    /**
     * This method reads user input. It allows the user to cancel and go back.
     *
     * @param msg A message to the user regarding the sort of input to receive.
     * @return null if the user chose to go back.
     */
    public static String getInput(String msg, boolean allowEmptyStr) {

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
