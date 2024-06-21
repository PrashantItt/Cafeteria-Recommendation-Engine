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

public class ChefRecomendationFoodDAO {

    public boolean insert(ChefRecomendationFood recommendation) {
        String sql = "INSERT INTO chefRecomendationFood (foodItemId, foodtypeId, Date) VALUES (?, ?, CURDATE())";
        try (
                Connection connection = Database.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setLong(1, recommendation.getFoodItemId());
            stmt.setLong(2, recommendation.getFoodtypeId());
            int affectedRows = stmt.executeUpdate();
            return affectedRows == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to retrieve all recommendations
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

    // Method to retrieve recommendations for today's date
    public List<ChefRecomendationFood> getTommorowMenu() {
        List<ChefRecomendationFood> recommendations = new ArrayList<>();
        String sql = "SELECT * FROM chefRecomendationFood WHERE Date = ?";
        try (
                Connection connection = Database.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    long foodItemId = rs.getLong("foodItemId");
                    long foodtypeId = rs.getLong("foodtypeId");
                    Date date = rs.getDate("Date");

                    // Create ChefRecomendationFood object using constructor
                    ChefRecomendationFood recommendation = new ChefRecomendationFood( foodItemId, foodtypeId);

                    recommendations.add(recommendation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recommendations;
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

