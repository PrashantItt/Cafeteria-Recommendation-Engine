package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Scanner;

public class EmployeeHandler implements RoleHandler {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

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
            System.out.println("0. Exit");

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
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
            case 0:
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
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

    private void viewNotifications() {
        // Implement view notifications functionality
        System.out.println("View Notifications functionality not yet implemented.");
    }

    private void selectFoodItemsForTomorrow(Scanner scanner) {
        out.println("VIEW_TOMORROW_FOOD");
        try {
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
                if (response.equals("END_OF_MENU")) {
                    break;
                }
            }
            System.out.println("Enter the food item ID to vote for (or 0 to exit):");
            long foodItemId = scanner.nextLong();
            if (foodItemId != 0) {
                out.println(foodItemId);

                String voteResponse = in.readLine();
                System.out.println(voteResponse);
            } else {
                System.out.println("Exiting without voting.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

            String date = LocalDate.now().toString();

            out.println( "SUBMIT_FEEDBACK " + userId + " " + foodItemId + " " + rating + " " + comment + " " + date);
        try {
            String response = in.readLine();
            System.out.println("Server reply: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



