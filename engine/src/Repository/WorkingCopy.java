package Repository;

import dto.TreeNode;
import dto.files.Blob;
import dto.files.Folder;
import dto.files.RepoFile;
/// import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class WorkingCopy {
    private String repoPath;

    /**
     * Creates the file tree of the repository in its current state.
     * All files are included, regardless of modification state.
     * @return
     */
    public TreeNode getRepoFileTree() {
        if (repoPath == null) return null;

        return getRepoFileTreeRec(repoPath);
    }

    private TreeNode getRepoFileTreeRec(String filePath) {
        TreeNode tn = new TreeNode();
        RepoFile fileData = null;
        File file = new File(filePath);

        if (file.isFile()) {
            fileData = getBlobData(file);
        } else if (file.isDirectory() && !file.getName().equals(FileManager.MAGIT_DIR)) {
            for (File childFile : file.listFiles()) {
                if (childFile.isHidden() || childFile.getName().equals(FileManager.MAGIT_DIR)) continue;

                TreeNode childNode = getRepoFileTreeRec(childFile.getPath()); // Recursive call
                childNode.setParent(tn);
                tn.addChildAndSetParent(childNode);
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

    private static String getChildrenData(TreeNode treeNode) {
        if (treeNode == null || treeNode.getChildren().isEmpty()) return null;

        StringBuilder dataBuilder = new StringBuilder();

        for (TreeNode tn : treeNode.getChildren()) {
            // String fileStr = tn.getRepoFile().toString();
            String fileStr = tn.getRepoFile().getMetaDataContent();

            dataBuilder.append(fileStr).append(System.lineSeparator());
        }

        return dataBuilder.toString();
    }

    private static RepoFile getBlobData(File file) {
        /**
         * File is a blob.
         * Need to calculate its SHA1.
         */
        StringBuilder stringBuilder = new StringBuilder();
        Path filePath = Paths.get(file.getPath());
        try (Stream<String> stream = Files.lines(filePath, StandardCharsets.UTF_8)) {
            stream.forEach(s -> stringBuilder.append(s).append(System.lineSeparator()));
        } catch (IOException e) {
            //handle exception
            // TODO
        }

        String fileContent = stringBuilder.toString();
        Blob treeFileData = new Blob();
        treeFileData.setName(file.getName());
        treeFileData.setContent(fileContent);

        return treeFileData;
    }

    /**
     * Changes the repository of the working copy. Note that the checks occur before calling this class.
     * @param newRepoPath
     */
    public void setRepoPath(String newRepoPath) {
        this.repoPath = newRepoPath;
    }
}
