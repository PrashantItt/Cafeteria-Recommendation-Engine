package server;

import java.io.PrintWriter;

public class EmployeeController {

    private EmployeeService employeeService;
    private PrintWriter out;

    public EmployeeController(PrintWriter out) {
        this.employeeService = new EmployeeService();
        this.out = out;
    }

    public void processCommand(String inputLine,long userId) {
        String[] parts = inputLine.split("#");
        String command = parts[0];
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
                handleViewRecommendedFood(inputLine,userId);
                break;
            case "EMPLOYEE_VOTING_INPUT":
                handleEmployeeVoting(inputLine,out);
                break;
            case "EMPLOYEE_CREATE_PROFILE":
                handleCreateEmployeeProfile(inputLine);
                break;
            case "EMPLOYEE_UPDATE_PROFILE":
                handleUpdateEmployeeProfile(inputLine);
                break;
            case "EMPLOYEE_VIEW_NOTIFICATION":
                handleViewNotification(inputLine);
                break;
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
    private void handleViewRecommendedFood(String request,Long userId) {
        String response = employeeService.handleViewRecommendedFood(request,userId);
        System.out.println("response"+response);
        out.println(response);
        out.println("END_OF_MENU");
    }
    private void handleEmployeeVoting(String request,PrintWriter out) {
        employeeService.handleEmployeeVoting(request,out);
    }
    private void handleCreateEmployeeProfile(String request) {
        String response = employeeService.handleCreateEmployeeProfile(request);
        out.println(response);
    }

    private void handleUpdateEmployeeProfile(String request) {
        String response = employeeService.handleUpdateEmployeeProfile(request);
        out.println(response);
    }
    private void handleViewNotification(String request) {
        String response =employeeService.handleViewNotification(request);
        out.println(response);
    }
}
