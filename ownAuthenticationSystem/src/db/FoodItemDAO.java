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
        String query = "UPDATE foodItem SET itemName = ?, price = ?, availabilityStatus = ?, foodItemTypeId = ? WHERE foodItemId = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, foodItem.getItemName());
            preparedStatement.setDouble(2, foodItem.getPrice());
            preparedStatement.setBoolean(3, foodItem.isAvailabilityStatus());
            preparedStatement.setLong(4, foodItem.getFoodItemTypeId());
            preparedStatement.setLong(5, foodItem.getFoodItemId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public FoodItem getFoodItemById(long foodItemId) {
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
                    return new FoodItem( itemName, price, availabilityStatus, foodItemTypeId);
                } else {
                    throw new IllegalArgumentException("No Food items with Id : " + foodItemId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<FoodItem> getFoodItemsByPrice(double minPrice, double maxPrice) {
        List<FoodItem> foodItems = new ArrayList<>();
        String query = "SELECT * FROM foodItem WHERE price >= ? AND price <= ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setDouble(1, minPrice);
            preparedStatement.setDouble(2, maxPrice);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    long foodItemId = resultSet.getLong("foodItemId");
                    String itemName = resultSet.getString("itemName");
                    double price = resultSet.getDouble("price");
                    boolean availabilityStatus = resultSet.getBoolean("availabilityStatus");
                    long foodItemTypeId = resultSet.getLong("foodItemTypeId");
                    foodItems.add(new FoodItem( itemName, price, availabilityStatus, foodItemTypeId));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foodItems;
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
                foodItems.add(new FoodItem( itemName, price, availabilityStatus, foodItemTypeId));
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
}
