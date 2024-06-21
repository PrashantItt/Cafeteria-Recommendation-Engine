package model;

public class MenuItem {
    private long menuItemId;
    private long foodItemId;
    private long menuId;
    private int quantity;

    public MenuItem(long menuItemId, long foodItemId, long menuId, int quantity) {
        this.menuItemId = menuItemId;
        this.foodItemId = foodItemId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public long getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(long foodItemId) {
        this.foodItemId = foodItemId;
    }

    public long getMenuId() {
        return menuId;
    }

    public void setMenuId(long menuId) {
        this.menuId = menuId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}