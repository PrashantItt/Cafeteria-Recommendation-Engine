package db;

import model.Role;
import db.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {

    public boolean createRole(String roleName) {
        String query = "INSERT INTO Role (roleName) VALUES (?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, roleName);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateRole(long roleId, String newRoleName) {
        String query = "UPDATE Role SET roleName = ? WHERE roleId = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, newRoleName);
            preparedStatement.setLong(2, roleId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getRoleName(long roleId) {
        String query = "SELECT roleName FROM Role WHERE roleId = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, roleId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("roleName");
                } else {
                    throw new IllegalArgumentException("Invalid roleId, cannot find role with given roleId: " + roleId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public long getRoleId(String roleName) {
        String query = "SELECT roleId FROM Role WHERE roleName = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, roleName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("roleId");
                } else {
                    throw new IllegalArgumentException("Invalid roleName, cannot find role with given roleName: " + roleName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Role> getRoles() {
        List<Role> roles = new ArrayList<>();
        String query = "SELECT * FROM Role";
        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                long roleId = resultSet.getLong("roleId");
                String roleName = resultSet.getString("roleName");
                roles.add(new Role(roleId, roleName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }
}
