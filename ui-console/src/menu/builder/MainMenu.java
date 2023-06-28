package menu.builder;

import menu.MenuAction;
import menu.MenuManager;
import menu.MenuPage;
import menu.action.*;

public class MainMenu {
    private static MenuManager graphMenuManager;
    private static MenuPage mainMenu = new MenuPage("Main Menu", null);

    public static MenuManager initMenu() {
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

        graphMenuManager = new MenuManager(mainMenu, false);

        return graphMenuManager;
    }

    private static void setMenu_UsernameUpdate() {
        String menuName = "Update username";
        new MenuAction(menuName, mainMenu, new UpdateUsername());
    }

    private static void setMenu_CreateNewRepo() {
        String menuName = "Create a new repository";
        new MenuAction(menuName, mainMenu, new CreateRepo());
    }

    private static void setMenu_RepoFileLoad() {

    }

    private static void setMenu_SwitchRepo() {

    }

    private static void setMenu_ShowCurrCommitFiles() {

    }

    private static void setMenu_ShowWCStatus() {

    }

    private static void setMenu_Commit() {

    }

    private static void setMenu_ShowBranches() {

    }

    private static void setMenu_CreateBranch() {

    }

    private static void setMenu_DeleteBranch() {

    }

    private static void setMenu_CheckoutHeadBranch() {

    }

    private static void setMenu_ShowActiveBranchHistory() {

    }

    private static void setMenu_Exit() {

    }
}
