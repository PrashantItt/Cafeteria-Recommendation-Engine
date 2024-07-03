package server;

import java.io.PrintWriter;
import java.sql.SQLException;

public class AdminController {

    private AdminService adminService;
    private PrintWriter out;

    public AdminController(PrintWriter out) {
        this.adminService = new AdminService();
        this.out = out;
    }

    public void processCommand(String inputLine) {
        String[] parts = inputLine.split("#");

        switch (parts[0]) {
            case "ADMIN_ADD_USER":
                handleAddUser(inputLine);
                break;
            case "ADMIN_UPDATE_USER":
                handleUpdateUser(inputLine);
                break;
            case "ADMIN_DELETE_USER":
                handleDeleteUser(inputLine);
                break;
            case "ADMIN_ADD_MENU_ITEM":
                handleAddMenuItem(inputLine);
                break;
            case "ADMIN_UPDATE_MENU_ITEM":
                handleUpdateMenuItem(inputLine);
                break;
            case "ADMIN_DELETE_MENU_ITEM":
                handleDeleteMenuItem(inputLine);
                break;
            case "COMMON_VIEW_MENU":
                handleViewMenu();
                break;
            default:
                out.println("Invalid ADMIN command");
        }
    }

    private void handleAddUser(String arguments) {
        try {
            String response = adminService.handleAddUser(arguments);
            out.println(response);
        } catch (SQLException e) {
            out.println("Error adding user: " + e.getMessage());
        }
    }

    private void handleUpdateUser(String arguments) {
        String response = adminService.handleUpdateUser(arguments);
        out.println(response);
    }

    private void handleDeleteUser(String arguments) {
        String response = adminService.handleDeleteUser(arguments);
        out.println(response);
    }

    private void handleAddMenuItem(String arguments) {
        String response = adminService.handleAddMenuItem(arguments);
        out.println(response);
    }

    private void handleUpdateMenuItem(String arguments) {
        String response = adminService.handleUpdateMenuItem(arguments);
        out.println(response);
    }

    private void handleDeleteMenuItem(String arguments) {
        String response = adminService.handleDeleteMenuItem(arguments);
        out.println(response);
    }

    private void handleViewMenu() {
        String response = adminService.handleViewMenu();
        out.println(response);
    }
}
