package menu;

public abstract class MenuItem {
    protected String name;
    protected MenuPage parentMenuItem;
    protected String comments;

    final protected boolean isSubMenu;

    // constructor
    public MenuItem(String name, MenuPage parent, boolean isSubMenu) {
        this.name = name;
        this.isSubMenu = isSubMenu;
        setParentMenu(parent);
    }

    public boolean isSubMenu() {
        return isSubMenu;
    }

    // name getter-setter
    public String getName() {
        return name;
    }

    public boolean setName(String newName) {
        boolean res;

        if (newName == null || newName.isEmpty()) {
            res = false;
        }
        else {
            this.name = newName;
            res = true;
        }

        return res;
    }


    // comments getter-setter
    public String getComments () {
        return comments;
    }

    /**
     * Replaces old comment with new comment.
     */
    public void setComments(String newComments) {
        this.comments = newComments;
    }

    /**
     * Appends new comments to existing comments.
     */
    public void addComments(String newComments) {
        this.comments = this.comments + "\n" + newComments;
    }


    // parentMenu getter-setter
    public MenuItem getParentMenu() {
        return parentMenuItem;
    }

    public boolean setParentMenu(MenuPage newParent) {
        boolean res = true;
        this.parentMenuItem = newParent;
        if (parentMenuItem != null) {
            parentMenuItem.addItem(this);
        }

        return res;
    }

    /**
     *
     * @return indices of available options, meaning [0 ... size-1].
     *
     * For single action menus the return is 1.
     */
    public int getOptionRange() {
        return 1;
    }

    public abstract MenuItem getItem(int index);

    public abstract void updateMenuItemText();

    public static String addNumberAndName(int i, String text) {
        return "\t" + i + ".\t" + text;
    }
}
