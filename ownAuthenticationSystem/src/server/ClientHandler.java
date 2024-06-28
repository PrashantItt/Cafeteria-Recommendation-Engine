package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import service.UserService;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private AdminController adminController;
    private ChefController chefController;
    private EmployeeController employeeController;
    private CommonController commonController;
    private final UserService userService;


    public ClientHandler(Socket clientSocket) throws SQLException {
        this.clientSocket = clientSocket;
        this.userService = new UserService();
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.startsWith("LOGIN ")) {
                    handleLogin(inputLine, out);
                } else if (inputLine.startsWith("ADMIN_")) {
                    if (adminController == null) {
                        adminController = new AdminController(out);
                    }
                    adminController.processCommand(inputLine);
                } else if (inputLine.startsWith("CHEF_")) {
                    if (chefController == null) {
                        chefController = new ChefController(out,in);
                    }
                    chefController.processCommand(inputLine);
                } else if (inputLine.startsWith("EMPLOYEE_")) {
                    if (employeeController == null) {
                        employeeController = new EmployeeController(out);
                    }
                    employeeController.processCommand(inputLine);
                } else if (inputLine.startsWith("COMMON_")) {
                    if (commonController == null) {
                        commonController = new CommonController(out,in);
                    }
                    commonController.processCommand(inputLine);
                } else {
                    out.println("Unknown command");
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Client disconnected");
        }
    }

    private void handleLogin(String inputLine, PrintWriter out) throws SQLException {
        String[] parts = inputLine.split(" ");
        if (parts.length == 3) {
            String username = parts[1];
            String password = parts[2];
            if (userService.validateLogin(username, password)) {
                out.println("LOGIN SUCCESSFUL");
                Long roleId = userService.getRoleId(username, password);
                out.println(roleId);
            } else {
                out.println("LOGIN FAILED");
            }
        } else {
            out.println("Invalid LOGIN command");
        }
    }
}
