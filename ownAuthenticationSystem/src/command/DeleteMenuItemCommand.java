package command;

import service.AdminService;

import java.io.PrintWriter;

public class DeleteMenuItemCommand implements Command {
    private AdminService adminService;
    private PrintWriter out;

    public DeleteMenuItemCommand(AdminService adminService, PrintWriter out) {
        this.adminService = adminService;
        this.out = out;
    }

    @Override
    public void execute(String arguments) {
        String response = adminService.handleDeleteMenuItem(arguments);
        out.println(response);
    }
}
