package recomendationEngine;

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
import model.SentimentResult;

public class RecommendationSystem {
    private FeedbackDAO feedbackDAO;
    private SentimentAnalysisService sentimentService;
    private FoodItemDAO foodItemDAO;
    private FoodItemTypeDAO foodItemTypeDAO;

    public RecommendationSystem() {
        this.feedbackDAO = new FeedbackDAO();
        this.sentimentService = new SentimentAnalysisService();
        this.foodItemDAO = new FoodItemDAO();
        this.foodItemTypeDAO = new FoodItemTypeDAO();
    }

    public Map<String, List<Map<String, Object>>> getTopFoodItemsByCategory(String numberOfItems) {
        Map<Long, Map<String, Object>> menuItems = calculateSentimentScores();
        return extractTopFoodItemsByCategory(menuItems, numberOfItems);
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

            storeRatingAndCommentInMenu(menuItemId, (double) menuItemDetails.get("AverageRating"), (String) menuItemDetails.get("SentimentComment"));
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
        SentimentResult sentimentResult = sentimentService.analyzeSentiment(combinedComments.toString().trim());
        double normalizedSentimentScore = sentimentResult.getSentimentScore() * 5;

        double compositeScore = 0.5 * averageRating + 0.5 * normalizedSentimentScore;
        compositeScore = Math.max(1, Math.min(5, compositeScore));

        Map<String, Object> menuItemDetails = new HashMap<>();
        menuItemDetails.put("CompositeScore", compositeScore);
        menuItemDetails.put("SentimentComment", sentimentResult.getSentimentComment());
        menuItemDetails.put("AverageRating", averageRating);

        return menuItemDetails;
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

            List<Map<String, Object>> topItems = new ArrayList<>();
            for (Map<String, Object> item : sortedList) {
                Long menuItemId = (Long) item.get("Id");
                FoodItem foodItem = foodItemDAO.getFoodItemById(menuItemId);
                if (foodItem != null) {
                    Map<String, Object> itemDetails = createFoodItemDetails(foodItem, item);
                    topItems.add(itemDetails);
                }
            }

            topFoodItemsByCategory.put(category, topItems);
        }

        return topFoodItemsByCategory;
    }

    private List<Map<String, Object>> extractLowRatedFoodItems(Map<Long, Map<String, Object>> menuItems) {
        List<Map<String, Object>> lowRatedFoodItems = new ArrayList<>();

        SentimentAnalysisService sentimentService = new SentimentAnalysisService();

        for (Map.Entry<Long, Map<String, Object>> entry : menuItems.entrySet()) {
            Map<String, Object> details = entry.getValue();
            double averageRating = (double) details.get("AverageRating");
            System.out.println(averageRating);
            String sentimentComment = (String) details.get("SentimentComment");
            System.out.println(sentimentComment);

            if (averageRating < 2.0 && sentimentService.containsNegativeKeywords(sentimentComment)) {
                Long menuItemId = entry.getKey();
                FoodItem foodItem = foodItemDAO.getFoodItemById(menuItemId);
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
        itemDetails.put("SentimentComment", details.get("SentimentComment"));
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
