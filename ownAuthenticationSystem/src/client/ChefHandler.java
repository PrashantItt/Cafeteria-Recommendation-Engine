package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ChefHandler implements RoleHandler {
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader userInput;
    private final Map<Integer, Runnable> menuActions = new HashMap<>();
    private final Scanner scanner;

    public ChefHandler(Socket socket, PrintWriter out, BufferedReader userInput) {
        this.socket = socket;
        this.out = out;
        this.userInput = userInput;
        this.scanner = new Scanner(System.in);
        initializeMenuActions();
    }

    @Override
    public void handle(String userId) throws IOException {
        showMenu();
    }

    private void initializeMenuActions() {
        menuActions.put(1, this::rollOutMenuForNextDay);
        menuActions.put(2, this::finalizeMenuForNextDay);
        menuActions.put(3, this::viewMenu);
        menuActions.put(4, this::viewDiscardMenu);
        menuActions.put(5, this::getAllFeedbackOfDiscardedFoodName);
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
        System.out.println("1. Roll Out Menu for Next Day");
        System.out.println("2. Finalize Menu for Next Day");
        System.out.println("3. View Menu");
        System.out.println("4. View Discard Menu");
        System.out.println("5. View Discarded Feedback by Food Name");
        System.out.println("0. Logout");
    }

    private void invalidChoice() {
        System.out.println("Invalid choice. Please try again.");
    }

    private void rollOutMenuForNextDay() {
        try {
            System.out.print("Enter the number of items you want to view recommendations for: ");
            String numberOfItems = scanner.nextLine();
            sendRequest("CHEF_ROLL_OUT_MENU" + "#" + numberOfItems);
            receiveAndPrintResponse("END_OF_MENU");
        } catch (NumberFormatException e) {
            handleInvalidInput("Invalid input. Please enter a valid number.");
        }
    }

    private void finalizeMenuForNextDay() {
        System.out.print("Enter the MenuId for the next day (comma-separated IDs): ");
        String menuIds = scanner.nextLine();
        sendRequest("CHEF_FINALIZE_MENU" + "#" + menuIds);
        receiveAndPrintSingleResponse();
    }

    private void viewMenu() {
        sendRequest("CHEF_VIEW_MENU");
        receiveAndPrintResponse("END_OF_MENU");
    }

    private void viewDiscardMenu() {
        sendRequest("CHEF_DISCARD_MENU");
        receiveAndPrintResponse("END_OF_MENU");
        handleDiscardableFoodOptions();
    }

    private void handleDiscardableFoodOptions() {
        System.out.println("Console Options:");
        System.out.println("1. Remove the Food Item from Menu List (Should be done once a month)");
        System.out.println("2. Get Detailed Feedback (Should be done once a month)");
        System.out.print("Enter your choice (1 or 2): ");
        String choice = scanner.nextLine();

        if ("1".equals(choice)) {
            removeFoodItem();
        } else if ("2".equals(choice)) {
            getDetailedFeedback();
        } else {
            System.out.println("Invalid option. Please try again.");
        }
    }

    private void removeFoodItem() {
        sendRequest("REMOVE_FOOD_ITEM");
        System.out.print("Enter the name of the food item to be removed from the menu: ");
        String foodItemName = scanner.nextLine().trim();
        sendRequest("foodName" + "#" + foodItemName);
        receiveAndPrintResponse("End of Response");
    }

    private void getDetailedFeedback() {
        sendRequest("GET_DETAIL_FEEDBACK");
        System.out.print("Enter the name of the food item for which you want detailed feedback: ");
        String foodItemName = scanner.nextLine().trim();
        sendRequest(foodItemName);
        receiveAndPrintResponse("End of Response");
    }

    private void getAllFeedbackOfDiscardedFoodName() {
        System.out.print("Enter the name of the food item to view all feedback: ");
        String foodItemName = scanner.nextLine().trim();
        sendRequest("CHEF_GET_ALL_FEEDBACK_BY_FOOD_NAME" + "#" + foodItemName);
        receiveAndPrintResponse("END_OF_FEEDBACK");
    }

    private void logout() {
        sendRequest("LOGOUT");
        receiveAndPrintSingleResponse();
    }

    private void sendRequest(String request) {
        out.println(request);
        out.flush();
    }

    private void receiveAndPrintResponse(String endSignal) {
        try {
            String response;
            while ((response = userInput.readLine()) != null) {
                System.out.println(response);
                if (response.equals(endSignal)) {
                    break;
                }
            }
        } catch (IOException e) {
            handleServerCommunicationError(e);
        }
    }

    private void receiveAndPrintSingleResponse() {
        try {
            String response = userInput.readLine();
            System.out.println("Server reply: " + response);
        } catch (IOException e) {
            handleServerCommunicationError(e);
        }
    }

    private void handleInvalidInput(String message) {
        System.out.println(message);
    }

    private void handleServerCommunicationError(IOException e) {
        System.out.println("An error occurred while communicating with the server: " + e.getMessage());
    }
}
