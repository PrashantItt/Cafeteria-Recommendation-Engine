package db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.ChefRecomendationFood;
import model.FoodItem;

public class ChefRecomendationFoodDAO {

    public boolean insert(ChefRecomendationFood recommendation) {
        String sql = "INSERT INTO chefRecomendationFood (foodItemId, Date) VALUES (?, CURDATE())";
        try (Connection connection = Database.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, recommendation.getFoodItemId());
            int affectedRows = stmt.executeUpdate();
            return affectedRows == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<FoodItem> getTommorowMenu() {
        List<FoodItem> recommendedFoodItems = new ArrayList<>();
        String query = "SELECT fi.foodItemId, fi.itemName, fi.price, fi.avg_rating, fi.sentiment_comment, fi.foodItemTypeId " + "FROM chefrecomendationfood crf " + "JOIN fooditem fi ON crf.foodItemId = fi.foodItemId " + "WHERE crf.Date = CURDATE()";

        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);
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
        String sql = "UPDATE chefRecomendationFood SET vote = vote + 1 WHERE foodItemId = ? AND `Date` = CURDATE()";

        try (Connection connection = Database.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, foodItemId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ChefRecomendationFood> getTopFoodItemsForToday() {
        List<ChefRecomendationFood> topFoodItems = new ArrayList<>();
        String sql = "SELECT crf.foodItemId, crf.foodtypeId, crf.vote " +
                "FROM chefRecomendationFood crf " +
                "WHERE crf.Date = ? " +
                "AND crf.vote = ( " +
                "    SELECT MAX(inner_crf.vote) " +
                "    FROM chefRecomendationFood inner_crf " +
                "    WHERE inner_crf.foodtypeId = crf.foodtypeId " +
                "    AND inner_crf.Date = crf.Date " +
                ")";

        try (Connection connection = Database.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(LocalDate.now()));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    long foodItemId = rs.getLong("foodItemId");
                    long foodtypeId = rs.getLong("foodtypeId");
                    int vote = rs.getInt("vote");

                    ChefRecomendationFood foodItem = new ChefRecomendationFood(foodItemId, foodtypeId, vote);
                    topFoodItems.add(foodItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topFoodItems;
    }

    public boolean entryExistsForToday() {
        String query = "SELECT COUNT(*) FROM chefRecomendationFood WHERE `Date` = CURDATE()";
        try (Connection connection = Database.getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}

