package service;

import db.FoodItemDAO;
import model.FoodItem;
import java.util.List;

public class FoodItemService {
    private final FoodItemDAO foodItemDAO = new FoodItemDAO();

    public List<FoodItem> getAllFoodItems() {
        return foodItemDAO.getAllFoodItems();
    }
}