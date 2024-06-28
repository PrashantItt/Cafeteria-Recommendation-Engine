package server;

import db.ChefRecomendationFoodDAO;
import db.DiscardFoodFeedbackDAO;
import db.DiscardFoodItemDAO;
import model.ChefRecomendationFood;
import model.DiscardFoodFeedback;
import model.DiscardFoodItem;
import recomendationEngine.RecommendationSystem;
import db.FoodItemDAO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ChefService {

    private ChefRecomendationFoodDAO chefRecomendationFoodDAO;

    public ChefService() {
        this.chefRecomendationFoodDAO = new ChefRecomendationFoodDAO();
    }

    public String handleFinalizeCreationMenu(String inputLine, PrintWriter out) {
        String[] menuIds = inputLine.split(" ");
        String[] parts = menuIds[1].split(",");
        boolean allFoodsAdded = true;

        for (int i = 0; i < parts.length; i++) {
            String foodIdStr = parts[i].trim();
            try {
                Long foodItemId = Long.parseLong(foodIdStr);
                ChefRecomendationFood chefRecomendationFood = new ChefRecomendationFood(foodItemId);
                boolean foodAdded = chefRecomendationFoodDAO.insert(chefRecomendationFood);
                if (!foodAdded) {
                    allFoodsAdded = false;
                }
            } catch (NumberFormatException e) {
                out.println("Invalid food ID format: " + foodIdStr);
                allFoodsAdded = false;
            }
        }

        if (allFoodsAdded) {
            return "Chef Recommendation Food Added Successfully";
        } else {
            return "Failed to add some or all Chef Recommendation Food";
        }
    }


    public void handleRoleItemMenu(String arguments, PrintWriter out) throws SQLException {
        RecommendationSystem recommendationSystem = new RecommendationSystem();
        Map<String, List<Map<String, Object>>> top3FoodItemsByCategory = recommendationSystem.getTop3FoodItemsByCategory();
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

    public void discardItemList(PrintWriter out, BufferedReader in) throws SQLException, IOException {
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
        String[] foodItemName = response.split(" ");
        if (foodItemName.length == 1) {
            try {
                String foodName = foodItemName[0];
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
        String[] foodItemDetails = response.split(" ");
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

    public void handleFinalizeMenu(PrintWriter out) {
        List<ChefRecomendationFood> chefRecomendationFoodList = chefRecomendationFoodDAO.getTop3FoodItemsForToday();

        for (ChefRecomendationFood foodItem : chefRecomendationFoodList) {
            out.println("Food Item ID: " + foodItem.getFoodItemId());
        }

        out.println("END_OF_MENU");
    }

    public void handleDisplayDiscardMenuList(String inputLine, PrintWriter out) {
        String[] parts = inputLine.split(" ");
        System.out.println(parts[0]);
        DiscardFoodFeedbackDAO discardFoodFeedbackDAO = new DiscardFoodFeedbackDAO();
        List<DiscardFoodFeedback> discardFoodItemList = discardFoodFeedbackDAO.getFeedbacksByFoodName(parts[1]);

        out.printf("%-10s %-15s %-15s %-15s %-30s %20s\n",
                "Id", "FoodName","UserId", "Like", "Dislike", "MomRecipe", "DiscardDate");
        out.println("-------------------------------------------------------------------");

        for (DiscardFoodFeedback item : discardFoodItemList) {
            out.printf("%-10d %-15d %-15s %-15s %-30s %20s\n",
                    item.getId(),
                    item.getFoodName(),
                    item.getUserID(),
                    item.getQuestion1(),
                    item.getQuestion2(),
                    item.getQuestion3(),
                    item.getFeedbackDate());
        }

        out.println("END_OF_MENU");
    }
}
