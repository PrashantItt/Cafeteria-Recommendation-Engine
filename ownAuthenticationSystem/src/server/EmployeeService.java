package server;

import db.*;
import model.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeService {

    public String handleSubmitFeedback(String inputLine) {
        String[] parts = inputLine.split("#");
        System.out.println("parts" +parts);
        if (parts.length == 6) {
            try {
                long userId = Long.parseLong(parts[1]);
                long menuItemId = Long.parseLong(parts[2]);
                int rating = Integer.parseInt(parts[3]);
                String comment = parts[4];
                System.out.println("comment" +comment);
                String date = parts[5];

                Feedback feedback = new Feedback(menuItemId, userId, comment, rating, date);

                FeedbackDAO feedbackDAO = new FeedbackDAO();
                boolean feedbackAdded = feedbackDAO.addFeedback(feedback);

                if (feedbackAdded) {
                    return "FEEDBACK_RECEIVED";
                } else {
                    return "ERROR_FEEDBACK_NOT_ADDED";
                }
            } catch (NumberFormatException e) {
                return "Error processing feedback: " + e.getMessage();
            }
        } else {
            return "Invalid SUBMIT_FEEDBACK command";
        }
    }

    public String handleDiscardMenuFeedback (String response) {
        String[] parts = response.split("#");
        if (parts.length == 6) {
            try {
                long userId = Long.parseLong(parts[1]);
                String  foodName = parts[2];
                String dislikeAboutFood  = parts[3];
                String likeAboutFood = parts[4];
                String momRecipe = parts[5];

                DiscardFoodFeedback discardFoodFeedback = new DiscardFoodFeedback(foodName, (int) userId, likeAboutFood, dislikeAboutFood, momRecipe);

                DiscardFoodFeedbackDAO discardFoodFeedbackDAO = new DiscardFoodFeedbackDAO();
                boolean feedbackAdded = discardFoodFeedbackDAO.insert(discardFoodFeedback);

                if (feedbackAdded) {
                    return "FEEDBACK_RECEIVED";
                } else {
                    return "ERROR_FEEDBACK_NOT_ADDED";
                }
            } catch (NumberFormatException e) {
                return "Error processing feedback: " + e.getMessage();
            }
        } else {
            return "Invalid SUBMIT_FEEDBACK command";
        }


    }

    public String handleViewRecommendedFood(String request) {
        ChefRecomendationFoodDAO chefRecomendationFoodDAO = new ChefRecomendationFoodDAO();
        FoodItemTypeDAO foodItemTypeDAO = new FoodItemTypeDAO();

        List<FoodItem> recommendedFoodItems = chefRecomendationFoodDAO.getTommorowMenu();
        List<FoodItemType> foodItemTypes = foodItemTypeDAO.getAllFoodItemTypes();
        Map<Long, FoodItemType> foodItemTypeMap = new HashMap<>();
        for (FoodItemType type : foodItemTypes) {
            foodItemTypeMap.put(type.getFoodItemTypeId(), type);
        }

        StringBuilder menuString = new StringBuilder();
        menuString.append(String.format("%-10s %-20s %-10s %-10s %-20s %-15s %-30s\n",
                "ID", "Name", "Price", "Available", "Type", "Avg Rating", "Sentiment Comment"));

        for (FoodItem item : recommendedFoodItems) {
            FoodItemType foodItemType = foodItemTypeMap.get(item.getFoodItemTypeId());
            menuString.append(String.format("%-10d %-20s %-10.2f %-10s %-20s %-15d %-30s\n",
                    item.getFoodItemId(),
                    item.getItemName(),
                    item.getPrice(),
                    item.isAvailabilityStatus() ? "Yes" : "No",
                    foodItemType != null ? foodItemType.getFoodItemType() : "N/A",
                    item.getAvgRating(),
                    item.getSentimentComment()));
        }

        return menuString.toString();
    }

    public void handleEmployeeVoting(String response) {
        String[] parts = response.split("#", 2);
        if (parts.length == 2 && parts[0].equals("EMPLOYEE_VOTING_INPUT")) {
            String votingData = parts[1];
            Map<String, Long> votingMap = parseVotingData(votingData);

            processVotes(votingMap);
        } else {
            System.out.println("Invalid input or no voting data received.");
        }
    }

    private Map<String, Long> parseVotingData(String data) {
        Map<String, Long> votingMap = new HashMap<>();
        data = data.replaceAll("[{}]", ""); // Remove curly braces

        String[] entries = data.split(", ");
        for (String entry : entries) {
            String[] keyValue = entry.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                Long value = Long.parseLong(keyValue[1].trim());
                votingMap.put(key, value);
            }
        }

        return votingMap;
    }

    private void processVotes(Map<String, Long> votingMap) {
        System.out.println("Processing Votes:");
        for (Map.Entry<String, Long> entry : votingMap.entrySet()) {
            System.out.println("Meal: " + entry.getKey() + ", Item ID: " + entry.getValue());

            try {
                FoodItemTypeDAO foodItemTypeDAO = new FoodItemTypeDAO();
                FoodItemType foodItemType = foodItemTypeDAO.getFoodItemTypeByName(entry.getKey());

                FoodItemDAO foodItemDAO = new FoodItemDAO();
                FoodItem foodItem = foodItemDAO.getFoodItemById(entry.getValue());

                if (foodItemType.getFoodItemTypeId() == foodItem.getFoodItemTypeId()) {
                    ChefRecomendationFoodDAO chefRecomendationFoodDAO = new ChefRecomendationFoodDAO();
                    boolean response = chefRecomendationFoodDAO.insertVote(entry.getValue());

                    if (!response) {
                        System.err.println("Failed to insert vote for Item ID: " + entry.getValue());
                        // Optionally return here if you want to stop further processing on failure
                        // return;
                    }
                } else {
                    System.err.println("Mismatch between food item type IDs for Meal: " + entry.getKey() + ", Item ID: " + entry.getValue());
                }
            } catch (Exception e) {
                System.err.println("An error occurred while processing vote for Meal: " + entry.getKey() + ", Item ID: " + entry.getValue());
                e.printStackTrace();
            }
        }
    }

}
