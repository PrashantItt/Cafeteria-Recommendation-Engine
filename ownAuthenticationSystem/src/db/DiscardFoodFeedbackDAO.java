package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.DiscardFoodFeedback;
import db.Database;

public class DiscardFoodFeedbackDAO {


    public boolean insert(DiscardFoodFeedback feedback) {
        String query = "INSERT INTO discardfoodfeedback (foodName, userID, question1, question2, question3, feedbackDate) " + "VALUES (?, ?, ?, ?, ?, CURDATE())";

        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, feedback.getFoodName());
            preparedStatement.setInt(2, feedback.getUserID());
            preparedStatement.setString(3, feedback.getQuestion1());
            preparedStatement.setString(4, feedback.getQuestion2());
            preparedStatement.setString(5, feedback.getQuestion3());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public List<DiscardFoodFeedback> getFeedbacksByFoodName(String foodName) {
        List<DiscardFoodFeedback> feedbacks = new ArrayList<>();
        String query = "SELECT * FROM discardfoodfeedback WHERE foodName = ?";

        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, foodName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    DiscardFoodFeedback feedback = createFeedbackFromResultSet(resultSet);
                    feedbacks.add(feedback);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return feedbacks;
    }


    private DiscardFoodFeedback createFeedbackFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String foodName = resultSet.getString("foodName");
        int userID = resultSet.getInt("userID");
        String question1 = resultSet.getString("question1");
        String question2 = resultSet.getString("question2");
        String question3 = resultSet.getString("question3");
        Date feedbackDate = resultSet.getDate("feedbackDate");

        return new DiscardFoodFeedback(id, foodName, userID, question1, question2, question3, feedbackDate);
    }
}
