package model;

public class SentimentResult {
    private String comment;
    private String sentiment;

    public SentimentResult(String comment, String sentiment) {
        this.comment = comment;
        this.sentiment = sentiment;
    }

    public String getComment() {
        return comment;
    }

    public String getSentiment() {
        return sentiment;
    }

    @Override
    public String toString() {
        return "SentimentResult{" +
                "comment='" + comment + '\'' +
                ", sentiment='" + sentiment + '\'' +
                '}';
    }
}

