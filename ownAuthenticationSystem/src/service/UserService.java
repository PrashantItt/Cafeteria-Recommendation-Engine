package service;

import db.UserDAO;
import model.User;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public boolean validateLogin(String username, String password) {
        return userDAO.validateUser(username, password);
    }

    public User getUserById(long userId) {
        return userDAO.getUserById((int) userId);
    }
    public Long getRoleId(String username, String password) {
        return userDAO.getRoleIdByUsernameAndPassword(username,password);
    }
}
