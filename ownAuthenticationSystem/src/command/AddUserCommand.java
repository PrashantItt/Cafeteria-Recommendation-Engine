package command;

import service.AdminService;

import java.io.PrintWriter;
import java.sql.SQLException;

public class AddUserCommand implements Command {
    private AdminService adminService;
    private PrintWriter out;

    public AddUserCommand(AdminService adminService, PrintWriter out) {
        this.adminService = adminService;
        this.out = out;
    }

    @Override
    public void execute(String arguments) {
        try {
            String response = adminService.handleAddUser(arguments);
            out.println(response);
        } catch (SQLException e) {
            out.println("Error adding user: " + e.getMessage());
        }
    }
}
