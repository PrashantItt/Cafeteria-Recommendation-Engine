package model;

public class FoodItemType {
    private long foodItemTypeId;
    private String foodItemType;

    public FoodItemType(long foodItemTypeId, String foodItemType) {
        this.foodItemTypeId = foodItemTypeId;
        this.foodItemType = foodItemType;
    }

    public long getFoodItemTypeId() {
        return foodItemTypeId;
    }

    public void setFoodItemTypeId(long foodItemTypeId) {
        this.foodItemTypeId = foodItemTypeId;
    }

    public String getFoodItemType() {
        return foodItemType;
    }

    public void setFoodItemType(String foodItemType) {
        this.foodItemType = foodItemType;
    }
}
