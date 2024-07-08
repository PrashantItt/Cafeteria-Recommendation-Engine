package recomendationEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.SentimentResult;

public class SentimentAnalysisService {
    private String[] positiveKeywords = {
            "good", "great", "excellent", "awesome", "fantastic",
            "delicious", "tasty", "yummy", "scrumptious", "savory",
            "mouthwatering", "delectable", "flavorful", "succulent", "appetizing",
            "fresh", "satisfying", "juicy", "tender", "perfect",
            "heavenly", "divine", "exquisite", "superb", "lovely", "nice",
            "nutritious", "wholesome", "aromatic", "crispy", "delightful",
            "golden", "hearty", "luscious", "spicy", "velvety", "well-seasoned"
    };

    private String[] negativeKeywords = {
            "bad", "poor", "terrible", "horrible", "awful",
            "bland", "stale", "overcooked", "undercooked", "tasteless",
            "greasy", "salty", "burnt", "dry", "rubbery",
            "unappetizing", "disgusting", "soggy", "inedible", "flavorless",
            "spoiled", "rotten", "bitter", "chewy", "tough",
            "cold", "fatty", "mushy", "raw", "unseasoned"
    };

    public SentimentResult analyzeSentiment(String comment) {
        comment = comment.toLowerCase().trim();
        Map<String, Integer> positiveMatchCounts = countKeywordMatches(comment, positiveKeywords);
        Map<String, Integer> negativeMatchCounts = countKeywordMatches(comment, negativeKeywords);

        double sentimentScore = calculateSentimentScore(positiveMatchCounts.size(), negativeMatchCounts.size());
        String sentimentComment = generateSentimentComment(positiveMatchCounts, negativeMatchCounts);
        System.out.println("Sentiment Comment: " + sentimentComment);

        return new SentimentResult(sentimentScore, sentimentComment);
    }

    private Map<String, Integer> countKeywordMatches(String comment, String[] keywords) {
        Map<String, Integer> keywordCounts = new HashMap<>();
        for (String keyword : keywords) {
            Pattern pattern = Pattern.compile("\\b" + keyword + "\\b");
            Matcher matcher = pattern.matcher(comment);
            int count = 0;
            while (matcher.find()) {
                count++;
            }
            if (count > 0) {
                keywordCounts.put(keyword, count);
            }
        }
        return keywordCounts;
    }

    private double calculateSentimentScore(int positiveCount, int negativeCount) {
        if (positiveCount > negativeCount) {
            return 1.0;
        } else if (negativeCount > positiveCount) {
            return -1.0;
        } else {
            return 0.0;
        }
    }

    private String generateSentimentComment(Map<String, Integer> positiveMatchCounts, Map<String, Integer> negativeMatchCounts) {
        StringBuilder sentimentComment = new StringBuilder();

        int positiveCount = positiveMatchCounts.values().stream().mapToInt(Integer::intValue).sum();
        int negativeCount = negativeMatchCounts.values().stream().mapToInt(Integer::intValue).sum();

        if (positiveCount > negativeCount) {
            int countPositiveWords = 0;
            for (Map.Entry<String, Integer> entry : positiveMatchCounts.entrySet()) {
                if (countPositiveWords < 3) {
                    sentimentComment.append(entry.getKey()).append(", ");
                    countPositiveWords++;
                } else {
                    break;
                }
            }
            if (sentimentComment.length() > 0) {
                sentimentComment.setLength(sentimentComment.length() - 2);
            }
            sentimentComment.append(". ");
        } else if (negativeCount > positiveCount) {
            int countNegativeWords = 0;
            for (Map.Entry<String, Integer> entry : negativeMatchCounts.entrySet()) {
                if (countNegativeWords < 3) {
                    sentimentComment.append(entry.getKey()).append(", ");
                    countNegativeWords++;
                } else {
                    break;
                }
            }
            if (sentimentComment.length() > 0) {
                sentimentComment.setLength(sentimentComment.length() - 2);
            }
            sentimentComment.append(". ");
        } else {
            if (positiveCount > 0) {
                int countPositiveWords = 0;
                for (Map.Entry<String, Integer> entry : positiveMatchCounts.entrySet()) {
                    if (countPositiveWords < 3) {
                        sentimentComment.append(entry.getKey()).append(", ");
                        countPositiveWords++;
                    } else {
                        break;
                    }
                }
                if (sentimentComment.length() > 0) {
                    sentimentComment.setLength(sentimentComment.length() - 2);
                }
                sentimentComment.append(". ");
            }

            if (negativeCount > 0) {
                int countNegativeWords = 0;
                for (Map.Entry<String, Integer> entry : negativeMatchCounts.entrySet()) {
                    if (countNegativeWords < 3) {
                        sentimentComment.append(entry.getKey()).append(", ");
                        countNegativeWords++;
                    } else {
                        break;
                    }
                }
                if (sentimentComment.length() > 0) {
                    sentimentComment.setLength(sentimentComment.length() - 2);
                }
                sentimentComment.append(". ");
            }

            if (positiveCount == 0 && negativeCount == 0) {
                sentimentComment.append("No significant feedback.");
            }
        }

        return sentimentComment.toString();
    }

    public boolean containsNegativeKeywords(String sentimentComment) {
        String[] words = sentimentComment.split(", ");

        for (String word : words) {
            String cleanWord = word.replaceAll("[^a-zA-Z0-9]", "");

            for (String keyword : negativeKeywords) {
                if (cleanWord.equalsIgnoreCase(keyword)) {
                    return true;
                }
            }
        }
        return false;
    }
}
