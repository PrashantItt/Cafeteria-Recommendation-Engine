package server;

import command.*;
import service.AdminService;
import service.CommonService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AdminController {
    private Map<String, Command> commandMap = new HashMap<>();
    private PrintWriter out;

    public AdminController(PrintWriter out) {
        AdminService adminService = new AdminService();
        CommonService commonService = new CommonService();
        this.out = out;

        commandMap.put("ADMIN_ADD_USER", new AddUserCommand(adminService, out));
        commandMap.put("ADMIN_UPDATE_USER", new UpdateUserCommand(adminService, out));
        commandMap.put("ADMIN_DELETE_USER", new DeleteUserCommand(adminService, out));
        commandMap.put("ADMIN_ADD_MENU_ITEM", new AddMenuItemCommand(adminService, out));
        commandMap.put("ADMIN_UPDATE_MENU_ITEM", new UpdateMenuItemCommand(adminService, out));
        commandMap.put("ADMIN_DELETE_MENU_ITEM", new DeleteMenuItemCommand(adminService, out));
        commandMap.put("ADMIN_VIEW_MENU", new ViewMenuCommand(commonService, out));
    }

    public void processCommand(String inputLine) throws SQLException, IOException {
        String[] parts = inputLine.split("#");
        Command command = commandMap.get(parts[0]);

        if (command != null) {
            command.execute(inputLine);
        } else {
            out.println("Invalid ADMIN command");
        }
    }
}
