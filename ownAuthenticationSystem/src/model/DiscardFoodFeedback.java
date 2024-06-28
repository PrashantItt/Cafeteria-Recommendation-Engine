package model;

import java.util.Date;

public class DiscardFoodFeedback {
    private int id;
    private String foodName;
    private int userID;
    private String question1;
    private String question2;
    private String question3;
    private Date feedbackDate;

    public DiscardFoodFeedback(int id, String foodName, int userID, String question1, String question2, String question3, Date feedbackDate) {
        this.id = id;
        this.foodName = foodName;
        this.userID = userID;
        this.question1 = question1;
        this.question2 = question2;
        this.question3 = question3;
        this.feedbackDate = feedbackDate;
    }

    public DiscardFoodFeedback(String foodName, int userID, String question1, String question2, String question3) {
        this.foodName = foodName;
        this.userID = userID;
        this.question1 = question1;
        this.question2 = question2;
        this.question3 = question3;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public String getQuestion2() {
        return question2;
    }

    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    public String getQuestion3() {
        return question3;
    }

    public void setQuestion3(String question3) {
        this.question3 = question3;
    }

    public Date getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(Date feedbackDate) {
        this.feedbackDate = feedbackDate;
    }
}

