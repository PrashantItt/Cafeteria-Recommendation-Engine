package db;

import model.UserActivity;
import db.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserActivityDAO {

    public List<UserActivity> getAllUserActivities() {
        List<UserActivity> userActivities = new ArrayList<>();
        try (Connection connection = Database.getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT * FROM UserActivity")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                long userId = resultSet.getLong("userId");
                Timestamp loginTime = resultSet.getTimestamp("loginTime");
                Timestamp logOutTime = resultSet.getTimestamp("logOutTime");
                userActivities.add(new UserActivity(id, userId, loginTime, logOutTime));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userActivities;
    }

    public UserActivity getUserActivityById(int id) {
        UserActivity userActivity = null;
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM UserActivity WHERE id = ?")) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    long userId = resultSet.getLong("userId");
                    Timestamp loginTime = resultSet.getTimestamp("loginTime");
                    Timestamp logOutTime = resultSet.getTimestamp("logOutTime");
                    userActivity = new UserActivity(id, userId, loginTime, logOutTime);
                } else {
                    throw new IllegalArgumentException("Invalid id, cannot find user activity with given id: " + id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userActivity;
    }

    public boolean addUserActivity(UserActivity userActivity) {
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO UserActivity (userId, loginTime) VALUES (?, CURRENT_TIMESTAMP)")) {

            preparedStatement.setLong(1, userActivity.getUserId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateUserActivity(Long userId) {
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("UPDATE UserActivity SET logOutTime = CURRENT_TIMESTAMP WHERE userId = ? AND logOutTime IS NULL")) {
            preparedStatement.setLong(1, userId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
