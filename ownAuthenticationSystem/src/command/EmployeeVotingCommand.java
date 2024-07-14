package command;

import service.EmployeeService;

import java.io.PrintWriter;

public class EmployeeVotingCommand implements Command {
    private EmployeeService employeeService;
    private PrintWriter out;

    public EmployeeVotingCommand(EmployeeService employeeService, PrintWriter out) {
        this.employeeService = employeeService;
        this.out = out;
    }

    @Override
    public void execute(String arguments) {
        employeeService.handleEmployeeVoting(arguments, out);
    }
}
