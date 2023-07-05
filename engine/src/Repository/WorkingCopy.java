package Repository;

import DataObjects.FileData;
import DataObjects.TreeNode;
import DataObjects.files.Blob;
import DataObjects.files.Folder;
import DataObjects.files.RepoFile;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Stream;

public class WorkingCopy {

    // TODO: delete eventually, a method made for development
    public static void showWCTree(String wcPath) {
        TreeNode root = getTreeRec(wcPath);

        printTree(root);
    }

    public static TreeNode getWCTree(String wcPath) {
        TreeNode root = getTreeRec(wcPath);

        return root;
    }

    private static TreeNode getTreeRec(String filePath) {
        TreeNode tn = new TreeNode();
//        FileData fileData = new FileData();
        RepoFile fileData = null;

        File file = new File(filePath);

        if (file.isFile()) {
            fileData = getBlobData(file);
        } else if (file.isDirectory() && !file.getName().equals(".magit")) {
            for (File childFile : file.listFiles()) {
                if (childFile.getName().equals(".magit")) continue;

                TreeNode childNode = getTreeRec(childFile.getPath()); // Recursive call
                childNode.setParent(tn);
                tn.addChild(childNode);
            }

            fileData = getFolderData(tn, file);
        }

        tn.setFile(fileData);

        return tn;
    }

    private static RepoFile getFolderData(TreeNode tn, File file) {
        if (tn == null || file == null || !file.isDirectory()) return null;

        Folder fileData = new Folder();
        fileData.setName(file.getName());

        for (TreeNode childNode : tn.getChildren()) {
            fileData.addFile(childNode.getRepoFile());
        }

        return fileData;
    }
//    private static RepoFile getFolderData(TreeNode tn, File file) {
//        if (tn == null || file == null || !file.isDirectory()) return null;
//
//        // FileData fileData = new FileData();
//        Folder fileData = new Folder();
//
//
//        fileData.setName(file.getName());
//        // fileData.setFileType(FileData.FileType.FOLDER);
//
//        String childrenData = getChildrenData(tn);
//        String folderDataSha1 = DigestUtils.sha1Hex(childrenData);
//
//        fileData.setId(folderDataSha1);
//
//        return fileData;
//    }

    private static String getChildrenData(TreeNode treeNode) {
        if (treeNode == null || treeNode.getChildren().size() == 0) return null;

        StringBuilder dataBuilder = new StringBuilder();

        for (TreeNode tn : treeNode.getChildren()) {
            // String fileStr = tn.getRepoFile().toString();
            String fileStr = tn.getRepoFile().getMetaData();

            dataBuilder.append(fileStr + "\n");
        }

        String data = dataBuilder.toString();

        return data;
    }

    private static RepoFile getBlobData(File file) {
        /**
         * File is a blob.
         * Need to calculate its SHA1.
         */
        StringBuilder stringBuilder = new StringBuilder();
        Path filePath = Paths.get(file.getPath());
        try (Stream<String> stream = Files.lines(filePath, StandardCharsets.UTF_8)) {
            stream.forEach(s -> stringBuilder.append(s).append("\n"));
        } catch (IOException e) {
            //handle exception
            // TODO
        }

        String fileContent = stringBuilder.toString();


        // String sha1Str = DigestUtils.sha1Hex(fileContent);

        Blob treeFileData = new Blob();
        treeFileData.setName(file.getName());
        treeFileData.setContent(fileContent);
        // treeFileData.setId(sha1Str);
        // treeFileData.setFileType(FileData.FileType.BLOB);

//                System.out.println("File content:\n");
//                System.out.println(fileContent);
//                System.out.println("SHA1: " + sha1Str);

        return treeFileData;
    }

    private static void printTree(TreeNode root) {
        int level = 0;

        printTreeRec(root, level);
    }

    private static void printTreeRec(TreeNode root, int level) {
        String indent = "";
        for (int i = 0; i < level; i++) {
            for (int j = 0; j < 3; j++) {
                indent = indent + " ";
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
}
