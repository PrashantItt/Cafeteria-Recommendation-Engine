package db;

import model.User;
import db.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public User getUserById(int userId) {
        User user = null;
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM User WHERE id = ?")) {

            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    long roleId = resultSet.getLong("roleId");
                    String password = resultSet.getString("password");
                    user = new User(userId, name, password, roleId);
                } else {
                    throw new IllegalArgumentException("Invalid userId, cannot find user with given userId: " + userId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean addUser(User user) {
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO User (name, roleId, password) VALUES (?, ?, ?)")) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setLong(2, user.getRoleId());
            preparedStatement.setString(3, user.getPassword());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateUser(User user) {
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("UPDATE User SET roleId = ?, password = ? WHERE name = ?")) {
            preparedStatement.setLong(1, user.getRoleId());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteUser(String UserName) {
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM User WHERE name = ?")) {

            preparedStatement.setString(1, UserName);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Long getRoleIdByUsernameAndPassword(String username, String password) {
        Long roleId = null;
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("SELECT roleId FROM User WHERE name = ? AND password = ?")) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    roleId = resultSet.getLong("roleId");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roleId;
    }

    public Long getUserIdByUsernameAndPassword(String username, String password) {
        Long userId = null;
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("SELECT userId FROM User WHERE name = ? AND password = ?")) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    userId = resultSet.getLong("userId");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }

    public boolean validateUser(String username, String password) {
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM User WHERE name = ? AND password = ?")) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}

