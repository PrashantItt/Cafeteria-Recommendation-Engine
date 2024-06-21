package db;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.FoodRecommendation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class FoodRecommendationDAO {

    public boolean addFoodRecommendation(Date recommendationDate, Long foodItemId, double sentimentScore) {
        String query = "INSERT INTO foodrecommendations (recommendationDate, foodItemId, ScentimentScore) VALUES (?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setDate(1, new java.sql.Date(recommendationDate.getTime()));
            preparedStatement.setLong(2,  foodItemId);
            preparedStatement.setDouble(3, sentimentScore);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteFoodRecommendation(int recommendationId) {
        String query = "DELETE FROM foodrecommendations WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, recommendationId);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateFoodRecommendationSentimentScore(int recommendationId, double sentimentScore) {
        String query = "UPDATE foodrecommendations SET sentimentScore = ? WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setDouble(1, sentimentScore);
            preparedStatement.setInt(2, recommendationId);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

   /* public FoodRecommendation getFoodRecommendationById(int recommendationId) {
        String query = "SELECT * FROM foodrecommendations WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, recommendationId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Date recommendationDate = resultSet.getDate("recommendationDate");
                    int foodItemId = resultSet.getInt("foodItemId");
                    double sentimentScore = resultSet.getDouble("sentimentScore");
                    return new FoodRecommendation( recommendationDate, foodItemId, sentimentScore);
                } else {
                    throw new IllegalArgumentException("Invalid recommendationId, cannot find recommendation with given id: " + recommendationId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    public List<FoodRecommendation> getAllFoodRecommendations() {
        List<FoodRecommendation> foodRecommendations = new ArrayList<>();
        String query = "SELECT * FROM foodrecommendations";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    Date recommendationDate = resultSet.getDate("recommendationDate");
                    Long foodItemId = resultSet.getLong("foodItemId");
                    double sentimentScore = resultSet.getDouble("sentimentScore");
                    FoodRecommendation recommendation = new FoodRecommendation( recommendationDate, foodItemId, sentimentScore);
                    foodRecommendations.add(recommendation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foodRecommendations;
    }

   /* public List<FoodRecommendation> getTodayFoodRecommendations() {
        List<FoodRecommendation> foodRecommendations = new ArrayList<>();
        String query = "SELECT * FROM foodrecommendations WHERE recommendationDate = CURDATE()";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    Date recommendationDate = resultSet.getDate("recommendationDate");
                    Long foodItemId = resultSet.getLong("foodItemId");
                    double sentimentScore = resultSet.getDouble("ScentimentScore");
                    FoodRecommendation recommendation = new FoodRecommendation(id,recommendationDate, foodItemId, sentimentScore,roleId);
                    foodRecommendations.add(recommendation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foodRecommendations;
    }*/

    public List<FoodRecommendation> getTodayFoodRecommendations() {
        List<FoodRecommendation> foodRecommendations = new ArrayList<>();
        String query = "SELECT fr.*, fi.foodItemTypeId FROM foodrecommendations fr " +
                "JOIN foodItem fi ON fr.foodItemId = fi.foodItemId " +
                "WHERE fr.recommendationDate = CURDATE()";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    Date recommendationDate = resultSet.getDate("recommendationDate");
                    Long foodItemId = resultSet.getLong("foodItemId");
                    double sentimentScore = resultSet.getDouble("ScentimentScore");
                    Long roleId = resultSet.getLong("foodItemTypeId"); // Retrieve roleId from result set

                    FoodRecommendation recommendation = new FoodRecommendation(id, recommendationDate, foodItemId, sentimentScore, roleId);
                    foodRecommendations.add(recommendation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foodRecommendations;
    }



}

