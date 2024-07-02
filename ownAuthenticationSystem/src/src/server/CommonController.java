package server;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class CommonController {
    private CommonService commonService;
    private PrintWriter out;
    private BufferedReader in;

    public CommonController(PrintWriter out, BufferedReader in) {
        this.commonService = new CommonService();
        this.out = out;
        this.in = in;
    }

    public void processCommand(String inputLine) {
        String[] parts = inputLine.split(" ");
        System.out.println(parts[0]);

        switch (parts[0]) {
            case "COMMON_VIEW_MENU":
                handleViewMenu();
                break;
            default:
                out.println("Invalid CHEF command");
        }

    }

    private void handleViewMenu() {
        System.out.println("Commomn Controller is calling");
        String response = commonService.handleViewMenu();
        out.println(response);
    }
}