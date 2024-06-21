package recomendationEngine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SentimentAnalysisService {

    // Method to perform sentiment analysis and return sentiment score
    public double analyzeSentiment(String comment) {
        // Normalize the comment text
        comment = comment.toLowerCase().trim();

        // Define positive and negative keywords
        String[] positiveKeywords = {"good", "great", "excellent", "awesome", "fantastic"};
        String[] negativeKeywords = {"bad", "poor", "terrible", "horrible", "awful"};

        // Calculate sentiment score based on keyword matches
        int positiveMatches = countKeywordMatches(comment, positiveKeywords);
        int negativeMatches = countKeywordMatches(comment, negativeKeywords);

        // Determine sentiment score based on matches
        double sentimentScore = 0.0;
        if (positiveMatches > negativeMatches) {
            sentimentScore = 1.0; // Positive sentiment
        } else if (negativeMatches > positiveMatches) {
            sentimentScore = -1.0; // Negative sentiment
        }

        return sentimentScore;
    }

    // Method to count occurrences of keywords in a comment
    private int countKeywordMatches(String comment, String[] keywords) {
        int count = 0;
        for (String keyword : keywords) {
            Pattern pattern = Pattern.compile("\\b" + keyword + "\\b");
            Matcher matcher = pattern.matcher(comment);
            while (matcher.find()) {
                count++;
            }
        }
        return count;
    }
}

