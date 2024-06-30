package db;


import model.Feedback;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class FeedbackDAO {

    public boolean addFeedback(Feedback feedback) {
        String sql = "INSERT INTO Feedback (userId,menuItemId, rating, comment, date) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, feedback.getUserId());
            statement.setLong(2, feedback.getMenuItemId());
            statement.setInt(3, feedback.getRating());
            statement.setString(4, feedback.getComment());
            statement.setString(5, feedback.getDate());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<Feedback> getAllFeedback() {
        List<Feedback> feedbacks = new ArrayList<>();
        String sql = "SELECT * FROM Feedback";
        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                long feedbackId = resultSet.getLong("Id");
                long userId = resultSet.getLong("userId");
                long menuItemId = resultSet.getLong("menuItemId");
                int rating = resultSet.getInt("rating");
                String comment = resultSet.getString("comment");
                String date = resultSet.getString("date");

                feedbacks.add(new Feedback( feedbackId,menuItemId, userId, comment, rating,date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbacks;
    }


}
