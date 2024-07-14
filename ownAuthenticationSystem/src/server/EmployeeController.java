package server;

import command.*;
import service.CommonService;
import service.EmployeeService;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class EmployeeController {
    private Map<String, Command> commandMap = new HashMap<>();
    private PrintWriter out;

    public EmployeeController(PrintWriter out) {
        EmployeeService employeeService = new EmployeeService();
        CommonService commonService = new CommonService();
        this.out = out;

        commandMap.put("EMPLOYEE_SUBMIT_FEEDBACK", new SubmitFeedbackCommand(employeeService, out));
        commandMap.put("EMPLOYEE_SUBMIT_DISCARD_FEEDBACK", new DiscardMenuFeedbackCommand(employeeService, out));
        commandMap.put("EMPLOYEE_VIEW_TOMORROW_FOOD", new ViewRecommendedFoodCommand(employeeService, out));
        commandMap.put("EMPLOYEE_VOTING_INPUT", new EmployeeVotingCommand(employeeService, out));
        commandMap.put("EMPLOYEE_CREATE_PROFILE", new CreateEmployeeProfileCommand(employeeService, out));
        commandMap.put("EMPLOYEE_UPDATE_PROFILE", new UpdateEmployeeProfileCommand(employeeService, out));
        commandMap.put("EMPLOYEE_VIEW_NOTIFICATION", new ViewNotificationCommand(employeeService, out));
        commandMap.put("EMPLOYEE_VIEW_MENU", new ViewMenuCommand(commonService, out));
        commandMap.put("EMPLOYEE_DISCARD_MENU",new ViewDiscardMenuItems(employeeService, out));
    }

    public void processCommand(String inputLine) {
        String[] parts = inputLine.split("#");
        String command = parts[0];
        System.out.println("command"+command);
        String arguments = inputLine.substring(command.length()).trim();
        System.out.println("arguments"+arguments);
        Command commandObj = commandMap.get(command);

        if (commandObj != null) {
            try {
                commandObj.execute(arguments);
            } catch (Exception e) {
                out.println("Error processing command: " + e.getMessage());
            }
        } else {
            out.println("Invalid EMPLOYEE command");
        }
    }
}
