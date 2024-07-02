package db;

import model.Notification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import model.Notification;

import java.text.SimpleDateFormat;

public class NotificationDAO {

    public boolean sendNotification(Notification notification) {
        String query = "INSERT INTO notification (message, date) VALUES (?, ?)";
        try (Connection connection = Database.getConnection(); PreparedStatement prepStmt = connection.prepareStatement(query)) {
            java.sql.Date currentDate = new java.sql.Date(new Date().getTime());
            prepStmt.setString(1, notification.getMessage());
            prepStmt.setDate(2, currentDate);
            prepStmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public Notification getNotification(long notificationId) {
        String query = "SELECT * FROM notification WHERE notificationId = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement prepStmt = connection.prepareStatement(query)) {

            prepStmt.setLong(1, notificationId);
            try (ResultSet resultSet = prepStmt.executeQuery()) {
                if (resultSet.next()) {
                    String message = resultSet.getString("message");
                    Date date = resultSet.getDate("date");
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

    public List<Notification> getNotificationsByCurrentDate() {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM notification WHERE date = CURDATE()";

        try (Connection connection = Database.getConnection(); PreparedStatement prepStmt = connection.prepareStatement(query)) {

            try (ResultSet resultSet = prepStmt.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("notificationId");
                    String message = resultSet.getString("message");
                    Date date = resultSet.getDate("date");

                    Notification notification = new Notification(id, message, date);
                    notifications.add(notification);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notifications;
    }

}
