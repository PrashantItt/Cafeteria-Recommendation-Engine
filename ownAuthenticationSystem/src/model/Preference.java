package model;

public class Preference {
    private long preferenceId;
    private long userId;
    private long foodItemId;
    private long menuId;
    private boolean preference;

    public Preference(long preferenceId, long userId, long foodItemId, long menuId, boolean preference) {
        this.preferenceId = preferenceId;
        this.userId = userId;
        this.foodItemId = foodItemId;
        this.menuId = menuId;
        this.preference = preference;
    }

    // Getters and Setters
    public long getPreferenceId() {
        return preferenceId;
    }

    public void setPreferenceId(long preferenceId) {
        this.preferenceId = preferenceId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public boolean isPreference() {
        return preference;
    }

    public void setPreference(boolean preference) {
        this.preference = preference;
    }
}

