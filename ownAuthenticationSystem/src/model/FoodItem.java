package model;

public class FoodItem {
    private long foodItemId;
    private String itemName;
    private double price;
    private boolean availabilityStatus;
    private long foodItemTypeId;

    public FoodItem(String itemName, double price, boolean availabilityStatus, long foodItemTypeId) {
       // this.foodItemId = foodItemId;
        this.itemName = itemName;
        this.price = price;
        this.availabilityStatus = availabilityStatus;
        this.foodItemTypeId = foodItemTypeId;
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
