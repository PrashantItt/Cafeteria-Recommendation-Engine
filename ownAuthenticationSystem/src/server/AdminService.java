package server;

import db.FoodItemDAO;
import db.FoodItemTypeDAO;
import db.UserDAO;
import model.FoodItem;
import model.FoodItemType;
import model.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminService {

    public String handleAddUser(String inputLine) throws SQLException {
        System.out.println(inputLine);
        String[] parts = inputLine.split(" ");
        System.out.println(parts.length);

        if (parts.length == 4) {
            String username = parts[1];
            String password = parts[2];
            Long roleId = Long.valueOf(parts[3]);
            try {
                User user = new User(username, password, roleId);
                UserDAO userDAO = new UserDAO();
                userDAO.addUser(user);
                return "User added successfully";
            } catch (NumberFormatException e) {
                return "Invalid role ID format";
            }
        } else {
            return "Invalid ADD_USER command";
        }
    }

    public String handleUpdateUser(String inputLine) {
        String[] parts = inputLine.split(" ");
        if (parts.length == 4) {
            String username = parts[1];
            String newPassword = parts[2];
            try {
                Long newRoleId = Long.valueOf(parts[3]);
                User user = new User(username, newPassword, newRoleId);
                UserDAO userDAO = new UserDAO();
                userDAO.updateUser(user);
                return "User updated successfully";
            } catch (NumberFormatException e) {
                return "Invalid role ID format";
            }
        } else {
            return "Invalid UPDATE_USER command";
        }
    }

    public String handleDeleteUser(String inputLine) {
        String[] parts = inputLine.split(" ");
        if (parts.length == 2) {
            String username = parts[1];
            UserDAO userDAO = new UserDAO();
            userDAO.deleteUser(username);
            return "User deleted successfully";
        } else {
            return "Invalid DELETE_USER command";
        }
    }

    public String handleAddMenuItem(String inputLine) {
        String[] parts = inputLine.split(" ");
        if (parts.length == 5) {
            String itemName = parts[1];
            try {
                double price = Double.parseDouble(parts[2]);
                boolean availabilityStatus = Boolean.parseBoolean(parts[3]);
                long foodItemTypeId = Long.parseLong(parts[4]);

                FoodItem foodItem = new FoodItem(itemName, price, availabilityStatus, foodItemTypeId);
                FoodItemDAO foodItemDAO = new FoodItemDAO();
                foodItemDAO.addFoodItem(foodItem);
                return "Menu item added successfully";
            } catch (NumberFormatException e) {
                return "Error adding menu item";
            }
        } else {
            return "Invalid ADD_MENU_ITEM command";
        }
    }

    public String handleUpdateMenuItem(String inputLine) {
        System.out.println(inputLine);
        String[] parts = inputLine.split(" ");
        System.out.println(parts.length);
        if (parts.length == 6) {
            try {
                long id = Long.parseLong(parts[1]);
                String itemName = parts[2];
                double price = Double.parseDouble(parts[3]);
                boolean availabilityStatus = Boolean.parseBoolean(parts[4]);
                long foodItemTypeId = Long.parseLong(parts[5]);

                FoodItem foodItem = new FoodItem(id, itemName, price, availabilityStatus, foodItemTypeId);
                FoodItemDAO foodItemDAO = new FoodItemDAO();
                boolean response = foodItemDAO.updateFoodItem(foodItem);
                if (response) {
                    return "Menu item updated successfully";
                } else {
                    return "Menu item not updated successfully";
                }

            } catch (NumberFormatException e) {
                return "Error updating menu item: " + e.getMessage();
            }
        }
         else {
            return "Invalid UPDATE_MENU_ITEM command";
        }
    }

    public String handleDeleteMenuItem(String inputLine) {
        String[] parts = inputLine.split(" ");
        if (parts.length == 2) {
            try {
                long foodItemId = Long.parseLong(parts[1]);
                FoodItemDAO foodItemDAO = new FoodItemDAO();
                foodItemDAO.deleteFoodItem(foodItemId);
                return "Menu item deleted successfully";
            } catch (NumberFormatException e) {
                return "Error deleting menu item: " + e.getMessage();
            }
        } else {
            return "Invalid DELETE_MENU_ITEM command";
        }
    }

    public String handleViewMenu() {
        FoodItemDAO foodItemDAO = new FoodItemDAO();
        FoodItemTypeDAO foodItemTypeDAO = new FoodItemTypeDAO();

        List<FoodItem> menu = foodItemDAO.getAllFoodItems();
        List<FoodItemType> foodItemTypes = foodItemTypeDAO.getAllFoodItemTypes();
        Map<Long, FoodItemType> foodItemTypeMap = new HashMap<>();
        for (FoodItemType type : foodItemTypes) {
            foodItemTypeMap.put(type.getFoodItemTypeId(), type);
        }

        StringBuilder menuString = new StringBuilder();
        menuString.append(String.format("%-10s %-20s %-10s %-10s %-20s\n",
                "ID", "Name", "Price", "Available", "Type"));

        for (FoodItem item : menu) {
            FoodItemType foodItemType = foodItemTypeMap.get(item.getFoodItemTypeId());
            menuString.append(String.format("%-10d %-20s %-10.2f %-10s %-20s\n",
                    item.getFoodItemId(),
                    item.getItemName(),
                    item.getPrice(),
                    item.isAvailabilityStatus() ? "Yes" : "No",
                    foodItemType != null ? foodItemType.getFoodItemType() : "N/A"));
        }
        menuString.append("END_OF_MENU");
        return menuString.toString();
    }
}
