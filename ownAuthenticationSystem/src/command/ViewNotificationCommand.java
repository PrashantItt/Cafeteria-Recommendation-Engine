package command;

import service.EmployeeService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;


public class ViewNotificationCommand implements Command {
    private EmployeeService employeeService;
    private PrintWriter out;

    public ViewNotificationCommand(EmployeeService employeeService, PrintWriter out) {
        this.employeeService = employeeService;
        this.out = out;
    }


    @Override
    public void execute(String arguments) throws SQLException, IOException {
        String response = employeeService.handleViewNotification(arguments);
        out.println(response);
    }
}
