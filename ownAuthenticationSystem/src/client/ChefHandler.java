package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChefHandler implements RoleHandler {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader userInput;

    public ChefHandler(Socket socket, PrintWriter out, BufferedReader in) {
        this.socket = socket;
        this.out = out;
        this.userInput = in;
    }

    @Override
    public void handle() throws IOException {
        showMenu();
    }

    private void showMenu() throws IOException {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("Please choose an action:");
            System.out.println("1. Roll Out Menu for Next Day");
            System.out.println("2. Finalize Menu for Next Day");
            System.out.println("3. View Menu");
            System.out.println("4. Generate Monthly Report");
            System.out.println("5. View Discard Menu");
            System.out.println("6. View Discard Menu Feedback");
            System.out.println("0. Exit");

            choice = scanner.nextInt();
            scanner.nextLine();
            handleUserChoice(choice, scanner);
        } while (choice != 0);

        System.out.println("Exiting...");
    }

    private void handleUserChoice(int choice, Scanner scanner) throws IOException {
        switch (choice) {
            case 1:
                rollOutMenuForNextDay(scanner);
                break;
            case 2:
                finalizeMenuForNextDay(scanner);
                break;
            case 3:
                viewMenu();
                break;
            case 4:
                generateMonthlyReport();
                break;
            case 5:
                viewDiscardMenu(scanner);
                break;
            case 6:
                getAllFeedbackByFoodName(scanner);
                break;
            case 0:
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    private void finalizeMenuForNextDay(Scanner scanner) {
        System.out.print("Enter the MenuId for the next day (comma separated Id): ");
        String menuIds = scanner.nextLine();
        out.println("CHEF_FINALIZE_MENU" +" "+ menuIds);
        out.flush();

        try {
            String response = userInput.readLine();
            System.out.println("Server reply: " + response);
        } catch (IOException e) {
            System.out.println("Error reading response from server: " + e.getMessage());
        }
    }

    private void rollOutMenuForNextDay(Scanner scanner) {
        out.println("CHEF_ROLL_OUT_MENU");
        out.flush();
        try {
            String response;
            while ((response = userInput.readLine()) != null) {
                System.out.println(response);
                if (response.equals("END_OF_MENU")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateMonthlyReport() {
        System.out.println("Generate Monthly Report functionality not yet implemented.");
    }

    private void viewMenu() {
        out.println("COMMON_VIEW_MENU");
        out.flush();
        try {
            String response;
            while ((response = userInput.readLine()) != null) {
                System.out.println(response);
                if (response.equals("END_OF_MENU")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void viewDiscardMenu(Scanner scanner) throws IOException {
        out.println("CHEF_DISCARD_MENU");
        out.flush();

        String response;
        while ((response = userInput.readLine()) != null) {
            System.out.println(response);
            if (response.equals("END_OF_MENU")) {
                break;
            }
        }

        handleDiscardableFoodOptions(scanner);
    }

    private void handleDiscardableFoodOptions(Scanner scanner) throws IOException {
        System.out.println("Console Options:");
        System.out.println("1. Remove the Food Item from Menu List (Should be done once a month)");
        System.out.println("   Chef will enter the food item name which needs to be removed from Menu");
        System.out.println("2. Get Detailed Feedback (Should be done once a month)");
        System.out.println("   Chef will roll out 3-5 questions to know more about improvements to be done for selected food item.");
        System.out.print("Enter your choice (1 or 2): ");
        String choice = scanner.nextLine();
        out.flush();

        if ("1".equals(choice)) {
            removeFoodItem(scanner);
        } else if ("2".equals(choice)) {
            getDetailedFeedback(scanner);
        } else {
            System.out.println("Invalid option. Please try again.");
        }
    }

    private void removeFoodItem(Scanner scanner) throws IOException {
        out.println("REMOVE_FOOD_ITEM");
        System.out.print("Enter the name of the food item to be removed from the menu: ");
        String foodItemName = scanner.nextLine().trim();
        out.println("foodName " + foodItemName);
        out.flush();

        String serverResponse;
        while (!(serverResponse = userInput.readLine()).equalsIgnoreCase("End of Response")) {
            System.out.println(serverResponse);
        }
    }

    private void getDetailedFeedback(Scanner scanner) throws IOException {
        out.println("GET_DETAIL_FEEDBACK");
        System.out.print("Enter the name of the food item for which you want detailed feedback: ");
        String foodItemName = scanner.nextLine().trim();
        out.println(foodItemName);
        out.flush();

        String serverResponse;
        while (!(serverResponse = userInput.readLine()).equalsIgnoreCase("End of Response")) {
            System.out.println(serverResponse);
        }
    }

    private void getAllFeedbackByFoodName(Scanner scanner) throws IOException {
        System.out.print("Enter the name of the food item to view all feedback: ");
        String foodItemName = scanner.nextLine().trim();
        out.println("CHEF_GET_ALL_FEEDBACK_BY_FOOD_NAME"+ " "+foodItemName);
        out.flush();

        String serverResponse;
        while ((serverResponse = userInput.readLine()) != null) {
            System.out.println(serverResponse);
            if (serverResponse.equalsIgnoreCase("END_OF_FEEDBACK")) {
                break;
            }
        }
    }
}
