package menu.utils;

import dto.TreeNode;
import dto.files.RepoFile;
import utils.SharedUtils;

import java.util.Collection;

public abstract class Utils {
    public static void printFiles(Collection<RepoFile> files) {
        if (files.isEmpty()) {
            System.out.println("No files changed.");
        }

        for (RepoFile file : files) {
            String str =
                    "[" +
                            file.getChangeType().toString() +
                            "] " +
                            file.getName(); // TODO: change to full path
            System.out.println(str);
        }
    }

    public static void printFileTree(TreeNode root, boolean printFullPath) {
        printTreeRec(root, 0, printFullPath);
    }

    private static void printTreeRec(TreeNode root, int level, boolean printFullPath) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < level; i++) {
            for (int j = 0; j < 3; j++) {
                indent.append(" ");
            }
        }

        String name = printFullPath ? root.getRepoFile().getFullPath() : root.getRepoFile().getName();
        String input = indent + name + " - " + root.getRepoFile().getFileType();
        System.out.println(input);

        Collection<TreeNode> children = root.getChildren();

        for (TreeNode tn :
                children) {
            printTreeRec(tn, level + 1, printFullPath);
        }
    }

    public static boolean isValidFilePath(String pathStr) {
        return SharedUtils.isValidFilePath(pathStr);
    }

    public static void repoNotSetMsg() {
        System.out.println("You must first load a repository.");
    }
}
