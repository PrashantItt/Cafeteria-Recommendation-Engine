package db;

import model.FoodItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodItemDAO {

    public void addFoodItem(FoodItem foodItem) {
        String query = "INSERT INTO foodItem (itemName, price, availabilityStatus, foodItemTypeId, dietaryPreference, spiceLevel, cuisinePreference, sweetTooth) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = Database.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, foodItem.getItemName());
            statement.setDouble(2, foodItem.getPrice());
            statement.setBoolean(3, foodItem.isAvailabilityStatus());
            statement.setLong(4, foodItem.getFoodItemTypeId());
            statement.setString(5, foodItem.getDietaryPreference());
            statement.setString(6, foodItem.getSpiceLevel());
            statement.setString(7, foodItem.getCuisinePreference());
            statement.setString(8, foodItem.getSweetTooth());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteFoodItem(long foodItemId) {
        String query = "DELETE FROM foodItem WHERE foodItemId = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, foodItemId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateFoodItem(FoodItem foodItem) {
        String query = "UPDATE foodItem SET itemName = ?, price = ?, availabilityStatus = ?, foodItemTypeId = ?, dietaryPreference = ?, spiceLevel = ?, cuisinePreference = ?, sweetTooth = ? WHERE foodItemId = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, foodItem.getItemName());
            preparedStatement.setDouble(2, foodItem.getPrice());
            preparedStatement.setBoolean(3, foodItem.isAvailabilityStatus());
            preparedStatement.setLong(4, foodItem.getFoodItemTypeId());
            preparedStatement.setString(5, foodItem.getDietaryPreference());
            preparedStatement.setString(6, foodItem.getSpiceLevel());
            preparedStatement.setString(7, foodItem.getCuisinePreference());
            preparedStatement.setString(8, foodItem.getSweetTooth());
            preparedStatement.setLong(9, foodItem.getFoodItemId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<FoodItem> getAllFoodItems() {
        List<FoodItem> foodItems = new ArrayList<>();
        String query = "SELECT * FROM foodItem";
        try (Connection connection = Database.getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                long foodItemId = resultSet.getLong("foodItemId");
                String itemName = resultSet.getString("itemName");
                double price = resultSet.getDouble("price");
                boolean availabilityStatus = resultSet.getBoolean("availabilityStatus");
                long foodItemTypeId = resultSet.getLong("foodItemTypeId");
                String dietaryPreference = resultSet.getString("dietaryPreference");
                String spiceLevel = resultSet.getString("spiceLevel");
                String cuisinePreference = resultSet.getString("cuisinePreference");
                String sweetTooth = resultSet.getString("sweetTooth");
                foodItems.add(new FoodItem(foodItemId, itemName, price, availabilityStatus, foodItemTypeId, dietaryPreference, spiceLevel, cuisinePreference, sweetTooth));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foodItems;
    }

    public FoodItem getFoodItemById(long foodItemId) {
        FoodItem foodItem = null;
        String query = "SELECT * FROM foodItem WHERE foodItemId = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, foodItemId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    long id = resultSet.getLong("foodItemId");
                    String itemName = resultSet.getString("itemName");
                    double price = resultSet.getDouble("price");
                    boolean availabilityStatus = resultSet.getBoolean("availabilityStatus");
                    long foodItemTypeId = resultSet.getLong("foodItemTypeId");
                    int averageRating = resultSet.getInt("avg_rating");
                    String sentimentComments = resultSet.getString("sentiment_comment");
                    String dietaryPreference = resultSet.getString("dietaryPreference");
                    String spiceLevel = resultSet.getString("spiceLevel");
                    String cuisinePreference = resultSet.getString("cuisinePreference");
                    String sweetTooth = resultSet.getString("sweetTooth");

                    foodItem = new FoodItem(id, itemName, price, availabilityStatus, foodItemTypeId, averageRating, sentimentComments, dietaryPreference, spiceLevel, cuisinePreference, sweetTooth);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foodItem;
    }

    public FoodItem getFoodItemByName(String itemName) {
        FoodItem foodItem = null;
        String query = "SELECT * FROM foodItem WHERE itemName = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, itemName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    long foodItemId = resultSet.getLong("foodItemId");
                    double price = resultSet.getDouble("price");
                    boolean availabilityStatus = resultSet.getBoolean("availabilityStatus");
                    long foodItemTypeId = resultSet.getLong("foodItemTypeId");
                    foodItem = new FoodItem(foodItemId, itemName, price, availabilityStatus, foodItemTypeId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foodItem;
    }

    public boolean deleteFoodItemByName(String itemName) {
        String query = "DELETE FROM foodItem WHERE itemName = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, itemName);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateRatingAndComment(long foodItemId, int avgRating, String sentimentComment) {
        String query = "UPDATE foodItem SET avg_rating = ?, sentiment_comment = ? WHERE foodItemId = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, avgRating);
            preparedStatement.setString(2, sentimentComment);
            preparedStatement.setLong(3, foodItemId);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
