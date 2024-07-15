package model;

public class FoodItem {
    private long foodItemId;
    private String itemName;
    private double price;
    private boolean availabilityStatus;
    private long foodItemTypeId;
    private int avgRating;
    private String sentimentComment;
    private String dietaryPreference;
    private String spiceLevel;
    private String cuisinePreference;
    private String sweetTooth;

    public FoodItem(Long foodItemId,String itemName, double price, boolean availabilityStatus, long foodItemTypeId,
                    String dietaryPreference, String spiceLevel, String cuisinePreference, String sweetTooth) {
        this.foodItemId = foodItemId;
        this.itemName = itemName;
        this.price = price;
        this.availabilityStatus = availabilityStatus;
        this.foodItemTypeId = foodItemTypeId;
        this.dietaryPreference = dietaryPreference;
        this.spiceLevel = spiceLevel;
        this.cuisinePreference = cuisinePreference;
        this.sweetTooth = sweetTooth;
    }

    public FoodItem(Long foodItemId,String itemName, double price, boolean availabilityStatus, long foodItemTypeId) {
        this.foodItemId = foodItemId;
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

    public FoodItem(long id, String itemName, double price, boolean availabilityStatus, long foodItemTypeId, int averageRating, String sentimentComments, String dietaryPreference, String spiceLevel, String cuisinePreference, String sweetTooth) {
        this.foodItemId = id;
        this.itemName = itemName;
        this.price = price;
        this.availabilityStatus = availabilityStatus;
        this.foodItemTypeId = foodItemTypeId;
        this.avgRating = averageRating;
        this.sentimentComment = sentimentComments;
        this.dietaryPreference = dietaryPreference;
        this.spiceLevel = spiceLevel;
        this.cuisinePreference = cuisinePreference;
        this.sweetTooth = sweetTooth;
    }

    public FoodItem(String itemName, double price, boolean availabilityStatus, long foodItemTypeId, String dietaryPreference, String spiceLevel, String cuisinePreference, String sweetTooth) {
        this.itemName = itemName;
        this.price = price;
        this.availabilityStatus = availabilityStatus;
        this.foodItemTypeId = foodItemTypeId;
        this.dietaryPreference = dietaryPreference;
        this.spiceLevel = spiceLevel;
        this.cuisinePreference = cuisinePreference;
        this.sweetTooth = sweetTooth;
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
    public String getDietaryPreference() { return dietaryPreference; }

    public void setDietaryPreference(String dietaryPreference) {
        this.dietaryPreference = dietaryPreference;
    }

    public String getSpiceLevel() { return spiceLevel; }
    public void setSpiceLevel(String spiceLevel) {
        this.spiceLevel = spiceLevel;
    }

    public String getCuisinePreference() {
        return cuisinePreference;
    }
    public void setCuisinePreference(String cuisinePreference) {
        this.cuisinePreference = cuisinePreference;
    }

    public String getSweetTooth() {
        return sweetTooth;
    }
    public void setSweetTooth(String sweetTooth) {
        this.sweetTooth = sweetTooth;
    }


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
