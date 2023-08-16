package menu.builder;

import menu.MenuAction;
import menu.MenuManager;
import menu.MenuPage;
import menu.action.*;

public class MainMenu {
    private static String username = "Administrator";
    private MenuManager menuManager;
    private static MenuPage mainMenu = new MenuPage("Main Menu", null);

    public MainMenu() {
        menuManager = initMenu();
    }

    public void runMenu() {
        if (menuManager != null) menuManager.RunMenu();
    }

    public static void setUsername(String newName) {
        username = newName;
    }

    public static String getUsername() {
        return username;
    }

    private MenuManager initMenu() {
        setMenu_UsernameUpdate();           // 1
        setMenu_CreateNewRepo();
        setMenu_RepoFileLoad();             // 2
        setMenu_SwitchRepo();               // 3
        setMenu_ShowCurrCommitFiles();      // 4
        setMenu_ShowWCStatus();             // 5
        setMenu_Commit();                   // 6
        setMenu_ShowBranches();             // 7
        setMenu_CreateBranch();             // 8
        setMenu_DeleteBranch();             // 9
        setMenu_CheckoutHeadBranch();       // 10
        setMenu_ShowActiveBranchHistory();  // 11
        setMenu_Exit();                     // 12

        menuManager = new MenuManager(mainMenu, false);

        return menuManager;
    }

    private void setMenu_UsernameUpdate() {
        String menuName = "Update username";
        new MenuAction(menuName, mainMenu, new UpdateUsername());
    }

    private void setMenu_CreateNewRepo() {
        String menuName = "Create a new repository";
        new MenuAction(menuName, mainMenu, new CreateRepo());
    }

    private void setMenu_RepoFileLoad() {

    }

    private void setMenu_SwitchRepo() {

    }

    private void setMenu_ShowCurrCommitFiles() {

    }

    private void setMenu_ShowWCStatus() {
        String menuName = "Print Working Copy";
        new MenuAction(menuName, mainMenu, new ShowWC());
    }

    private void setMenu_Commit() {
        String menuName = "Commit";
        new MenuAction(menuName, mainMenu, new Commit());
    }

    private void setMenu_ShowBranches() {

    }

    private void setMenu_CreateBranch() {
        String menuName = "Create branch";
        new MenuAction(menuName, mainMenu, new CreateBranch());
    }

    private void setMenu_DeleteBranch() {

    }

    private void setMenu_CheckoutHeadBranch() {
        String menuName = "Checkout (switch branch)";
        new MenuAction(menuName, mainMenu, new Checkout());
    }

    private void setMenu_ShowActiveBranchHistory() {

    }

    private void setMenu_Exit() {

    }
}
