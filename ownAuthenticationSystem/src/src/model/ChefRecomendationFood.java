package model;

import java.sql.Date;

public class ChefRecomendationFood {
    private long id;
    private long foodItemId;
    private long foodtypeId;
    private Date date;



    public ChefRecomendationFood(long foodItemId, long foodtypeId) {
        this.foodItemId = foodItemId;
        this.foodtypeId = foodtypeId;
    }
    public ChefRecomendationFood(long foodItemId) {
        this.foodItemId = foodItemId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(long foodItemId) {
        this.foodItemId = foodItemId;
    }

    public long getFoodtypeId() {
        return foodtypeId;
    }
    public void setFoodTyoeId(long foodTypeId) {this.foodtypeId = foodTypeId;}

    public void setFoodTypeId(long foodtypeId) {
        this.foodtypeId = foodtypeId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ChefRecomendationFood{" +
                "id=" + id +
                ", foodItemId=" + foodItemId +
                ", foodtypeId=" + foodtypeId +
                ", date=" + date +
                '}';
    }
}

