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
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private long UserId;

    public EmployeeHandler(Socket socket, PrintWriter out, BufferedReader in) {
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
            System.out.println("1. View Menu");
            System.out.println("2. View Notifications");
            System.out.println("3. Select Food Items for Tomorrow's Menu");
            System.out.println("4. Submit Feedback");
            System.out.println("5. Submit Feedbak For Discard Menu");
            System.out.println("6. Create Profile");
            System.out.println("7. Update Profile");
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
                viewMenu();
                break;
            case 2:
                viewNotifications();
                break;
            case 3:
                selectFoodItemsForTomorrow(scanner);
                break;
            case 4:
                submitFeedback(scanner);
                break;
            case 5:
                discardMenuFeedback(scanner);
            case 6:
                createProfile(scanner);
                break;
            case 7:
                updateProfile(scanner);
                break;
            case 0:
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    private void viewMenu() {
        out.println("COMMON_VIEW_MENU");
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

    private void viewNotifications() {
        out.println("EMPLOYEE_VIEW_NOTIFICATION");
        try {
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
                if (response.equals("END")) {
                    break;
                }
            }
    }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void selectFoodItemsForTomorrow(Scanner scanner) {
        out.println("EMPLOYEE_VIEW_TOMORROW_FOOD");
        try {
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
                if (response.equals("END_OF_MENU")) {
                    break;
                }
            }
            Map<String, Long> votingInput = VotingForMenu(scanner);
            out.println("EMPLOYEE_VOTING_INPUT" +"#" + votingInput);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Long> VotingForMenu(Scanner scanner) {
        Map<String, Long> menuVotes = new HashMap<>();

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
    }



    private void submitFeedback(Scanner scanner) {

            System.out.print("Enter your user ID: ");
            long userId = scanner.nextLong();
            scanner.nextLine();

            System.out.print("Enter the food item ID: ");
            long foodItemId = scanner.nextLong();
            scanner.nextLine();

            System.out.print("Enter your rating (1-5): ");
            int rating = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter your comment: ");
            String comment = scanner.nextLine().trim();
            System.out.println("Comment" +comment);

            String date = LocalDate.now().toString();

            out.println( "EMPLOYEE_SUBMIT_FEEDBACK "+ "#" + userId + "#" + foodItemId + "#" + rating + "#" + comment + "#" + date);
        try {
            String response = in.readLine();
            System.out.println("Server reply: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void discardMenuFeedback(Scanner scanner) {
        System.out.print("Enter the food name: ");
        String foodName = scanner.nextLine().trim();

        System.out.println("We are trying to improve your experience with " + foodName + ". Please provide your feedback and help us.");

        System.out.print("Q1. What didn’t you like about " + foodName + "? ");
        String dislikeAboutFood = scanner.nextLine().trim();

        System.out.print("Q2. How would you like " + foodName + " to taste? ");
        String likeAboutFood = scanner.nextLine().trim();

        System.out.print("Q3. Share your mom’s recipe: ");
        String momRecipe = scanner.nextLine().trim();

        System.out.print("Enter your user ID: ");
        long userId = scanner.nextLong();
        scanner.nextLine();


        out.println("EMPLOYEE_SUBMIT_DISCARD_FEEDBACK" + "#" +userId+"#" + foodName +"#" + dislikeAboutFood + "#" +likeAboutFood +"#"+ momRecipe);

        try {
            String response = in.readLine();
            System.out.println("Server reply: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void createProfile(Scanner scanner) {
        System.out.print("Enter your user ID: ");
        long userId = scanner.nextLong();
        scanner.nextLine();

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

        out.println("EMPLOYEE_CREATE_PROFILE" + "#" + userId + "#" + name + "#" + dietaryPreference + "#" + spiceLevel + "#" + cuisinePreference + "#" + sweetTooth);

        try {
            String response = in.readLine();
            System.out.println("Server reply: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateProfile(Scanner scanner) {
        System.out.print("Enter your user ID: ");
        long userId = scanner.nextLong();
        scanner.nextLine();

        System.out.print("Enter your updated name (press Enter to skip): ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter your updated dietary preference (press Enter to skip): ");
        String dietaryPreference = scanner.nextLine().trim();

        System.out.print("Enter your updated spice level preference (press Enter to skip): ");
        String spiceLevel = scanner.nextLine().trim();

        System.out.print("Enter your updated cuisine preference (press Enter to skip): ");
        String cuisinePreference = scanner.nextLine().trim();

        System.out.print("Update your sweet tooth preference? (Yes/No, press Enter to skip): ");
        String sweetTooth = scanner.nextLine().trim();

        out.println("EMPLOYEE_UPDATE_PROFILE" + "#" + userId + "#" + name + "#" + dietaryPreference + "#" + spiceLevel + "#" + cuisinePreference + "#" + sweetTooth);

        try {
            String response = in.readLine();
            System.out.println("Server reply: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
