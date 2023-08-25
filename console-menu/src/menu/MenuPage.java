package menu;

import java.util.ArrayList;
import java.util.List;

public class MenuPage extends MenuItem {
    protected List<MenuItem> subMenuItems;
    private final List<GeneratesMessage> msgGenerators; // This variable adds a message when printing the submenu.

    public MenuPage(String subMenuName, MenuPage parentItem) {
        this(subMenuName, parentItem, null);
    }

    public MenuPage(String subMenuName, MenuPage parentItem, GeneratesMessage msgGenerator) {
        super(subMenuName, parentItem, true);
        subMenuItems = new ArrayList<>();
        List<GeneratesMessage> msgGenerators = new ArrayList<>();

        if (msgGenerator != null) {
            msgGenerators.add(msgGenerator);
        }

        this.msgGenerators = msgGenerators;
    }

    public void addMsgGenerator(GeneratesMessage msgGenerator) {
        this.msgGenerators.add(msgGenerator);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(name).append(System.lineSeparator());
        int i = 1;

        if (!msgGenerators.isEmpty()) {
            for (GeneratesMessage msgGenerator :
                    msgGenerators) {
                String msg = msgGenerator.generateMessage();
                if (msg != null) {
                    res.append(msg).append(System.lineSeparator());
                }
            }
        }

        for (MenuItem item : subMenuItems) {
            res.append("\n").append(addNumberAndName(i, item.name));

            if (!Utils.isNullOrEmptyStr(item.getComments())) {
                res.append("\n\t\t").append(item.getComments());
            }

            i++;
        }

        return res.toString();
    }


    @Override
    public int getOptionRange() {
        return subMenuItems.size();
    }

    public MenuItem getItem(int index) {
        MenuItem res = null;

        if (subMenuItems != null) {
            res = subMenuItems.get(index);
        }

        return res;
    }

    public boolean addItem(MenuItem newItem) {
        boolean res;

        res = subMenuItems.add(newItem);

        return res;
    }

    @Override
    public void updateMenuItemText() {
    }
}
