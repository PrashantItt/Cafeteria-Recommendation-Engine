package model;

public class FoodItem {
    private long foodItemId;
    private String itemName;
    private double price;
    private boolean availabilityStatus;
    private long foodItemTypeId;
    private int avgRating;
    private String sentimentComment;

    public FoodItem(Long foodItemId,String itemName, double price, boolean availabilityStatus, long foodItemTypeId) {
        this.foodItemId = foodItemId;
        this.itemName = itemName;
        this.price = price;
        this.availabilityStatus = availabilityStatus;
        this.foodItemTypeId = foodItemTypeId;
    }
    public FoodItem(String itemName, double price, boolean availabilityStatus, long foodItemTypeId) {
        this.itemName = itemName;
        this.price = price;
        this.availabilityStatus = availabilityStatus;
        this.foodItemTypeId = foodItemTypeId;
    }
    public FoodItem(Long foodItemId, String itemName, double price, boolean availabilityStatus, long foodItemTypeId, int avgRating, String sentimentComment) {
        this(foodItemId, itemName, price, availabilityStatus, foodItemTypeId);
        this.avgRating = avgRating;
        this.sentimentComment = sentimentComment;
    }

    public long getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(long foodItemId) {
        this.foodItemId = foodItemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(boolean availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public long getFoodItemTypeId() {
        return foodItemTypeId;
    }

    public void setFoodItemTypeId(long foodItemTypeId) {
        this.foodItemTypeId = foodItemTypeId;
    }

    public int getAvgRating() { return avgRating; }

    public void setAvgRating(int avgRating) { this.avgRating = avgRating; }

    public String getSentimentComment() { return sentimentComment; }

    public void setSentimentComment(String sentimentComment) { this.sentimentComment = sentimentComment; }

    @Override
    public String toString() {
        return "FoodItem{" +
                "foodItemId=" + foodItemId +
                ", itemName='" + itemName + '\'' +
                ", price=" + price +
                ", availabilityStatus=" + availabilityStatus +
                ", foodItemTypeId=" + foodItemTypeId +
                '}';
    }
}
