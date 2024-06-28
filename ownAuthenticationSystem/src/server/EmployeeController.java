package server;

import java.io.PrintWriter;

public class EmployeeController {

    private EmployeeService employeeService;
    private PrintWriter out;

    public EmployeeController(PrintWriter out) {
        this.employeeService = new EmployeeService();
        this.out = out;
    }

    public void processCommand(String inputLine) {
        String[] parts = inputLine.split("#");
        System.out.println(parts.length);
        String command = parts[0];
        System.out.println(command);
        String arguments = inputLine.substring(command.length()).trim();
        System.out.println("arguments" +arguments);
        switch (command) {
            case "EMPLOYEE_SUBMIT_FEEDBACK":
                handleSubmitFeedback(inputLine);
                break;
            case "EMPLOYEE_SUBMIT_DISCARD_FEEDBACK" :
                handleDiscardMenuFeedback(inputLine);
                break;
            case "EMPLOYEE_VIEW_TOMORROW_FOOD":
                handleViewRecommendedFood(inputLine);
                break;
            case "EMPLOYEE_VOTING_INPUT":
                handleEmployeeVoting(inputLine);

            default:
                out.println("Invalid EMPLOYEE command");
        }
    }

    private void handleSubmitFeedback(String arguments) {
        String response = employeeService.handleSubmitFeedback(arguments);
        out.println(response);
    }
    private void handleDiscardMenuFeedback(String request) {
        System.out.println("Employee Discard item");
        String response = employeeService.handleDiscardMenuFeedback(request);
        out.println(response);
    }
    private void handleViewRecommendedFood(String request) {
        String response = employeeService.handleViewRecommendedFood(request);
        System.out.println("response"+response);
        out.println(response);
        out.println("END_OF_MENU");
    }
    private void handleEmployeeVoting(String request) {
        employeeService.handleEmployeeVoting(request);
    }
}
