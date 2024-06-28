package model;

import java.util.Date;

public class DiscardFoodItem {
    private long id;
    private String foodName;
    private String likeAboutFood;
    private String dislikeAboutFood;
    private String momRecipe;
    private long userId;
    private Date date;

    public DiscardFoodItem(long id, String foodName, String likeAboutFood, String dislikeAboutFood, String momRecipe,long userId, Date date) {
        this.id = id;
        this.foodName = foodName;
        this.likeAboutFood = likeAboutFood;
        this.dislikeAboutFood = dislikeAboutFood;
        this.momRecipe = momRecipe;
        this.userId = userId;
        this.date = date;
    }
    public DiscardFoodItem( String foodName, String likeAboutFood, String dislikeAboutFood, String momRecipe,long userId, Date date) {
        this.foodName = foodName;
        this.likeAboutFood = likeAboutFood;
        this.dislikeAboutFood = dislikeAboutFood;
        this.momRecipe = momRecipe;
        this.userId = userId;
        this.date = date;
    }

    public DiscardFoodItem( String foodName, String likeAboutFood, String dislikeAboutFood, String momRecipe,long userId) {
        this.foodName = foodName;
        this.likeAboutFood = likeAboutFood;
        this.dislikeAboutFood = dislikeAboutFood;
        this.momRecipe = momRecipe;
        this.userId = userId;
    }
    public DiscardFoodItem( String foodName) {
        this.foodName = foodName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getLikeAboutFood() {
        return likeAboutFood;
    }

    public void setLikeAboutFood(String likeAboutFood) {
        this.likeAboutFood = likeAboutFood;
    }

    public String getDislikeAboutFood() {
        return dislikeAboutFood;
    }

    public void setDislikeAboutFood(String dislikeAboutFood) {
        this.dislikeAboutFood = dislikeAboutFood;
    }

    public String getMomRecipe() {
        return momRecipe;
    }

    public void setMomRecipe(String momRecipe) {
        this.momRecipe = momRecipe;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }
    public void setDiscardDate(Date date) {
        this.date = date;
    }
    public Date getDiscardDate() {
        return date;
    }

}

