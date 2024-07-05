package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class ChefController {

    private ChefService chefService;
    private PrintWriter out;
    private BufferedReader in;

    public ChefController(PrintWriter out,BufferedReader in) {
        this.chefService = new ChefService();
        this.out = out;
        this.in = in;
    }

    public void processCommand(String inputLine) throws SQLException, IOException {
        String[] parts = inputLine.split("#");
        System.out.println("Input" + inputLine);
        System.out.println("Parts" +parts[0]);



        switch (parts[0]) {
            case "CHEF_FINALIZE_MENU":
                handleFinalizeCreationMenu(inputLine,out);
                break;
            case "CHEF_ROLL_OUT_MENU":
                handleRoleItemMenu(inputLine);
                break;
            case "CHEF_FINAL_ITEM":
                handleFinalizeMenu();
                break;
            case "CHEF_DISCARD_MENU":
                handleDiscardMenuList(out,in);
                break;
            case "CHEF_GET_ALL_FEEDBACK_BY_FOOD_NAME":
                handleDisplayDiscardMenuList(inputLine,out);
                break;
            default:
                out.println("Invalid CHEF command");
        }
    }

    private void handleDiscardMenuList(PrintWriter out, BufferedReader in) throws SQLException, IOException {
        chefService.discardItemList(out,in);
    }

    private void handleFinalizeCreationMenu(String arguments, PrintWriter out) {
        String response = chefService.handleFinalizeCreationMenu(arguments,out);
        this.out.println(response);
    }

    private void handleRoleItemMenu(String arguments) throws SQLException {
        chefService.handleRoleItemMenu(arguments,out);
    }

    private void handleFinalizeMenu() {
        chefService.handleFinalizeMenu(out);
    }
    private void handleDisplayDiscardMenuList(String inputLine, PrintWriter out){
        chefService.handleDisplayDiscardMenuList(inputLine,out);

    }
}
