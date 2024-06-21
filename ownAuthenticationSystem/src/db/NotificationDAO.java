package db;

import model.Notification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    public boolean sendNotification(Notification notification) {
        String query = "INSERT INTO Notifications (message) VALUES (?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement prepStmt = connection.prepareStatement(query)) {

            prepStmt.setString(1, notification.getMessage());
            prepStmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteNotification(long notificationId) {
        String query = "DELETE FROM Notifications WHERE notificationId = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement prepStmt = connection.prepareStatement(query)) {

            prepStmt.setLong(1, notificationId);
            prepStmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateNotification(Notification notification) {
        String query = "UPDATE Notifications SET message = ? WHERE notificationId = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement prepStmt = connection.prepareStatement(query)) {

            prepStmt.setString(1, notification.getMessage());
            prepStmt.setLong(2, notification.getNotificationId());
            prepStmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Notification getNotification(long notificationId) {
        String query = "SELECT * FROM Notifications WHERE notificationId = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement prepStmt = connection.prepareStatement(query)) {

            prepStmt.setLong(1, notificationId);
            try (ResultSet resultSet = prepStmt.executeQuery()) {
                if (resultSet.next()) {
                    String message = resultSet.getString("message");
                    String date = resultSet.getString("date");
                    return new Notification(notificationId, message, date);
                } else {
                    throw new IllegalArgumentException("No Notification with Id: " + notificationId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Notification> getAllNotificationsAfter(long notificationId) {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM Notifications WHERE notificationId > ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement prepStmt = connection.prepareStatement(query)) {

            prepStmt.setLong(1, notificationId);
            try (ResultSet resultSet = prepStmt.executeQuery()) {
                while (resultSet.next()) {
                    long id = resultSet.getLong("notificationId");
                    String message = resultSet.getString("message");
                    String date = resultSet.getString("date");
                    notifications.add(new Notification(id, message, date));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }
}
