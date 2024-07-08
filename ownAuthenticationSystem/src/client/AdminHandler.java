package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AdminHandler implements RoleHandler {
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;
    private final Map<Integer, Runnable> menuActions = new HashMap<>();
    private final Scanner scanner;

    public AdminHandler(Socket socket, PrintWriter out, BufferedReader in) {
        this.socket = socket;
        this.out = out;
        this.in = in;
        this.scanner = new Scanner(System.in);
        initializeMenuActions();
    }

    @Override
    public void handle() {
        showMenu();
    }

    private void initializeMenuActions() {
        menuActions.put(1, this::addUsers);
        menuActions.put(2, this::updateUsers);
        menuActions.put(3, this::deleteUsers);
        menuActions.put(4, this::addMenuItems);
        menuActions.put(5, this::updateMenuItems);
        menuActions.put(6, this::deleteMenuItems);
        menuActions.put(7, this::viewMenu);
    }

    private void showMenu() {
        int choice;
        do {
            printMenu();
            try {
                choice = Integer.parseInt(scanner.nextLine());
                menuActions.getOrDefault(choice, this::invalidChoice).run();
            } catch (NumberFormatException e) {
                handleInvalidInput("Invalid input. Please enter a number.");
                choice = -1;
            }
        } while (choice != 0);

        System.out.println("Logging out...");
        logout();
    }

    private void printMenu() {
        System.out.println("Please choose an action:");
        System.out.println("1. Add Users");
        System.out.println("2. Update Users");
        System.out.println("3. Delete Users");
        System.out.println("4. Add Menu Items");
        System.out.println("5. Update Menu Items");
        System.out.println("6. Delete Menu Items");
        System.out.println("7. View Menu");
        System.out.println("0. Logout");
    }

    private void invalidChoice() {
        System.out.println("Invalid choice. Please try again.");
    }

    private void addUsers() {
        try {
            System.out.print("Enter new user's username: ");
            String username = scanner.nextLine();
            System.out.print("Enter new user's password: ");
            String password = scanner.nextLine();
            System.out.print("Enter the RoleID: ");
            Long roleId = Long.parseLong(scanner.nextLine());

            sendRequest("ADMIN_ADD_USER#" + username + "#" + password + "#" + roleId);
            receiveAndPrintSingleResponse();
        } catch (NumberFormatException e) {
            handleInvalidInput("Invalid RoleID. Please enter a valid number.");
        } catch (IOException e) {
            handleServerCommunicationError(e);
        }
    }

    private void updateUsers() {
        try {
            System.out.print("Enter the username of the user to update: ");
            String username = scanner.nextLine();
            System.out.print("Enter new password: ");
            String newPassword = scanner.nextLine();
            System.out.print("Enter new RoleID: ");
            Long newRoleId = Long.parseLong(scanner.nextLine());

            sendRequest("ADMIN_UPDATE_USER#" + username + "#" + newPassword + "#" + newRoleId);
            receiveAndPrintSingleResponse();
        } catch (NumberFormatException e) {
            handleInvalidInput("Invalid RoleID. Please enter a valid number.");
        } catch (IOException e) {
            handleServerCommunicationError(e);
        }
    }

    private void deleteUsers() {
        try {
            System.out.print("Enter the username of the user to delete: ");
            String username = scanner.nextLine();

            sendRequest("ADMIN_DELETE_USER#" + username);
            receiveAndPrintSingleResponse();
        } catch (IOException e) {
            handleServerCommunicationError(e);
        }
    }

    private void addMenuItems() {
        try {
            System.out.print("Enter item name: ");
            String itemName = scanner.nextLine();
            System.out.print("Enter item price: ");
            double price = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter availability status (true/false): ");
            boolean availabilityStatus = Boolean.parseBoolean(scanner.nextLine());
            System.out.print("Enter food item type ID: ");
            long foodItemTypeId = Long.parseLong(scanner.nextLine());
            System.out.print("Select dietary preference (Vegetarian/Non Vegetarian/Eggetarian): ");
            String dietaryPreference = scanner.nextLine();
            System.out.print("Select spice level (High/Medium/Low): ");
            String spiceLevel = scanner.nextLine();
            System.out.print("Select cuisine preference (North Indian/South Indian/Other): ");
            String cuisinePreference = scanner.nextLine();
            System.out.print("Do you have a sweet tooth? (Yes/No): ");
            String sweetTooth = scanner.nextLine();

            sendRequest("ADMIN_ADD_MENU_ITEM#" + itemName + "#" + price + "#" + availabilityStatus + "#" + foodItemTypeId + "#" + dietaryPreference + "#" + spiceLevel + "#" + cuisinePreference + "#" + sweetTooth);
            receiveAndPrintSingleResponse();
        } catch (NumberFormatException e) {
            handleInvalidInput("Invalid input. Please enter the correct values.");
        } catch (IOException e) {
            handleServerCommunicationError(e);
        }
    }

    private void updateMenuItems() {
        try {
            System.out.print("Enter food item ID to update: ");
            long foodItemId = Long.parseLong(scanner.nextLine());
            System.out.print("Enter new item name: ");
            String itemName = scanner.nextLine();
            System.out.print("Enter new item price: ");
            double price = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter new availability status (true/false): ");
            boolean availabilityStatus = Boolean.parseBoolean(scanner.nextLine());
            System.out.print("Enter new food item type ID: ");
            long foodItemTypeId = Long.parseLong(scanner.nextLine());
            System.out.print("Enter new dietary preference (Vegetarian/Non Vegetarian/Eggetarian): ");
            String dietaryPreference = scanner.nextLine();
            System.out.print("Enter new spice level (High/Medium/Low): ");
            String spiceLevel = scanner.nextLine();
            System.out.print("Enter new cuisine preference (North Indian/South Indian/Other): ");
            String cuisinePreference = scanner.nextLine();
            System.out.print("Do you have a sweet tooth? (Yes/No): ");
            String sweetTooth = scanner.nextLine();

            sendRequest("ADMIN_UPDATE_MENU_ITEM#" + foodItemId + "#" + itemName + "#" + price + "#" + availabilityStatus + "#" + foodItemTypeId + "#" + dietaryPreference + "#" + spiceLevel + "#" + cuisinePreference + "#" + sweetTooth);
            receiveAndPrintSingleResponse();
        } catch (NumberFormatException e) {
            handleInvalidInput("Invalid input. Please enter the correct values.");
        } catch (IOException e) {
            handleServerCommunicationError(e);
        }
    }

    private void deleteMenuItems() {
        try {
            System.out.print("Enter food item ID to delete: ");
            long foodItemId = Long.parseLong(scanner.nextLine());

            sendRequest("ADMIN_DELETE_MENU_ITEM#" + foodItemId);
            receiveAndPrintSingleResponse();
        } catch (NumberFormatException e) {
            handleInvalidInput("Invalid food item ID. Please enter a valid number.");
        } catch (IOException e) {
            handleServerCommunicationError(e);
        }
    }

    private void viewMenu() {
        try {
            sendRequest("COMMON_VIEW_MENU");
            receiveAndPrintResponse("END_OF_MENU");
        } catch (IOException e) {
            handleServerCommunicationError(e);
        }
    }

    private void logout() {
        try {
            sendRequest("LOGOUT");
            receiveAndPrintSingleResponse();
        } catch (IOException e) {
            handleServerCommunicationError(e);
        }
    }

    private void sendRequest(String request) {
        out.println(request);
    }

    private void receiveAndPrintResponse(String endSignal) throws IOException {
        String response;
        while ((response = in.readLine()) != null) {
            System.out.println(response);
            if (response.equals(endSignal)) {
                break;
            }
        }
    }

    private void receiveAndPrintSingleResponse() throws IOException {
        String response = in.readLine();
        System.out.println("Server reply: " + response);
    }

    private void handleInvalidInput(String message) {
        System.out.println(message);
    }

    private void handleServerCommunicationError(IOException e) {
        System.out.println("An error occurred while communicating with the server: " + e.getMessage());
    }
}
