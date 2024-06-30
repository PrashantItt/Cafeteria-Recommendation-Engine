package db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.ChefRecomendationFood;
import model.FoodItem;

public class ChefRecomendationFoodDAO {

    public boolean insert(ChefRecomendationFood recommendation) {
        String sql = "INSERT INTO chefRecomendationFood (foodItemId, Date) VALUES (?, CURDATE())";
        try (
                Connection connection = Database.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setLong(1, recommendation.getFoodItemId());
            int affectedRows = stmt.executeUpdate();
            return affectedRows == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ChefRecomendationFood> getAll() {
        List<ChefRecomendationFood> recommendations = new ArrayList<>();
        String sql = "SELECT * FROM chefRecomendationFood";
        try (
                Connection connection = Database.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                long foodItemId = rs.getLong("foodItemId");
                long foodtypeId = rs.getLong("foodtypeId");
                Date date = rs.getDate("Date");

                ChefRecomendationFood recommendation = new ChefRecomendationFood( foodItemId, foodtypeId);

                recommendations.add(recommendation);
                recommendations.add(recommendation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recommendations;
    }

    public List<FoodItem> getTommorowMenu() {
        List<FoodItem> recommendedFoodItems = new ArrayList<>();
        String query = "SELECT fi.foodItemId, fi.itemName, fi.price, fi.avg_rating, fi.sentiment_comment, fi.foodItemTypeId " +
                "FROM chefrecomendationfood crf " +
                "JOIN fooditem fi ON crf.foodItemId = fi.foodItemId " +
                "WHERE crf.Date = CURDATE()";//"WHERE crf.Date = CURDATE() + INTERVAL 1 DAY";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                long foodItemId = resultSet.getLong("foodItemId");
                String itemName = resultSet.getString("itemName");
                double price = resultSet.getDouble("price");
                int avgRating = resultSet.getInt("avg_rating");
                String sentimentComment = resultSet.getString("sentiment_comment");
                long foodItemTypeId = resultSet.getLong("foodItemTypeId");

                FoodItem foodItem = new FoodItem(foodItemId, itemName, price, true, foodItemTypeId, avgRating, sentimentComment);
                recommendedFoodItems.add(foodItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recommendedFoodItems;
    }

    public boolean insertVote(long foodItemId) {
        String sql = "UPDATE chefRecomendationFood SET vote = vote + 1 WHERE foodItemId = ?";
        try (
                Connection connection = Database.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setLong(1, foodItemId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ChefRecomendationFood> getTop3FoodItemsForToday() {
        List<ChefRecomendationFood> topFoodItems = new ArrayList<>();
        String sql = "SELECT crf.foodItemId, crf.foodtypeId, MAX(crf.vote) AS max_votes " +
                "FROM chefRecomendationFood crf " +
                "WHERE crf.Date = ? " +
                "GROUP BY crf.foodItemId, crf.foodtypeId " +
                "ORDER BY max_votes DESC " +
                "LIMIT 3";

        try (
                Connection connection = Database.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setDate(1, Date.valueOf(LocalDate.now()));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    long foodItemId = rs.getLong("foodItemId");
                    long foodtypeId = rs.getLong("foodtypeId");

                    ChefRecomendationFood foodItem = new ChefRecomendationFood(foodItemId, foodtypeId);
                    topFoodItems.add(foodItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topFoodItems;
    }

}

