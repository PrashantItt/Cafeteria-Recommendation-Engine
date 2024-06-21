package model;

// FoodRecommendationDTO.java

import java.util.Date;

public class FoodRecommendation {
    private Long id;
    private Date recommendationDate;
    private Long foodItemId;
    private double sentimentScore;
    private Long roleId;

    public FoodRecommendation(Long id, Date recommendationDate, Long foodItemId, double sentimentScore, Long roleId) {
        this.id = id;
        this.recommendationDate = recommendationDate;
        this.foodItemId = foodItemId;
        this.sentimentScore = sentimentScore;
        this.roleId = roleId;
    }
    public FoodRecommendation( Date recommendationDate, Long foodItemId, double sentimentScore) {
        this.recommendationDate = recommendationDate;
        this.foodItemId = foodItemId;
        this.sentimentScore = sentimentScore;
    }

    // Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRoleId() {
        return roleId;
    }
    public Date getRecommendationDate() {
        return recommendationDate;
    }
    public void setRoleId(long id) {
        this.roleId = roleId;
    }

    public void setRecommendationDate(Date recommendationDate) {
        this.recommendationDate = recommendationDate;
    }

    public Long getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(Long foodItemId) {
        this.foodItemId = foodItemId;
    }

    public double getSentimentScore() {
        return sentimentScore;
    }

    public void setSentimentScore(double sentimentScore) {
        this.sentimentScore = sentimentScore;
    }

    // toString() method for debugging
    @Override
    public String toString() {
        return "FoodRecommendationDTO{" +
                "id=" + id +
                ", recommendationDate=" + recommendationDate +
                ", foodItemId=" + foodItemId +
                ", roleId=" + roleId +
                ", sentimentScore=" + sentimentScore +
                '}';
    }
}

