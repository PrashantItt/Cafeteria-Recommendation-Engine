package server;

import db.*;
import model.*;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;


public class EmployeeService {

    public String handleSubmitFeedback(String inputLine) {
        String[] parts = inputLine.split("#");
        System.out.println("parts" +parts);
        if (parts.length == 6) {
            try {
                long userId = Long.parseLong(parts[1]);
                long menuItemId = Long.parseLong(parts[2]);
                int rating = Integer.parseInt(parts[3]);
                String comment = parts[4];
                System.out.println("comment" +comment);
                String date = parts[5];

                Feedback feedback = new Feedback(menuItemId, userId, comment, rating, date);

                FeedbackDAO feedbackDAO = new FeedbackDAO();
                boolean feedbackAdded = feedbackDAO.addFeedback(feedback);

                if (feedbackAdded) {
                    return "FEEDBACK_RECEIVED";
                } else {
                    return "ERROR_FEEDBACK_NOT_ADDED";
                }
            } catch (NumberFormatException e) {
                return "Error processing feedback: " + e.getMessage();
            }
        } else {
            return "Invalid SUBMIT_FEEDBACK command";
        }
    }

    public String handleDiscardMenuFeedback (String response) {
        String[] parts = response.split("#");
        if (parts.length == 6) {
            try {
                long userId = Long.parseLong(parts[1]);
                String foodName = parts[2];
                String dislikeAboutFood = parts[3];
                String likeAboutFood = parts[4];
                String momRecipe = parts[5];

                DiscardFoodFeedback discardFoodFeedback = new DiscardFoodFeedback(foodName, (int) userId, likeAboutFood, dislikeAboutFood, momRecipe);

                DiscardFoodFeedbackDAO discardFoodFeedbackDAO = new DiscardFoodFeedbackDAO();
                boolean feedbackAdded = discardFoodFeedbackDAO.insert(discardFoodFeedback);

                if (feedbackAdded) {
                    return "FEEDBACK_RECEIVED";
                } else {
                    return "ERROR_FEEDBACK_NOT_ADDED";
                }
            } catch (NumberFormatException e) {
                return "Error processing feedback: " + e.getMessage();
            }
        } else {
            return "Invalid SUBMIT_FEEDBACK command";
        }


    }

    public String handleViewRecommendedFood(String request, Long userId) {
        ChefRecomendationFoodDAO chefRecomendationFoodDAO = new ChefRecomendationFoodDAO();
        FoodItemTypeDAO foodItemTypeDAO = new FoodItemTypeDAO();
        FoodItemDAO foodItemDAO = new FoodItemDAO();
        EmployeeProfileDAO employeeProfileDAO = new EmployeeProfileDAO();

        List<FoodItem> foodItems = new ArrayList<>();
        List<FoodItem> recommendedFoodItems = chefRecomendationFoodDAO.getTommorowMenu();

        for (FoodItem foodItem : recommendedFoodItems) {
            FoodItem detailedFoodItem = foodItemDAO.getFoodItemById(foodItem.getFoodItemId());
            foodItems.add(detailedFoodItem);
        }

        List<FoodItemType> foodItemTypes = foodItemTypeDAO.getAllFoodItemTypes();
        Map<Long, FoodItemType> foodItemTypeMap = new HashMap<>();
        for (FoodItemType type : foodItemTypes) {
            foodItemTypeMap.put(type.getFoodItemTypeId(), type);
        }

        EmployeeProfile employeeProfile = employeeProfileDAO.getEmployeeProfile(userId);
        String dietaryPreference = employeeProfile.getDietaryPreference();
        String spiceLevel = employeeProfile.getSpiceLevel();
        String cuisinePreference = employeeProfile.getCuisinePreference();
        String sweetTooth = employeeProfile.getSweetTooth();

        foodItems = foodItems.stream()
                .sorted(Comparator.comparing((FoodItem item) -> {
                    FoodItemType foodItemType = foodItemTypeMap.get(item.getFoodItemTypeId());
                    return foodItemType != null ? foodItemType.getFoodItemType() : "";
                }).thenComparing(item -> {
                    int preferenceScore = 0;
                    if (item.getDietaryPreference().equalsIgnoreCase(dietaryPreference)) {
                        preferenceScore -= 4;
                    }
                    if (item.getSpiceLevel().equalsIgnoreCase(spiceLevel)) {
                        preferenceScore -= 3;
                    }
                    if (item.getCuisinePreference().equalsIgnoreCase(cuisinePreference)) {
                        preferenceScore -= 2;
                    }
                    if (item.getSweetTooth().equalsIgnoreCase(sweetTooth)) {
                        preferenceScore -= 1;
                    }
                    return preferenceScore;
                }))
                .collect(Collectors.toList());

        StringBuilder menuString = new StringBuilder();
        menuString.append(String.format("%-10s %-20s %-10s %-10s %-20s %-15s %-30s\n",
                "ID", "Name", "Price", "Available", "Type", "Avg Rating", "Sentiment Comment"));

        for (FoodItem item : foodItems) {
            FoodItemType foodItemType = foodItemTypeMap.get(item.getFoodItemTypeId());
            menuString.append(String.format("%-10d %-20s %-10.2f %-10s %-20s %-15d %-30s\n",
                    item.getFoodItemId(),
                    item.getItemName(),
                    item.getPrice(),
                    item.isAvailabilityStatus() ? "Yes" : "No",
                    foodItemType != null ? foodItemType.getFoodItemType() : "N/A",
                    item.getAvgRating(),
                    item.getSentimentComment()));
        }

        return menuString.toString();
    }


    public void handleEmployeeVoting(String response, PrintWriter out) {
        String[] parts = response.split("#", 2);
        if (parts.length == 2 && parts[0].equals("EMPLOYEE_VOTING_INPUT")) {
            String votingData = parts[1];
            Map<String, Long> votingMap = parseVotingData(votingData);

            processVotes(votingMap,out);
        } else {
            System.out.println("Invalid input or no voting data received.");
        }
    }

    private Map<String, Long> parseVotingData(String data) {
        Map<String, Long> votingMap = new HashMap<>();
        data = data.replaceAll("[{}]", "");

        String[] entries = data.split(", ");
        for (String entry : entries) {
            String[] keyValue = entry.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                Long value = Long.parseLong(keyValue[1].trim());
                votingMap.put(key, value);
            }
        }

        return votingMap;
    }

    private void processVotes(Map<String, Long> votingMap,PrintWriter out) {
        System.out.println("Processing Votes:");
        for (Map.Entry<String, Long> entry : votingMap.entrySet()) {
            System.out.println("Meal: " + entry.getKey() + ", Item ID: " + entry.getValue());

            try {
                FoodItemTypeDAO foodItemTypeDAO = new FoodItemTypeDAO();
                FoodItemType foodItemType = foodItemTypeDAO.getFoodItemTypeByName(entry.getKey());

                FoodItemDAO foodItemDAO = new FoodItemDAO();
                FoodItem foodItem = foodItemDAO.getFoodItemById(entry.getValue());

                if (foodItemType.getFoodItemTypeId() == foodItem.getFoodItemTypeId()) {
                    ChefRecomendationFoodDAO chefRecomendationFoodDAO = new ChefRecomendationFoodDAO();
                    boolean response = chefRecomendationFoodDAO.insertVote(entry.getValue());

                    if (!response) {
                        out.println("ERROR: Failed to insert vote for Item ID: " + entry.getValue());
                    } else {
                        out.println("SUCCESS: Vote inserted for Item ID: " + entry.getValue());
                    }
                } else {
                    out.println("ERROR: Mismatch between food item type IDs for Meal: " + entry.getKey() + ", Item ID: " + entry.getValue());
                }
            } catch (Exception e) {
                out.println("ERROR: An error occurred while processing vote for Meal: " + entry.getKey() + ", Item ID: " + entry.getValue());
                e.printStackTrace(out);
            }
        }
        out.println("END_OF_VOTING_PROCESS");
    }

    public String handleCreateEmployeeProfile(String request) {
        String[] parts = request.split("#");
        if (parts.length == 7) {
            try {
                long userId = Long.parseLong(parts[1]);
                String name = parts[2];
                String dietaryPreference = parts[3];
                String spiceLevel = parts[4];
                String cuisinePreference = parts[5];
                String sweetTooth = parts[6];

                EmployeeProfile newProfile = new EmployeeProfile(userId, name, dietaryPreference, spiceLevel, cuisinePreference, sweetTooth);
                EmployeeProfileDAO profileDAO = new EmployeeProfileDAO();

                boolean profileAdded = profileDAO.addEmployeeProfile(newProfile);

                if (profileAdded) {
                    return "Employee profile created successfully.";
                } else {
                    return "Failed to create employee profile.";
                }
            } catch (NumberFormatException e) {
                System.err.println("Error parsing input: " + e.getMessage());
            }
        }
        return "Invalid profile creation request format.";

    }

    public String handleUpdateEmployeeProfile(String request) {
        String[] parts = request.split("#");
        if (parts.length == 7) {
            try {
                long userId = Long.parseLong(parts[1]);
                String name = parts[2];
                String dietaryPreference = parts[3];
                String spiceLevel = parts[4];
                String cuisinePreference = parts[5];
                String sweetTooth = parts[6];

                EmployeeProfile updatedProfile = new EmployeeProfile(userId, name, dietaryPreference, spiceLevel, cuisinePreference, sweetTooth);
                EmployeeProfileDAO profileDAO = new EmployeeProfileDAO();

                boolean profileUpdated = profileDAO.updateEmployeeProfile(updatedProfile);

                if (profileUpdated) {
                    return "Employee profile updated successfully.";
                } else {
                    return "Failed to update employee profile.";
                }
            } catch (NumberFormatException e) {
                System.err.println("Error parsing input: " + e.getMessage());
            }
        }
        return "Invalid profile update request format.";
    }

    public String handleViewNotification(String request) {
        String[] parts = request.split("#");

        if (parts.length == 1) {
            NotificationDAO notificationDAO = new NotificationDAO();
            List<Notification> notificationList = notificationDAO.getNotificationsByCurrentDate();

            StringBuilder notificationString = new StringBuilder();
            notificationString.append(String.format("%-15s %-50s %-20s\n", "Notification ID", "Message", "Date"));

            for (Notification notification : notificationList) {
                notificationString.append(String.format("%-15d %-50s %-20s\n",
                        notification.getNotificationId(),
                        notification.getMessage(),
                        notification.getDate().toString()));
            }
            notificationString.append("END_OF_NOTIFICATIONS");
            return notificationString.toString();
        } else {
            return "Invalid View Notification request format.";
        }
    }


}
