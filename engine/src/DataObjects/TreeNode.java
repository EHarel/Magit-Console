package DataObjects;

import DataObjects.files.RepoFile;

import java.util.Collection;
import java.util.LinkedList;

public class TreeNode {
    private TreeNode parent;
    private RepoFile repoFile;
    private Collection<TreeNode> children;

    public TreeNode() {
        children = new LinkedList<>();
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public RepoFile getRepoFile() {
        return repoFile;
    }

    public void setFile(RepoFile repoFile) {
        this.repoFile = repoFile;
    }

    public Collection<TreeNode> getChildren() {
        return children;
    }

    public void addChild(TreeNode treeNode) {
        children.add(treeNode);
    }
}
