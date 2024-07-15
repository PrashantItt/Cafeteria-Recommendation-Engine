package db;

import model.EmployeeProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmployeeProfileDAO {

    public boolean addEmployeeProfile(EmployeeProfile employeeProfile) {
        String sql = "INSERT INTO employeeProfile (userId, name, dietaryPreference, spiceLevel, cuisinePreference, sweetTooth) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, employeeProfile.getUserId());
            statement.setString(2, employeeProfile.getName());
            statement.setString(3, employeeProfile.getDietaryPreference());
            statement.setString(4, employeeProfile.getSpiceLevel());
            statement.setString(5, employeeProfile.getCuisinePreference());
            statement.setString(6, employeeProfile.getSweetTooth());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateEmployeeProfile(EmployeeProfile employeeProfile) {
        String sql = "UPDATE employeeProfile SET name = ?, dietaryPreference = ?, spiceLevel = ?, cuisinePreference = ?, sweetTooth = ? WHERE userId = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, employeeProfile.getName());
            statement.setString(2, employeeProfile.getDietaryPreference());
            statement.setString(3, employeeProfile.getSpiceLevel());
            statement.setString(4, employeeProfile.getCuisinePreference());
            statement.setString(5, employeeProfile.getSweetTooth());
            statement.setLong(6, employeeProfile.getUserId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public EmployeeProfile getEmployeeProfile(long userId) {
        String sql = "SELECT * FROM employeeProfile WHERE userId = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String dietaryPreference = resultSet.getString("dietaryPreference");
                    String spiceLevel = resultSet.getString("spiceLevel");
                    String cuisinePreference = resultSet.getString("cuisinePreference");
                    String sweetTooth = resultSet.getString("sweetTooth");

                    return new EmployeeProfile(userId, name, dietaryPreference, spiceLevel, cuisinePreference, sweetTooth);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isEmployeeProfileExists(long userId) {
        String sql = "SELECT 1 FROM employeeProfile WHERE userId = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

