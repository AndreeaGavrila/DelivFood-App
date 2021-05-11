package utils;

import java.util.ArrayList;
import java.util.Scanner;


public class MenuGeneric {

    private ArrayList<MenuItem> menuItems;
    private Scanner scanner;


    public MenuGeneric() {
        this.menuItems = new ArrayList<MenuItem>();
        this.scanner = new Scanner(System.in);
    }


    private void addMenuItem(String key, String name) {

        MenuItem menuItem = new MenuItem(key, name);
        menuItems.add(menuItem);
    }


    public void addMenuItem(String key, String name, Runnable runnable) {

        MenuItem menuItem = new MenuItem(key, name, runnable);
        menuItems.add(menuItem);
    }


    private void printMenuItems() {

        for (MenuItem menuItem : menuItems) {
            System.out.println("[" + menuItem.getKey() + "]: " + menuItem.getName());
        }
    }


    private void runCommand(String key) throws Exception {

        ArrayList<MenuItem> filteredMenuItems = new ArrayList<MenuItem>();

        // filter through the menu items, checking if the given key corresponds to a MenuItem

        for (MenuItem i : menuItems) {
            if (i.getKey().toLowerCase().equals(key))
                filteredMenuItems.add(i);
        }

        if (filteredMenuItems.size() > 0) {
            // if there are any menu items with the given key, run their runnables

            for (MenuItem i : filteredMenuItems) {
                i.getRunnable().run();
            }
        }
        else
            // if not, throw an exception that the key doesn't exist
            throw new Exception("Error - No valid option for '" + key + "' found, try again.");
    }


    private String scanLine() {

        System.out.print("> ");
        return scanner.nextLine();
    }


    private void addDefaultMenuItems() {
        addMenuItem("Q", "Quit");
    }


    public void initMenu() {

        addDefaultMenuItems();

        Boolean quit = false;

        while (!quit) {
            // print instructions
            System.out.println("\n--- Food Delivree ---\n");

            // print the menu items every time
            printMenuItems();

            // allow for input
            String option = scanLine();

            // show the input
            System.out.println("\n- Entered " + option);

            option = option.toLowerCase();

            // act depending on the input
            try {
                switch (option) {
                    case "q":
                        System.out.println("Thank you for using our app! :) ");
                        quit = true;
                        break;
                    default:
                        try {
                            runCommand(option);
                        } catch (Exception ex) {
                            System.out.println("Error - An error has occured: " + ex.getMessage());
                        }
                        break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            System.out.println();
        }
    }
}
