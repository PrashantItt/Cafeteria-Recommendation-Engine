package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class ChefHandler implements RoleHandler {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ChefHandler(Socket socket, PrintWriter out, BufferedReader in) {
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
            System.out.println("1. Finalize Menu for Next Day");
            System.out.println("2. Roll Out Menu for Next Day");
            System.out.println("3. Update Availability of Menu Item");
            System.out.println("4. Generate Monthly Report");
            System.out.println("5. View Menu");
            System.out.println("6. View Daily Review Comments");
            System.out.println("7. Send Final Notification Prior to Preparing Food");
            System.out.println("8. Send Final Notification to Employee");
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
                finalizeMenuForNextDay();
                break;
            case 2:
                rollOutMenuForNextDay(scanner);
                break;
            case 3:
                updateAvailabilityOfMenuItem(scanner);
                break;
            case 4:
                generateMonthlyReport();
                break;
            case 5:
                viewMenu();
                break;
            case 6:
                viewDailyReviewComments();
                break;
            case 7:
                sendFinalNotificationPriorToPreparingFood();
                break;
            case 8:
                sendFinalNotificationToEmployee();
                break;
            case 0:
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    private void finalizeMenuForNextDay() {
        out.println("FINAL_ITEM");
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

    private void rollOutMenuForNextDay(Scanner scanner) {
        out.println("ROLL_OUT_MENU");
        try {
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
                if (response.equals("END_OF_MENU")) {
                    break;
                }
            }
            String inputLine;
            inputLine = in.readLine();
            if (inputLine.startsWith("SEND_INPUTS")) {
               System.out.println("expecting a format like \"foodItemId:1,foodItemTypeId:2");
                String inputItems = scanner.nextLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateAvailabilityOfMenuItem(Scanner scanner) {

        System.out.println("Update Availability of Menu Item functionality not yet implemented.");
    }

    private void generateMonthlyReport() {

        System.out.println("Generate Monthly Report functionality not yet implemented.");
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

        System.out.println("View Daily Review Comments functionality not yet implemented.");
    }

    private void sendFinalNotificationPriorToPreparingFood() {

        System.out.println("Send Final Notification Prior to Preparing Food functionality not yet implemented.");
    }

    private void sendFinalNotificationToEmployee() {

        System.out.println("Send Final Notification to Employee functionality not yet implemented.");
    }
}

