package model;
public class SentimentResult {
    private final double sentimentScore;
    private final String sentimentComment;

    public SentimentResult(double sentimentScore, String sentimentComment) {
        this.sentimentScore = sentimentScore;
        this.sentimentComment = sentimentComment;
    }

    public double getSentimentScore() {
        return sentimentScore;
    }

    public String getSentimentComment() {
        return sentimentComment;
    }
}