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

    public EmployeeHandler(Socket socket, PrintWriter out, BufferedReader in) {
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
        menuActions.put(1, this::viewMenu);
        menuActions.put(2, this::viewNotifications);
        menuActions.put(3, this::selectFoodItemsForTomorrow);
        menuActions.put(4, this::submitFeedback);
        menuActions.put(5, this::discardMenuFeedback);
        menuActions.put(6, this::createProfile);
        menuActions.put(7, this::updateProfile);
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
        System.out.println("0. Logout");
    }

    private void invalidChoice() {
        System.out.println("Invalid choice. Please try again.");
    }

    private void viewMenu() {
        sendRequest("COMMON_VIEW_MENU");
        receiveAndPrintResponse("END_OF_MENU");
    }

    private void viewNotifications() {
        sendRequest("EMPLOYEE_VIEW_NOTIFICATION");
        receiveAndPrintResponse("END_OF_NOTIFICATIONS");
    }

    private void selectFoodItemsForTomorrow() {
        sendRequest("EMPLOYEE_VIEW_TOMORROW_FOOD");
        receiveAndPrintResponse("END_OF_MENU");

        Map<String, Long> votingInput = votingForMenu();
        out.println("EMPLOYEE_VOTING_INPUT#" + votingInput);

        receiveAndPrintResponse("END_OF_VOTING_PROCESS");
    }

    private Map<String, Long> votingForMenu() {
        Map<String, Long> menuVotes = new HashMap<>();

        System.out.print("Enter your preferred breakfast item Id: ");
        menuVotes.put("Breakfast", scanner.nextLong());

        System.out.print("Enter your preferred lunch item: ");
        menuVotes.put("Lunch", scanner.nextLong());

        System.out.print("Enter your preferred dinner item: ");
        menuVotes.put("Dinner", scanner.nextLong());

        return menuVotes;
    }

    private void submitFeedback() {
        long userId = promptForLong("Enter your user ID: ");
        long foodItemId = promptForLong("Enter the food item ID: ");
        int rating = promptForInt("Enter your rating (1-5): ");
        String comment = promptForString("Enter your comment: ");
        String date = LocalDate.now().toString();

        sendRequest("EMPLOYEE_SUBMIT_FEEDBACK#" + userId + "#" + foodItemId + "#" + rating + "#" + comment + "#" + date);
        receiveAndPrintSingleResponse();
    }

    private void discardMenuFeedback() {
        String foodName = promptForString("Enter the food name: ");
        System.out.println("We are trying to improve your experience with " + foodName + ". Please provide your feedback and help us.");
        String dislikeAboutFood = promptForString("Q1. What didn’t you like about " + foodName + "? ");
        String likeAboutFood = promptForString("Q2. How would you like " + foodName + " to taste? ");
        String momRecipe = promptForString("Q3. Share your mom’s recipe: ");
        long userId = promptForLong("Enter your user ID: ");

        sendRequest("EMPLOYEE_SUBMIT_DISCARD_FEEDBACK#" + userId + "#" + foodName + "#" + dislikeAboutFood + "#" + likeAboutFood + "#" + momRecipe);
        receiveAndPrintSingleResponse();
    }

    private void createProfile() {
        long userId = promptForLong("Enter your user ID: ");
        String name = promptForString("Enter your name: ");
        String dietaryPreference = promptForString("Enter your dietary preference (Vegetarian/Non Vegetarian/Eggetarian): ");
        String spiceLevel = promptForString("Enter your spice level preference (High/Medium/Low): ");
        String cuisinePreference = promptForString("Enter your cuisine preference (North Indian/South Indian/Other): ");
        String sweetTooth = promptForString("Do you have a sweet tooth? (Yes/No): ");

        sendRequest("EMPLOYEE_CREATE_PROFILE#" + userId + "#" + name + "#" + dietaryPreference + "#" + spiceLevel + "#" + cuisinePreference + "#" + sweetTooth);
        receiveAndPrintSingleResponse();
    }

    private void updateProfile() {
        long userId = promptForLong("Enter your user ID: ");
        String name = promptForString("Enter your updated name (press Enter to skip): ");
        String dietaryPreference = promptForString("Enter your updated dietary preference (press Enter to skip): ");
        String spiceLevel = promptForString("Enter your updated spice level preference (press Enter to skip): ");
        String cuisinePreference = promptForString("Enter your updated cuisine preference (press Enter to skip): ");
        String sweetTooth = promptForString("Update your sweet tooth preference? (Yes/No, press Enter to skip): ");

        sendRequest("EMPLOYEE_UPDATE_PROFILE#" + userId + "#" + name + "#" + dietaryPreference + "#" + spiceLevel + "#" + cuisinePreference + "#" + sweetTooth);
        receiveAndPrintSingleResponse();
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

    private long promptForLong(String prompt) {
        System.out.print(prompt);
        return scanner.nextLong();
    }

    private int promptForInt(String prompt) {
        System.out.print(prompt);
        return scanner.nextInt();
    }

    private String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
