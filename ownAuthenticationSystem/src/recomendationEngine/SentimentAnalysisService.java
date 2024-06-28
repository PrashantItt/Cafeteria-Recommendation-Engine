package recomendationEngine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SentimentAnalysisService {

    public double analyzeSentiment(String comment) {
        comment = comment.toLowerCase().trim();

        String[] positiveKeywords = {
                "good", "great", "excellent", "awesome", "fantastic",
                "delicious", "tasty", "yummy", "scrumptious", "savory",
                "mouthwatering", "delectable", "flavorful", "succulent", "appetizing",
                "fresh", "satisfying", "juicy", "tender", "perfect",
                "heavenly", "divine", "exquisite", "superb", "lovely"
        };

        String[] negativeKeywords = {
                "bad", "poor", "terrible", "horrible", "awful",
                "bland", "stale", "overcooked", "undercooked", "tasteless",
                "greasy", "salty", "burnt", "dry", "rubbery",
                "unappetizing", "disgusting", "soggy", "inedible", "flavorless",
                "spoiled", "rotten", "bitter", "chewy", "tough"
        };

        int positiveMatches = countKeywordMatches(comment, positiveKeywords);
        int negativeMatches = countKeywordMatches(comment, negativeKeywords);

        double sentimentScore = 0.0;
        if (positiveMatches > negativeMatches) {
            sentimentScore = 1.0;
        } else if (negativeMatches > positiveMatches) {
            sentimentScore = -1.0;
        }

        return sentimentScore;
    }

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
