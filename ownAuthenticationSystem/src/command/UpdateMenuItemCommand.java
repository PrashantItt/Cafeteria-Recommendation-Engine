package command;

import service.AdminService;

import java.io.PrintWriter;

public class UpdateMenuItemCommand implements Command {
    private AdminService adminService;
    private PrintWriter out;

    public UpdateMenuItemCommand(AdminService adminService, PrintWriter out) {
        this.adminService = adminService;
        this.out = out;
    }

    @Override
    public void execute(String arguments) {
        String response = adminService.handleUpdateMenuItem(arguments);
        out.println(response);
    }
}
