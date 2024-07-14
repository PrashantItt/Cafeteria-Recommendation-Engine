package command;

import service.AdminService;

import java.io.PrintWriter;

public class UpdateUserCommand implements Command {
    private AdminService adminService;
    private PrintWriter out;

    public UpdateUserCommand(AdminService adminService, PrintWriter out) {
        this.adminService = adminService;
        this.out = out;
    }

    @Override
    public void execute(String arguments) {
        String response = adminService.handleUpdateUser(arguments);
        out.println(response);
    }
}