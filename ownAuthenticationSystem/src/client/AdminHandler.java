package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class AdminHandler implements RoleHandler {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public AdminHandler(Socket socket, PrintWriter out, BufferedReader in) {
        this.socket = socket;
        this.out = out;
        this.in = in;
    }

    @Override
    public void handle() {
        showMenu();
    }

    private void showMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("Please choose an action:");
            System.out.println("1. Add Users");
            System.out.println("2. Update Users");
            System.out.println("3. Delete Users");
            System.out.println("4. Add Menu Items");
            System.out.println("5. Update Menu Items");
            System.out.println("6. Delete Menu Items");
            System.out.println("7. View Menu");
            System.out.println("8. View Daily Review Comments");
            System.out.println("9. Generate Monthly Report");
            System.out.println("10. View Monthly Report");
            System.out.println("0. Exit");

            choice = scanner.nextInt();
            scanner.nextLine();
            handleUserChoice(choice, scanner);
        } while (choice != 0);

        System.out.println("Exiting...");
    }

    private void handleUserChoice(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                addUsers(scanner);
                break;
            case 2:
                updateUsers(scanner);
                break;
            case 3:
                deleteUsers(scanner);
                break;
            case 4:
                addMenuItems(scanner);
                break;
            case 5:
                updateMenuItems(scanner);
                break;
            case 6:
                deleteMenuItems(scanner);
                break;
            case 7:
                viewMenu();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    private void addUsers(Scanner scanner) {
        System.out.print("Enter new user's username: ");
        String username = scanner.nextLine();
        System.out.print("Enter new user's password: ");
        String password = scanner.nextLine();
        System.out.println("Enter the RoleID");
        Long roleId = scanner.nextLong();

        out.println("ADD_USER " + username + " " + password + " " + roleId);
        try {
            String response = in.readLine();
            System.out.println("Server reply: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateUsers(Scanner scanner) {
        System.out.print("Enter the username of the user to update: ");
        String username = scanner.nextLine();
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        System.out.print("Enter new RoleID: ");
        Long newRoleId = scanner.nextLong();
        scanner.nextLine(); // Consume newline

        out.println("UPDATE_USER " + username + " " + newPassword + " " + newRoleId);
        try {
            String response = in.readLine();
            System.out.println("Server reply: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteUsers(Scanner scanner) {
        System.out.print("Enter the username of the user to delete: ");
        String username = scanner.nextLine();

        out.println("DELETE_USER " + username);
        try {
            String response = in.readLine();
            System.out.println("Server reply: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addMenuItems(Scanner scanner) {
        System.out.print("Enter item name: ");
        String itemName = scanner.nextLine();
        System.out.print("Enter item price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter availability status (true/false): ");
        boolean availabilityStatus = scanner.nextBoolean();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter food item type ID: ");
        long foodItemTypeId = scanner.nextLong();
        scanner.nextLine(); // Consume newline

        out.println("ADD_MENU_ITEM " + itemName + " " + price + " " + availabilityStatus + " " + foodItemTypeId);
        try {
            String response = in.readLine();
            System.out.println("Server reply: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateMenuItems(Scanner scanner) {
        System.out.print("Enter food item ID to update: ");
        long foodItemId = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Enter new item name: ");
        String itemName = scanner.nextLine();
        System.out.print("Enter new item price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter new availability status (true/false): ");
        boolean availabilityStatus = scanner.nextBoolean();
        scanner.nextLine();
        System.out.print("Enter new food item type ID: ");
        long foodItemTypeId = scanner.nextLong();
        scanner.nextLine();

        out.println("UPDATE_MENU_ITEM " + foodItemId + " " + itemName + " " + price + " " + availabilityStatus + " " + foodItemTypeId);
        try {
            String response = in.readLine();
            System.out.println("Server reply: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteMenuItems(Scanner scanner) {
        System.out.print("Enter food item ID to delete: ");
        long foodItemId = scanner.nextLong();
        scanner.nextLine(); // Consume newline

        out.println("DELETE_MENU_ITEM " + foodItemId);
        try {
            String response = in.readLine();
            System.out.println("Server reply: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void viewMenu() {
        out.println("VIEW_MENU");
        try {
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
                if (response.equals("END_OF_MENU")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void viewDailyReviewComments() {
        //System.out.println(username + " is viewing daily review comments.");
        // Implement view daily review comments functionality
    }

    private void generateMonthlyReport() {
       // System.out.println(username + " is generating the monthly report.");
        // Implement generate monthly report functionality
    }

    private void viewMonthlyReport() {
        //System.out.println(username + " is viewing the monthly report.");
        // Implement view monthly report functionality
    }
}


