package command;

import service.EmployeeService;

import java.io.PrintWriter;

public class CreateEmployeeProfileCommand implements Command {
    private EmployeeService employeeService;
    private PrintWriter out;

    public CreateEmployeeProfileCommand(EmployeeService employeeService, PrintWriter out) {
        this.employeeService = employeeService;
        this.out = out;
    }

    @Override
    public void execute(String arguments) {
        String response = employeeService.handleCreateEmployeeProfile(arguments);
        out.println(response);
    }
}