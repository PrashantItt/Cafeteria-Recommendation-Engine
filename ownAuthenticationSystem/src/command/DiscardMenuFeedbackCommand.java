package command;

import service.EmployeeService;

import java.io.PrintWriter;

public class DiscardMenuFeedbackCommand implements Command {
    private EmployeeService employeeService;
    private PrintWriter out;

    public DiscardMenuFeedbackCommand(EmployeeService employeeService, PrintWriter out) {
        this.employeeService = employeeService;
        this.out = out;
    }

    @Override
    public void execute(String arguments) {
        String response = employeeService.handleDiscardMenuFeedback(arguments);
        out.println(response);
    }
}
