package command;

import service.AdminService;

import java.io.PrintWriter;

public class DeleteUserCommand implements Command {
    private AdminService adminService;
    private PrintWriter out;

    public DeleteUserCommand(AdminService adminService, PrintWriter out) {
        this.adminService = adminService;
        this.out = out;
    }

    @Override
    public void execute(String arguments) {
        String response = adminService.handleDeleteUser(arguments);
        out.println(response);
    }
}
