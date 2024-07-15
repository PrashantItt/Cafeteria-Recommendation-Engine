package command;

import service.EmployeeService;

import java.io.PrintWriter;

public class SubmitFeedbackCommand implements Command {
    private EmployeeService employeeService;
    private PrintWriter out;

    public SubmitFeedbackCommand(EmployeeService employeeService, PrintWriter out) {
        this.employeeService = employeeService;
        this.out = out;
    }

    @Override
    public void execute(String arguments) {
        String response = employeeService.handleSubmitFeedback(arguments);
        out.println(response);
    }
}
