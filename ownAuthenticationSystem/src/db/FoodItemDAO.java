package db;

import model.FoodItem;
import db.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodItemDAO {

    public boolean addFoodItem(FoodItem foodItem) {
        String query = "INSERT INTO foodItem (itemName, price, availabilityStatus, foodItemTypeId) VALUES (?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, foodItem.getItemName());
            preparedStatement.setDouble(2, foodItem.getPrice());
            preparedStatement.setBoolean(3, foodItem.isAvailabilityStatus());
            preparedStatement.setLong(4, foodItem.getFoodItemTypeId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteFoodItem(long foodItemId) {
        String query = "DELETE FROM foodItem WHERE foodItemId = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, foodItemId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateFoodItem(FoodItem foodItem) {
        String query = "UPDATE foodItem SET foodItemId = ?, itemName = ?, price = ?, availabilityStatus = ?, foodItemTypeId = ? WHERE foodItemId = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setDouble(1, foodItem.getFoodItemId());
            preparedStatement.setString(2, foodItem.getItemName());
            preparedStatement.setDouble(3, foodItem.getPrice());
            preparedStatement.setBoolean(4, foodItem.isAvailabilityStatus());
            preparedStatement.setLong(5, foodItem.getFoodItemTypeId());
            preparedStatement.setLong(6, foodItem.getFoodItemId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<FoodItem> getAvailableFoodItems() {
        List<FoodItem> foodItems = new ArrayList<>();
        String query = "SELECT * FROM foodItem WHERE availabilityStatus = 1";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                long foodItemId = resultSet.getLong("foodItemId");
                String itemName = resultSet.getString("itemName");
                double price = resultSet.getDouble("price");
                boolean availabilityStatus = resultSet.getBoolean("availabilityStatus");
                long foodItemTypeId = resultSet.getLong("foodItemTypeId");
                foodItems.add(new FoodItem( itemName, price, availabilityStatus, foodItemTypeId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foodItems;
    }

    public List<FoodItem> getAllFoodItems() {
        List<FoodItem> foodItems = new ArrayList<>();
        String query = "SELECT * FROM foodItem";
        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                long foodItemId = resultSet.getLong("foodItemId");
                String itemName = resultSet.getString("itemName");
                double price = resultSet.getDouble("price");
                boolean availabilityStatus = resultSet.getBoolean("availabilityStatus");
                long foodItemTypeId = resultSet.getLong("foodItemTypeId");
                foodItems.add(new FoodItem(foodItemId,itemName, price, availabilityStatus, foodItemTypeId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foodItems;
    }

    public List<FoodItem> getFoodItemsByType(long foodItemTypeId) {
        List<FoodItem> foodItems = new ArrayList<>();
        String query = "SELECT * FROM foodItem WHERE foodItemTypeId = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, foodItemTypeId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    long foodItemId = resultSet.getLong("foodItemId");
                    String itemName = resultSet.getString("itemName");
                    double price = resultSet.getDouble("price");
                    boolean availabilityStatus = resultSet.getBoolean("availabilityStatus");
                    foodItems.add(new FoodItem( itemName, price, availabilityStatus, foodItemTypeId));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foodItems;
    }

    public FoodItem getFoodItemById(long foodItemId) {
        FoodItem foodItem = null;
        String query = "SELECT * FROM foodItem WHERE foodItemId = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, foodItemId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String itemName = resultSet.getString("itemName");
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
    public FoodItem getFoodItemByName(String itemName) {
        FoodItem foodItem = null;
        String query = "SELECT * FROM foodItem WHERE itemName = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

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
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, itemName);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateRatingAndComment(long foodItemId, int avgRating, String sentimentComment) {
        String query = "UPDATE foodItem SET avg_rating = ?, sentiment_comment = ? WHERE foodItemId = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

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
