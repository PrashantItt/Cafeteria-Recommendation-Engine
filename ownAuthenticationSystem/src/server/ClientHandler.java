package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

import model.ChefRecomendationFood;
import model.User;
import model.FoodItem;
import model.Feedback;
import db.UserDAO;
import db.FoodItemDAO;
import db.FeedbackDAO;
import service.UserService;
import db.ChefRecomendationFoodDAO;
import recomendationEngine.RecommendationSystem;
import db.FoodRecommendationDAO;
import model.FoodRecommendation;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final UserService userService;
    private final RecommendationSystem  recommendationSystem;
    private final FoodRecommendationDAO foodRecommendationDAO;

    private final ChefRecomendationFoodDAO chefRecomendationFoodDAO;

    public ClientHandler(Socket clientSocket) throws SQLException {
        this.clientSocket = clientSocket;
        this.recommendationSystem = new RecommendationSystem();
        this.userService = new UserService();
        this.foodRecommendationDAO = new FoodRecommendationDAO();
        this.chefRecomendationFoodDAO = new ChefRecomendationFoodDAO();
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.startsWith("LOGIN ")) {
                    handleLogin(inputLine, out);
                } else if (inputLine.startsWith("ADD_USER ")) {
                    handleAddUser(inputLine, out);
                }  else if (inputLine.startsWith("UPDATE_USER")) {
                    handleUpdateUser(inputLine, out);
                } else if (inputLine.startsWith("DELETE_USER")) {
                    handleDeleteUser(inputLine, out);
                } else if (inputLine.startsWith("ADD_MENU_ITEM")) {
                    handleAddMenuItem(inputLine, out);
                } else if (inputLine.startsWith("UPDATE_MENU_ITEM")) {
                    handleUpdateMenuItem(inputLine, out);
                } else if (inputLine.startsWith("DELETE_MENU_ITEM")) {
                    handleDeleteMenuItem(inputLine, out);
                } else if (inputLine.startsWith("VIEW_MENU")) {
                    handleViewMenu(out);
                } else if (inputLine.startsWith("SUBMIT_FEEDBACK")) {
                    handleSubmitFeedback(inputLine, out);
                } else if (inputLine.startsWith("VIEW_TOMORROW_FOOD")) {
                    handleSubmitFeedback(inputLine, out);
                    handleSubmitFeedback(inputLine, out);
                    handleTommorowMenu(in,out);
                } else if (inputLine.startsWith("ROLL_OUT_MENU")) {
                    handleRoleItemMenu(inputLine,in, out);
                } else if (inputLine.startsWith("FINAL_ITEM")) {
                    handleFinalizeMenu(out);
                }else {
                    out.println("Unknown command");
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Client disconnected");
        }
    }

    private void handleLogin(String inputLine, PrintWriter out) throws SQLException {
        String[] parts = inputLine.split(" ");
        if (parts.length == 3) {
            String username = parts[1];
            String password = parts[2];
            if (userService.validateLogin(username, password)) {
                out.println("LOGIN SUCCESSFUL");
                Long roleId = userService.getRoleId(username, password);
                out.println(roleId);
            } else {
                out.println("LOGIN FAILED");
            }
        } else {
            out.println("Invalid LOGIN command");
        }
    }

    private void handleAddUser(String inputLine, PrintWriter out) throws SQLException {
        String[] parts = inputLine.split(" ");
        if (parts.length == 4) {
            String username = parts[1];
            String password = parts[2];
            Long roleId = Long.valueOf(parts[3]);
            try {
                User user = new User(username, password, roleId);
                UserDAO userDAO = new UserDAO(); // Assuming UserDAO handles database operations
                userDAO.addUser(user);
                out.println("User added successfully");
            } catch (NumberFormatException e) {
                out.println("Invalid role ID format");
            }
        } else {
            out.println("Invalid ADD_USER command");
        }
    }

    private void handleUpdateUser(String inputLine, PrintWriter out) {
        String[] parts = inputLine.split(" ");
        if (parts.length == 4) {
            String username = parts[1];
            String newPassword = parts[2];
            try {
                Long newRoleId = Long.valueOf(parts[3]);
                User user = new User(username, newPassword, newRoleId);
                UserDAO userDAO = new UserDAO();
                userDAO.updateUser(user);
                out.println("User updated successfully");
            } catch (NumberFormatException e) {
                out.println("Invalid role ID format");
            }
        } else {
            out.println("Invalid UPDATE_USER command");
        }
    }

    private void handleDeleteUser(String inputLine, PrintWriter out) {
        String[] parts = inputLine.split(" ");
        if (parts.length == 2) {
            String username = parts[1];
            UserDAO userDAO = new UserDAO();
            userDAO.deleteUser(username);
            out.println("User deleted successfully");
        } else {
            out.println("Invalid DELETE_USER command");
        }
    }
    private void handleAddMenuItem(String inputLine, PrintWriter out) {
        String[] parts = inputLine.split(" ");
        if (parts.length == 5) {
            String itemName = parts[1];
            try {
                double price = Double.parseDouble(parts[2]);
                boolean availabilityStatus = Boolean.parseBoolean(parts[3]);
                long foodItemTypeId = Long.parseLong(parts[4]);

                FoodItem foodItem = new FoodItem(itemName, price, availabilityStatus, foodItemTypeId);
                FoodItemDAO foodItemDAO = new FoodItemDAO();
                foodItemDAO.addFoodItem(foodItem);
                out.println("Menu item added successfully");
            } catch (NumberFormatException e) {
                out.println("Error adding menu item: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            out.println("Invalid ADD_MENU_ITEM command");
        }
    }


    private void handleUpdateMenuItem(String inputLine, PrintWriter out) {
        String[] parts = inputLine.split(" ");
        if (parts.length == 5) {
            try {
                String itemName = parts[1];
                double price = Double.parseDouble(parts[2]);
                boolean availabilityStatus = Boolean.parseBoolean(parts[3]);
                long foodItemTypeId = Long.parseLong(parts[4]);

                FoodItem foodItem = new FoodItem(itemName, price, availabilityStatus, foodItemTypeId);
                FoodItemDAO foodItemDAO = new FoodItemDAO();
                foodItemDAO.updateFoodItem(foodItem);
                out.println("Menu item updated successfully");
            } catch (NumberFormatException e) {
                out.println("Error updating menu item: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            out.println("Invalid UPDATE_MENU_ITEM command");
        }
    }

    private void handleDeleteMenuItem(String inputLine, PrintWriter out) {
        String[] parts = inputLine.split(" ");
        if (parts.length == 2) {
            try {
                long foodItemId = Long.parseLong(parts[1]);
                FoodItemDAO foodItemDAO = new FoodItemDAO();
                foodItemDAO.deleteFoodItem(foodItemId);
                out.println("Menu item deleted successfully");
            } catch (NumberFormatException e) {
                out.println("Error deleting menu item: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            out.println("Invalid DELETE_MENU_ITEM command");
        }
    }

    private void handleViewMenu(PrintWriter out) {
        FoodItemDAO foodItemDAO = new FoodItemDAO();
        List<FoodItem> menu = foodItemDAO.getAllFoodItems();
        for (FoodItem item : menu) {
            out.println(item.toString());
        }
        out.println("END_OF_MENU");
    }

    /*private void handleTommorowMenu(PrintWriter out) {
        ChefRecomendationFoodDAO chefRecomendationFoodDAO = new ChefRecomendationFoodDAO();
        List<ChefRecomendationFood> menu = chefRecomendationFoodDAO.getAll();
        for (ChefRecomendationFood item : menu) {
            out.println(item.toString());
        }
        out.println("END_OF_MENU");
    }*/

    private void handleTommorowMenu(BufferedReader in, PrintWriter out) {
        ChefRecomendationFoodDAO chefRecomendationFoodDAO = new ChefRecomendationFoodDAO();
        List<ChefRecomendationFood> menu = chefRecomendationFoodDAO.getAll();

        // Print menu items to client
        for (ChefRecomendationFood item : menu) {
            out.println(item.toString());
        }
        out.println("END_OF_MENU");

        try {
            // Read client's vote input
            String voteInput = in.readLine();
            long foodItemId = Long.parseLong(voteInput.trim()); // Assuming the client sends foodItemId to vote for

            // Insert the vote into the database
            boolean voteInserted = chefRecomendationFoodDAO.insertVote(foodItemId);
            if (voteInserted) {
                out.println("Vote successfully recorded.");
            } else {
                out.println("Failed to record vote.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleSubmitFeedback(String inputLine, PrintWriter out) throws SQLException {
        String[] parts = inputLine.split(" ");
        if (parts.length == 6) {
            try {
                long userId = Long.parseLong(parts[1]);
                long menuItemId = Long.parseLong(parts[2]);
                int rating = Integer.parseInt(parts[3]);
                String comment = parts[4];
                String date = parts[5];

                // Create Feedback object
                Feedback feedback = new Feedback(menuItemId, userId, comment, rating, date);

                // Add feedback to database using FeedbackDAO
                FeedbackDAO feedbackDAO = new FeedbackDAO();
                boolean feedbackAdded = feedbackDAO.addFeedback(feedback);

                if (feedbackAdded) {
                    out.println("FEEDBACK_RECEIVED");
                } else {
                    out.println("ERROR_FEEDBACK_NOT_ADDED");
                }
            } catch (NumberFormatException e) {
                out.println("Invalid feedback data format");
                e.printStackTrace();
            }
        } else {
            out.println("Invalid SUBMIT_FEEDBACK command");
        }
    }

    private void handleRoleItemMenu(String inputLine,BufferedReader in ,PrintWriter out) {
        recommendationSystem.calculateSentimentScores();
        List<FoodRecommendation> foodRecommendationList =  foodRecommendationDAO.getTodayFoodRecommendations();
        for (FoodRecommendation item : foodRecommendationList) {
            out.println(item.toString());
        }

        processUserInputs(in,out);
        out.println("END_OF_MENU");
    }
    private void processUserInputs(BufferedReader in ,PrintWriter out) {
        try {
            out.println("SEND_INPUTS");
            String input;
            while ((input = in.readLine()) != null) {
                // Process each input, expecting a format like "foodItemId:1,foodItemTypeId:2"
                String[] parts = input.split(",");
                Long foodItemId = Long.parseLong(parts[0].split(":")[1]);
                Long foodItemTypeId = Long.parseLong(parts[1].split(":")[1]);

                ChefRecomendationFoodDAO chefRecomendationFoodDAO = new ChefRecomendationFoodDAO();
                ChefRecomendationFood chefRecomendationFood = new ChefRecomendationFood(foodItemId,foodItemTypeId);
                chefRecomendationFoodDAO.insert(chefRecomendationFood);

                // Break the loop if a certain condition is met (e.g., end of input signal)
                if (input.equals("END_OF_INPUTS")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleFinalizeMenu(PrintWriter out) {
        List<ChefRecomendationFood> chefRecomendationFoodList = chefRecomendationFoodDAO.getTop3FoodItemsForToday();

        for (ChefRecomendationFood foodItem : chefRecomendationFoodList) {
            out.println("Food Item ID: " + foodItem.getFoodItemId() +
                    ", Food Type ID: " + foodItem.getFoodtypeId());
        }
    }


}
