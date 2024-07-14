package command;

import service.AdminService;

import java.io.PrintWriter;

public class AddMenuItemCommand implements Command {
    private AdminService adminService;
    private PrintWriter out;

    public AddMenuItemCommand(AdminService adminService, PrintWriter out) {
        this.adminService = adminService;
        this.out = out;
    }

    @Override
    public void execute(String arguments) {
        String response = adminService.handleAddMenuItem(arguments);
        out.println(response);
    }
}