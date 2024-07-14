package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class EmployeeHandler implements RoleHandler {
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;
    private final Map<Integer, Runnable> menuActions = new HashMap<>();
    private final Scanner scanner;
    private long userId;

    public EmployeeHandler(Socket socket, PrintWriter out, BufferedReader in) {
        this.socket = socket;
        this.out = out;
        this.in = in;
        this.scanner = new Scanner(System.in);
        initializeMenuActions();
    }

    @Override
    public void handle(String userId) {
        this.userId = Long.parseLong(userId);
        showMenu();
    }

    private void initializeMenuActions() {
        menuActions.put(1, this::viewMenu);
        menuActions.put(2, this::viewNotifications);
        menuActions.put(3, this::selectFoodItemsForTomorrow);
        menuActions.put(4, this::submitFeedback);
        menuActions.put(5, this::discardMenuFeedback);
        menuActions.put(6, this::createProfile);
        menuActions.put(7, this::updateProfile);
        menuActions.put(8, this::viewDiscardedMenu);
    }

    private void showMenu() {
        int choice;
        do {
            printMenu();
            choice = scanner.nextInt();
            scanner.nextLine();
            menuActions.getOrDefault(choice, this::invalidChoice).run();
        } while (choice != 0);

        System.out.println("Logging out...");
        logout();
    }

    private void printMenu() {
        System.out.println("Please choose an action:");
        System.out.println("1. View Menu");
        System.out.println("2. View Notifications");
        System.out.println("3. Select Food Items for Tomorrow's Menu");
        System.out.println("4. Submit Feedback");
        System.out.println("5. Submit Feedback For Discard Menu");
        System.out.println("6. Create Profile");
        System.out.println("7. Update Profile");
        System.out.println("8. View Discarded Menu");
        System.out.println("0. Logout");
    }

    private void invalidChoice() {
        System.out.println("Invalid choice. Please try again.");
    }

    private void viewMenu() {
        sendRequest("EMPLOYEE_VIEW_MENU");
        receiveAndPrintResponse("END_OF_MENU");
    }

    private void viewNotifications() {
        sendRequest("EMPLOYEE_VIEW_NOTIFICATION");
        receiveAndPrintResponse("END_OF_NOTIFICATIONS");
    }

    private void selectFoodItemsForTomorrow() {
        sendRequest("EMPLOYEE_VIEW_TOMORROW_FOOD"+ "#" +userId);
        receiveAndPrintResponse("END_OF_MENU");

        Map<String, Long> votingInput = votingForMenu();
        out.println("EMPLOYEE_VOTING_INPUT" +"#"+ votingInput);

        receiveAndPrintResponse("END_OF_VOTING_PROCESS");
    }

    private Map<String, Long> votingForMenu() {
        Map<String, Long> menuVotes = new HashMap<>();
        try {
            System.out.print("Enter your preferred breakfast item Id: ");
            Long breakfast = scanner.nextLong();
            menuVotes.put("Breakfast", breakfast);

            System.out.print("Enter your preferred lunch item: ");
            Long lunch = scanner.nextLong();
            menuVotes.put("Lunch", lunch);

            System.out.print("Enter your preferred dinner item: ");
            Long dinner = scanner.nextLong();
            menuVotes.put("Dinner", dinner);

            return menuVotes;
        } catch (NumberFormatException e) {
            handleInvalidInput("Invalid input. Please enter the correct values.");
        }
        return menuVotes;
    }

    private void submitFeedback() {
        try {
            /*System.out.print("Enter your user ID: ");
            long userId = scanner.nextLong();
            scanner.nextLine();*/

            System.out.print("Enter the food item ID: ");
            long foodItemId = scanner.nextLong();
            scanner.nextLine();

            System.out.print("Enter your rating (1-5): ");
            int rating = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter your comment: ");
            String comment = scanner.nextLine().trim();

            String date = LocalDate.now().toString();

            sendRequest("EMPLOYEE_SUBMIT_FEEDBACK#" + userId + "#" + foodItemId + "#" + rating + "#" + comment + "#" + date);
            receiveAndPrintSingleResponse();
        } catch (NumberFormatException e) {
            handleInvalidInput("Invalid input. Please enter the correct values.");
        }
    }

    private void discardMenuFeedback() {
        try {
            System.out.print("Enter the food name: ");
            String foodName = scanner.nextLine().trim();

            System.out.println("We are trying to improve your experience with " + foodName + ". Please provide your feedback and help us.");

            System.out.print("Q1. What didn’t you like about " + foodName + "? ");
            String dislikeAboutFood = scanner.nextLine().trim();

            System.out.print("Q2. How would you like " + foodName + " to taste? ");
            String likeAboutFood = scanner.nextLine().trim();

            System.out.print("Q3. Share your mom’s recipe: ");
            String momRecipe = scanner.nextLine().trim();

            sendRequest("EMPLOYEE_SUBMIT_DISCARD_FEEDBACK#" + userId + "#" + foodName + "#" + dislikeAboutFood + "#" + likeAboutFood + "#" + momRecipe);
            receiveAndPrintSingleResponse();
        } catch (NumberFormatException e) {
            handleInvalidInput("Invalid input. Please enter the correct values.");
        }
    }

    private void createProfile() {
        try {

            System.out.print("Enter your name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter your dietary preference (Vegetarian/Non Vegetarian/Eggetarian): ");
            String dietaryPreference = scanner.nextLine().trim();

            System.out.print("Enter your spice level preference (High/Medium/Low): ");
            String spiceLevel = scanner.nextLine().trim();

            System.out.print("Enter your cuisine preference (North Indian/South Indian/Other): ");
            String cuisinePreference = scanner.nextLine().trim();

            System.out.print("Do you have a sweet tooth? (Yes/No): ");
            String sweetTooth = scanner.nextLine().trim();

            sendRequest("EMPLOYEE_CREATE_PROFILE#" + userId + "#" + name + "#" + dietaryPreference + "#" + spiceLevel + "#" + cuisinePreference + "#" + sweetTooth);
            receiveAndPrintSingleResponse();
        } catch (NumberFormatException e) {
            handleInvalidInput("Invalid input. Please enter the correct values.");
        }
    }

    private void updateProfile() {
        try {

            System.out.print("Enter your updated name : ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter your Updated dietary preference (Vegetarian/Non Vegetarian/Eggetarian): ");
            String dietaryPreference = scanner.nextLine().trim();

            System.out.print("Enter your updated spice level preference (High/Medium/Low): ");
            String spiceLevel = scanner.nextLine().trim();

            System.out.print("Enter your updated cuisine preference (North Indian/South Indian/Other): ");
            String cuisinePreference = scanner.nextLine().trim();

            System.out.print("Update your sweet tooth preference? (Yes/No, press Enter to skip): ");
            String sweetTooth = scanner.nextLine().trim();

            sendRequest("EMPLOYEE_UPDATE_PROFILE#" + userId + "#" + name + "#" + dietaryPreference + "#" + spiceLevel + "#" + cuisinePreference + "#" + sweetTooth);
            receiveAndPrintSingleResponse();
        } catch (NumberFormatException e) {
            handleInvalidInput("Invalid input. Please enter the correct values.");
        }

    }

    private void viewDiscardedMenu() {
        sendRequest("EMPLOYEE_DISCARD_MENU");
        receiveAndPrintResponse("END_OF_MENU");
    }

    private void logout() {
        sendRequest("LOGOUT");
        receiveAndPrintSingleResponse();
    }

    private void sendRequest(String request) {
        out.println(request);
    }

    private void receiveAndPrintResponse(String endSignal) {
        try {
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
                if (response.equals(endSignal)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveAndPrintSingleResponse() {
        try {
            String response = in.readLine();
            System.out.println("Server reply: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleInvalidInput(String message) {
        System.out.println(message);
    }
}