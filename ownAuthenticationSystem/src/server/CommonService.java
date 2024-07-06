package server;

import db.FoodItemDAO;
import db.FoodItemTypeDAO;
import model.FoodItem;
import model.FoodItemType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonService {

    public String handleViewMenu() {
        System.out.println("Common Service");
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
