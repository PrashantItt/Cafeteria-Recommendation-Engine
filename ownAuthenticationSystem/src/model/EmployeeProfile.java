package model;

public class EmployeeProfile {
    private long userId;
    private String name;
    private String dietaryPreference;
    private String spiceLevel;
    private String cuisinePreference;
    private String sweetTooth;

    public EmployeeProfile(long userId, String name, String dietaryPreference, String spiceLevel, String cuisinePreference, String sweetTooth) {
        this.userId = userId;
        this.name = name;
        this.dietaryPreference = dietaryPreference;
        this.spiceLevel = spiceLevel;
        this.cuisinePreference = cuisinePreference;
        this.sweetTooth = sweetTooth;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDietaryPreference() {
        return dietaryPreference;
    }

    public void setDietaryPreference(String dietaryPreference) {
        this.dietaryPreference = dietaryPreference;
    }

    public String getSpiceLevel() {
        return spiceLevel;
    }

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
}
