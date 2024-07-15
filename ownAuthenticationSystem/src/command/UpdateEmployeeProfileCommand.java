package command;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import service.EmployeeService;

public class UpdateEmployeeProfileCommand implements Command {
    private EmployeeService employeeService;
    private PrintWriter out;

    public UpdateEmployeeProfileCommand(EmployeeService employeeService, PrintWriter out) {
        this.employeeService = employeeService;
        this.out = out;
    }

    @Override
    public void execute(String arguments) throws SQLException, IOException {
        String response = employeeService.handleUpdateEmployeeProfile(arguments);
        out.println(response);

    }
}
