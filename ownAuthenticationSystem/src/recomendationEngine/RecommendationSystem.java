package recomendationEngine;

import java.sql.Connection;
import java.sql.SQLException;
import db.FeedbackDAO;
import model.Feedback;
import java.util.*;
import db.FoodRecommendationDAO;
import java.util.Date;

public class RecommendationSystem {
    private FeedbackDAO feedbackDAO;
    private FoodRecommendationDAO foodRecommendationDAO;
    private SentimentAnalysisService sentimentService;

    public RecommendationSystem() throws SQLException {
        this.feedbackDAO = new FeedbackDAO();
        this.sentimentService = new SentimentAnalysisService();
        this.foodRecommendationDAO = new FoodRecommendationDAO();
    }

    public void calculateSentimentScores() {
        List<Feedback> feedbackList = feedbackDAO.getAllFeedback();
        Map<Long, StringBuilder> menuItemComments = new HashMap<>();

        for (Feedback feedback : feedbackList) {
            Long menuItemId = feedback.getMenuItemId();
            if (!menuItemComments.containsKey(menuItemId)) {
                menuItemComments.put(menuItemId, new StringBuilder());
            }
            menuItemComments.get(menuItemId).append(feedback.getComment()).append(" "); // Combine comments
        }

        for (Map.Entry<Long, StringBuilder> entry : menuItemComments.entrySet()) {
            Long menuItemId = entry.getKey();
            String combinedComments = entry.getValue().toString().trim(); // Get combined comments

            double sentimentScore = sentimentService.analyzeSentiment(combinedComments);

            System.out.println("MenuItemId: " + menuItemId +
                    ", Combined Comments: " + combinedComments +
                    ", Sentiment Score: " + sentimentScore);

            foodRecommendationDAO.addFoodRecommendation(new Date(), menuItemId, sentimentScore);

        }
    }

}
