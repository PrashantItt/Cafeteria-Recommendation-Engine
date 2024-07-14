package command;

import java.io.PrintWriter;
import service.EmployeeService;
public class ViewRecommendedFoodCommand implements Command {
    private EmployeeService employeeService;
    private PrintWriter out;

    public ViewRecommendedFoodCommand(EmployeeService employeeService, PrintWriter out) {
        this.employeeService = employeeService;
        this.out = out;
    }

    @Override
    public void execute(String arguments) {
        String response = employeeService.handleViewRecommendedFood(arguments);
        out.println(response);
        out.println("END_OF_MENU");
    }
}
