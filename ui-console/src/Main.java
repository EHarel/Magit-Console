import menu.builder.MainMenu;

public class Main {
    public static void main(String[] args) {
        System.out.println("\nHello!\n");
        MainMenu mainMenu = new MainMenu();
        mainMenu.runMenu();
        System.out.println("\nGoodbye!\n");
    }
}