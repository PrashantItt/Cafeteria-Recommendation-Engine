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
        try (Connection connection = Database.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, foodName);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

