package db;

import model.ScheduledMenu;
import db.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduledMenuDAO {

    public List<ScheduledMenu> getAllScheduledMenus() {
        List<ScheduledMenu> scheduledMenus = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM scheduledMenu")) {

            while (resultSet.next()) {
                long scheduledMenuId = resultSet.getLong("scheduledMenuId");
                String date = resultSet.getString("date");
                long menuId = resultSet.getLong("menuId");
                scheduledMenus.add(new ScheduledMenu(scheduledMenuId, date, menuId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scheduledMenus;
    }

    public ScheduledMenu getScheduledMenu(long scheduledMenuId) {
        ScheduledMenu scheduledMenu = null;
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM scheduledMenu WHERE scheduledMenuId = ?")) {

            preparedStatement.setLong(1, scheduledMenuId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String date = resultSet.getString("date");
                    long menuId = resultSet.getLong("menuId");
                    scheduledMenu = new ScheduledMenu(scheduledMenuId, date, menuId);
                } else {
                    throw new IllegalArgumentException("Invalid scheduledMenuId, cannot find scheduledMenu with given scheduledMenuId: " + scheduledMenuId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scheduledMenu;
    }

    public boolean addScheduledMenu(ScheduledMenu scheduledMenu) {
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO scheduledMenu (date, menuId) VALUES (?, ?)")) {

            preparedStatement.setString(1, scheduledMenu.getDate());
            preparedStatement.setLong(2, scheduledMenu.getMenuId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteScheduledMenu(long scheduledMenuId) {
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM scheduledMenu WHERE scheduledMenuId = ?")) {

            preparedStatement.setLong(1, scheduledMenuId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateScheduledMenu(ScheduledMenu scheduledMenu) {
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE scheduledMenu SET date = ?, menuId = ? WHERE scheduledMenuId = ?")) {

            preparedStatement.setString(1, scheduledMenu.getDate());
            preparedStatement.setLong(2, scheduledMenu.getMenuId());
            preparedStatement.setLong(3, scheduledMenu.getScheduledMenuId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ScheduledMenu getScheduledMenuByDate(String date) {
        ScheduledMenu scheduledMenu = null;
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM scheduledMenu WHERE date = ?")) {

            preparedStatement.setString(1, date);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    long scheduledMenuId = resultSet.getLong("scheduledMenuId");
                    long menuId = resultSet.getLong("menuId");
                    scheduledMenu = new ScheduledMenu(scheduledMenuId, date, menuId);
                } else {
                    throw new IllegalArgumentException("Invalid date, cannot find scheduledMenu with given date: " + date);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scheduledMenu;
    }
}
