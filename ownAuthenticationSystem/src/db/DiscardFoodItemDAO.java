package db;

import model.DiscardFoodItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DiscardFoodItemDAO {

    public boolean addDiscardFoodItemByName(String foodName) {
        String sql = "INSERT INTO discardedfoods (foodName, discardDate) VALUES (?, CURDATE())";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, foodName);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<DiscardFoodItem> getAllDiscardFoodItems() {
        List<DiscardFoodItem> discardFoodItems = new ArrayList<>();
        String sql = "SELECT * FROM discardedfoods";
        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                long id = resultSet.getLong("Id");
                String foodName = resultSet.getString("foodName");
                String likeAboutFood = resultSet.getString("likeAboutFood");
                String dislikeAboutFood = resultSet.getString("dislikeAboutFood");
                String momRecipe = resultSet.getString("momRecipe");
                long userId = resultSet.getLong("userId");
                Date  date = resultSet.getDate("discardDate");

                discardFoodItems.add(new DiscardFoodItem(id, foodName, likeAboutFood, dislikeAboutFood, momRecipe,userId,date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discardFoodItems;
    }



    public List<DiscardFoodItem> getDiscardFoodItemsByName(String foodName) {
        List<DiscardFoodItem> discardFoodItems = new ArrayList<>();
        String sql = "SELECT * FROM discardedfoods WHERE foodName = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, foodName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    long id = resultSet.getLong("Id");
                    String likeAboutFood = resultSet.getString("likeAboutFood");
                    String dislikeAboutFood = resultSet.getString("dislikeAboutFood");
                    String momRecipe = resultSet.getString("momRecipe");
                    long userId = resultSet.getLong("userId");
                    Date discardDate = resultSet.getDate("discardDate");

                    discardFoodItems.add(new DiscardFoodItem(id, foodName, likeAboutFood, dislikeAboutFood, momRecipe, userId, discardDate));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discardFoodItems;
    }


}

