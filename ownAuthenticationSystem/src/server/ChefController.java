package server;

import command.*;
import service.ChefService;
import service.CommonService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ChefController {
    private Map<String, Command> commandMap = new HashMap<>();
    private PrintWriter out;
    private BufferedReader in;

    public ChefController(PrintWriter out, BufferedReader in) {
        ChefService chefService = new ChefService();
        CommonService commonService = new CommonService();

        this.out = out;
        this.in = in;

        commandMap.put("CHEF_FINALIZE_MENU", new FinalizeCreationMenuCommand(chefService, out));
        commandMap.put("CHEF_ROLL_OUT_MENU", new RollOutMenuCommand(chefService, out));
        commandMap.put("CHEF_FINAL_ITEM", new FinalizeMenuCommand(chefService, out));
        commandMap.put("CHEF_DISCARD_MENU", new DiscardMenuListCommand(chefService, out, in));
        commandMap.put("CHEF_GET_ALL_FEEDBACK_BY_FOOD_NAME", new GetAllFeedbackByFoodNameCommand(chefService, out));
        commandMap.put("CHEF_VIEW_MENU", new ViewMenuCommand(commonService, out));
    }

    public void processCommand(String inputLine) throws SQLException, IOException {
        String[] parts = inputLine.split("#");
        Command command = commandMap.get(parts[0]);

        if (command != null) {
            command.execute(inputLine);
        } else {
            out.println("Invalid CHEF command");
        }
    }
}
