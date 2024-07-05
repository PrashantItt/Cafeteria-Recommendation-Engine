package recomendationEngine;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import db.FeedbackDAO;
import db.FoodItemDAO;
import db.FoodItemTypeDAO;
import model.Feedback;
import model.FoodItem;
import model.FoodItemType;

public class RecommendationSystem {
    private FeedbackDAO feedbackDAO;
    private SentimentAnalysisService sentimentService;
    private FoodItemDAO foodItemDAO;
    private FoodItemTypeDAO foodItemTypeDAO;

    public RecommendationSystem() throws SQLException {
        this.feedbackDAO = new FeedbackDAO();
        this.sentimentService = new SentimentAnalysisService();
        this.foodItemDAO = new FoodItemDAO();
        this.foodItemTypeDAO = new FoodItemTypeDAO();
    }

    public Map<String, List<Map<String, Object>>> getTopFoodItemsByCategory(String numberOfItems) {
        Map<Long, Map<String, Object>> menuItems = calculateSentimentScores();
        return extractTopFoodItemsByCategory(menuItems,numberOfItems);
    }

    public List<Map<String, Object>> getLowRatedFoodItems() {
        Map<Long, Map<String, Object>> menuItems = calculateSentimentScores();
        return extractLowRatedFoodItems(menuItems);
    }

    private Map<Long, Map<String, Object>> calculateSentimentScores() {
        List<Feedback> feedbackList = feedbackDAO.getAllFeedback();
        Map<Long, List<Feedback>> menuItemFeedbackMap = groupFeedbackByMenuItem(feedbackList);
        Map<Long, Map<String, Object>> menuItems = new HashMap<>();

        for (Map.Entry<Long, List<Feedback>> entry : menuItemFeedbackMap.entrySet()) {
            Long menuItemId = entry.getKey();
            List<Feedback> feedbacks = entry.getValue();

            Map<String, Object> menuItemDetails = processFeedbacksAndGetDetails(feedbacks);
            menuItems.put(menuItemId, menuItemDetails);

            storeRatingAndCommentInMenu(menuItemId, (double) menuItemDetails.get("AverageRating"), (String) menuItemDetails.get("CombinedComments"));
        }

        return menuItems;
    }

    private Map<Long, List<Feedback>> groupFeedbackByMenuItem(List<Feedback> feedbackList) {
        Map<Long, List<Feedback>> menuItemFeedbackMap = new HashMap<>();
        for (Feedback feedback : feedbackList) {
            Long menuItemId = feedback.getMenuItemId();
            menuItemFeedbackMap.computeIfAbsent(menuItemId, k -> new ArrayList<>()).add(feedback);
        }
        return menuItemFeedbackMap;
    }

    private Map<String, Object> processFeedbacksAndGetDetails(List<Feedback> feedbacks) {
        StringBuilder combinedComments = new StringBuilder();
        double totalRating = 0;
        int count = feedbacks.size();

        for (Feedback feedback : feedbacks) {
            combinedComments.append(feedback.getComment()).append(" ");
            totalRating += feedback.getRating();
        }

        double averageRating = totalRating / count;
        double sentimentScore = sentimentService.analyzeSentiment(combinedComments.toString().trim());
        double normalizedSentimentScore = sentimentScore * 5;

        double compositeScore = 0.5 * averageRating + 0.5 * normalizedSentimentScore;
        compositeScore = Math.max(1, Math.min(5, compositeScore));

        Map<String, Object> menuItemDetails = new HashMap<>();
        menuItemDetails.put("CompositeScore", compositeScore);
        menuItemDetails.put("CombinedComments", generateSentimentComment(compositeScore));
        menuItemDetails.put("AverageRating", averageRating);

        return menuItemDetails;
    }

    private String generateSentimentComment(double normalizedScore) {
        if (normalizedScore >= 4.0) {
            return "Excellent";
        } else if (normalizedScore >= 3.0) {
            return "Good";
        } else if (normalizedScore >= 2.0) {
            return "Average";
        } else if (normalizedScore >= 1.0) {
            return "Poor";
        } else {
            return " ";
        }
    }

    private void storeRatingAndCommentInMenu(Long menuItemId, double averageRating, String sentimentComment) {
        boolean response = foodItemDAO.updateRatingAndComment(menuItemId, (int) averageRating, sentimentComment);
        System.out.println("response: " + response);
    }

    private Map<String, List<Map<String, Object>>> extractTopFoodItemsByCategory(Map<Long, Map<String, Object>> menuItems, String numberOfItems) {
        Map<String, List<Map<String, Object>>> topFoodItemsByCategory = new HashMap<>();
        int numberOfFood = Integer.parseInt(numberOfItems);

        for (Map.Entry<Long, Map<String, Object>> entry : menuItems.entrySet()) {
            Long menuItemId = entry.getKey();
            Map<String, Object> details = entry.getValue();

            String category = determineCategory(menuItemId);
            details.put("Id", menuItemId);
            topFoodItemsByCategory.computeIfAbsent(category, k -> new ArrayList<>()).add(details);
        }

        for (String category : topFoodItemsByCategory.keySet()) {
            List<Map<String, Object>> sortedList = topFoodItemsByCategory.get(category).stream()
                    .sorted((e1, e2) -> Double.compare((Double) e2.get("CompositeScore"), (Double) e1.get("CompositeScore")))
                    .limit(numberOfFood)
                    .collect(Collectors.toList());

            List<Map<String, Object>> top3Items = new ArrayList<>();
            for (Map<String, Object> item : sortedList) {
                Long menuItemId = (Long) item.get("Id");
                FoodItem foodItem = foodItemDAO.getFoodItemById(menuItemId);
                if (foodItem != null) {
                    Map<String, Object> itemDetails = createFoodItemDetails(foodItem, item);
                    top3Items.add(itemDetails);
                }
            }

            topFoodItemsByCategory.put(category, top3Items);
        }

        return topFoodItemsByCategory;
    }

    private List<Map<String, Object>> extractLowRatedFoodItems(Map<Long, Map<String, Object>> menuItems) {
        List<Map<String, Object>> lowRatedFoodItems = new ArrayList<>();

        for (Map.Entry<Long, Map<String, Object>> entry : menuItems.entrySet()) {
            Map<String, Object> details = entry.getValue();
            double compositeScore = (double) details.get("CompositeScore");
            String sentimentComment = details.get("CombinedComments").toString();

            if (compositeScore < 2.0 && ("Average".equals(sentimentComment) || "Poor".equals(sentimentComment))) {
                FoodItem foodItem = foodItemDAO.getFoodItemById(entry.getKey());
                if (foodItem != null) {
                    Map<String, Object> itemDetails = createFoodItemDetails(foodItem, details);
                    lowRatedFoodItems.add(itemDetails);
                }
            }
        }

        return lowRatedFoodItems;
    }

    private Map<String, Object> createFoodItemDetails(FoodItem foodItem, Map<String, Object> details) {
        Map<String, Object> itemDetails = new HashMap<>();
        itemDetails.put("Id", foodItem.getFoodItemId());
        itemDetails.put("Name", foodItem.getItemName());
        itemDetails.put("Price", foodItem.getPrice());
        itemDetails.put("CompositeScore", details.get("CompositeScore"));
        itemDetails.put("AverageRating", details.get("AverageRating"));
        itemDetails.put("SentimentComment", details.get("CombinedComments"));
        return itemDetails;
    }

    private String determineCategory(Long menuItemId) {
        FoodItem foodItem = foodItemDAO.getFoodItemById(menuItemId);
        if (foodItem == null) {
            return "Unknown";
        }
        FoodItemType foodItemType = foodItemTypeDAO.getFoodItemTypeById(foodItem.getFoodItemTypeId());
        return foodItemType != null ? foodItemType.getFoodItemType() : "Unknown";
    }
}
