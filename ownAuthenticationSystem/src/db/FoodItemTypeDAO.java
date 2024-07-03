package db;

import model.FoodItemType;
import db.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodItemTypeDAO {

    public boolean addFoodItemType(String foodItemType) {
        String query = "INSERT INTO foodItemType (foodItemType) VALUES (?)";
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, foodItemType);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public FoodItemType getFoodItemTypeById(long foodItemTypeId) {
        String query = "SELECT * FROM foodItemType WHERE foodItemTypeId = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, foodItemTypeId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String foodItemType = resultSet.getString("foodItemType");
                    return new FoodItemType(foodItemTypeId, foodItemType);
                } else {
                    throw new IllegalArgumentException("Invalid foodItemTypeId, cannot find foodItemType with given foodItemTypeId: " + foodItemTypeId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public FoodItemType getFoodItemTypeByName(String foodItemType) {
        String query = "SELECT * FROM foodItemType WHERE foodItemType = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, foodItemType);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    long foodItemTypeId = resultSet.getLong("foodItemTypeId");
                    return new FoodItemType(foodItemTypeId, foodItemType);
                } else {
                    throw new IllegalArgumentException("Invalid foodItemType, cannot find foodItemType with given foodItemType: " + foodItemType);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<FoodItemType> getAllFoodItemTypes() {
        List<FoodItemType> foodItemTypes = new ArrayList<>();
        String query = "SELECT * FROM foodItemType";
        try (Connection connection = Database.getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                long foodItemTypeId = resultSet.getLong("foodItemTypeId");
                String foodItemType = resultSet.getString("foodItemType");
                foodItemTypes.add(new FoodItemType(foodItemTypeId, foodItemType));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foodItemTypes;
    }
}
