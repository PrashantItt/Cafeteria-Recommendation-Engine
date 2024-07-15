package service;

import db.*;
import model.ChefRecomendationFood;
import model.DiscardFoodFeedback;
import model.FoodItem;
import model.FoodItemType;
import recomendationEngine.RecommendationSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class ChefService {

    private ChefRecomendationFoodDAO chefRecomendationFoodDAO;

    public ChefService() {
        this.chefRecomendationFoodDAO = new ChefRecomendationFoodDAO();
    }

    public String handleFinalizeCreationMenu(String inputLine, PrintWriter out) {
        if (chefRecomendationFoodDAO.entryExistsForToday()) {
            return "An entry for today already exists. Only one entry per day is allowed.";
        }

        String[] menuIds = inputLine.split("#");
        if (menuIds.length < 2) {
            return "Invalid input format.";
        }

        String[] foodIds = menuIds[1].split(",");
        if (foodIds.length < 3) {
            return "Please enter a minimum of three items for the Chef's Recommendation.";
        }

        boolean allFoodsAdded = addFoodsToRecommendation(foodIds, out);

        return allFoodsAdded ? "Chef Recommendation Food Added Successfully" : "Failed to add some or all Chef Recommendation Food";
    }

    private boolean addFoodsToRecommendation(String[] foodIds, PrintWriter out) {
        boolean allFoodsAdded = true;

        for (String foodIdStr : foodIds) {
            String trimmedFoodId = foodIdStr.trim();
            try {
                Long foodItemId = Long.parseLong(trimmedFoodId);
                ChefRecomendationFood chefRecomendationFood = new ChefRecomendationFood(foodItemId);
                boolean foodAdded = chefRecomendationFoodDAO.insert(chefRecomendationFood);
                if (!foodAdded) {
                    allFoodsAdded = false;
                }
            } catch (NumberFormatException e) {
                out.println("Invalid food ID format: " + trimmedFoodId);
                allFoodsAdded = false;
            }
        }

        return allFoodsAdded;
    }



    public void handleRoleItemMenu(String arguments, PrintWriter out) {
        String[] splitString = arguments.split("#");
        RecommendationSystem recommendationSystem = new RecommendationSystem();
        Map<String, List<Map<String, Object>>> top3FoodItemsByCategory = recommendationSystem.getTopFoodItemsByCategory(splitString[1]);
        for (Map.Entry<String, List<Map<String, Object>>> categoryEntry : top3FoodItemsByCategory.entrySet()) {
            String category = categoryEntry.getKey();
            List<Map<String, Object>> top3Items = categoryEntry.getValue();

            out.println("Category: " + category);
            out.printf("%-15s %-30s %-20s %-20s %-10s %-50s\n", "MenuItemId", "MenuItemName", "Composite Score", "Average Rating", "Price", "Sentiment Comment");
            out.println("----------------------------------------------------------------------------------------------------------------------------");

            for (Map<String, Object> item : top3Items) {
                out.printf("%-15d %-30s %-20.2f %-20.2f %-10.2f %-50s\n",
                        (Long) item.get("Id"),
                        (String) item.get("Name"),
                        (Double) item.get("CompositeScore"),
                        (Double) item.get("AverageRating"),
                        (Double) item.get("Price"),
                        (String) item.get("SentimentComment"));
            }
            out.println();
        }

        out.println("END_OF_MENU");
    }

    public void discardItemList(PrintWriter out, BufferedReader in) throws IOException {
        RecommendationSystem recommendationSystem = new RecommendationSystem();
        List<Map<String, Object>> lowRatedFoodItems = recommendationSystem.getLowRatedFoodItems();

        out.println("Low Rated Food Items:");
        out.printf("%-15s %-30s %-20s %-10s %-50s\n", "MenuItemId", "MenuItemName", "Average Rating", "Price", "Sentiment Comment");
        out.println("----------------------------------------------------------------------------------------------------------------------------");

        for (Map<String, Object> item : lowRatedFoodItems) {
            out.printf("%-15d %-30s %-20.2f %-10.2f %-50s\n",
                    (Long) item.get("Id"),
                    (String) item.get("Name"),
                    (Double) item.get("AverageRating"),
                    (Double) item.get("Price"),
                    (String) item.get("SentimentComment"));
        }

        out.println();
        out.println("END_OF_MENU");
        handleDiscardMenuRouting(in, out);
    }

    private void handleDiscardMenuRouting(BufferedReader in, PrintWriter out) throws IOException {
        String clientRequest;

        while ((clientRequest = in.readLine()) != null) {
            if (clientRequest.equals("REMOVE_FOOD_ITEM")) {
                handleRemoveFoodItem(in, out);
            } else if (clientRequest.equals("GET_DETAIL_FEEDBACK")) {
                boolean result = handleGetDetailedFeedback(in, out);
                out.println(result ? "Feedback recorded successfully." : "Failed to record feedback.");
                out.println("End of Response");
            } else {
                out.println("Invalid input. Please try again.");
                out.println("End of Response");
                out.flush();
            }
        }
    }

    private boolean handleGetDetailedFeedback(BufferedReader in, PrintWriter out) throws IOException {
        String response = in.readLine();
        String[] foodItemName = response.split("#");
        if (foodItemName.length == 1) {
            try {
                String foodName = foodItemName[0];
                System.out.println("foodName"+foodName);
                DiscardFoodItemDAO discardFoodItemDAO = new DiscardFoodItemDAO();
                return discardFoodItemDAO.addDiscardFoodItemByName(foodName);
            } catch (NumberFormatException e) {
                out.println("Error adding menu item");
            }
        }
        return false;
    }

    private void handleRemoveFoodItem(BufferedReader in, PrintWriter out) throws IOException {
        String response = in.readLine();
        String[] foodItemDetails = response.split("#");
        if (foodItemDetails.length < 2) {
            out.println("Invalid input format. Please provide food item name.");
            out.println("End of Response");
            out.flush();
            return;
        }

        String foodItemName = foodItemDetails[1];
        FoodItemDAO foodItemDAO = new FoodItemDAO();
        boolean removed = foodItemDAO.deleteFoodItemByName(foodItemName);

        if (removed) {
            out.println("Food item '" + foodItemName + "' successfully removed.");
        } else {
            out.println("Failed to remove food item '" + foodItemName + "'. Please try again.");
        }

        out.println("End of Response");
        out.flush();
    }
    public String handleFinalizeMenu() {
        List<ChefRecomendationFood> chefRecomendationFoodList = chefRecomendationFoodDAO.getTopFoodItemsForToday();
        FoodItemTypeDAO foodItemTypeDAO = new FoodItemTypeDAO();
        FoodItemDAO foodItemDAO = new FoodItemDAO();

        StringBuilder menuString = new StringBuilder();
        menuString.append(String.format("%-30s %-20s\n", "Food Item Name", "Food Type Name"));

        for (ChefRecomendationFood foodItem : chefRecomendationFoodList) {
            FoodItemType foodItemType = foodItemTypeDAO.getFoodItemTypeById(foodItem.getFoodtypeId());
            String foodTypeName = (foodItemType != null) ? foodItemType.getFoodItemType() : "Unknown";
            FoodItem food = foodItemDAO.getFoodItemById(foodItem.getFoodItemId());
            String foodName = (food != null) ? food.getItemName() : "Unknown";

            menuString.append(String.format("%-30s %-20s\n", foodName, foodTypeName));
        }

        menuString.append("END_OF_MENU");
        return menuString.toString();
    }





    public void handleDisplayDiscardMenuList(String inputLine, PrintWriter out) {
        String[] parts = inputLine.split("#");
        if (parts.length < 2) {
            out.println("Invalid input format. Please provide food name.");
            out.println("END_OF_MENU");
            return;
        }

        String foodName = parts[1];
        System.out.println(parts[0]);
        DiscardFoodFeedbackDAO discardFoodFeedbackDAO = new DiscardFoodFeedbackDAO();
        List<DiscardFoodFeedback> discardFoodItemList = discardFoodFeedbackDAO.getFeedbacksByFoodName(foodName);

        out.printf("%-10s %-15s %-15s %-15s %-20s %-20s %-20s\n",
                "Id", "FoodName","UserId", "Question1", "Question2", "Question3", "DiscardDate");
        out.println("----------------------------------------------------------------------------------------------------------------------");

        for (DiscardFoodFeedback item : discardFoodItemList) {
            out.printf("%-10d %-15s %-15d %-15s %-20s %-20s %-20s\n",
                    item.getId(),
                    item.getFoodName(),
                    item.getUserID(),
                    item.getQuestion1(),
                    item.getQuestion2(),
                    item.getQuestion3(),
                    item.getFeedbackDate().toString());
        }

        out.println("END_OF_FEEDBACK");
    }
}
