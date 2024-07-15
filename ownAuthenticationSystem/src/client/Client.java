package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
   private static final String SERVER_ADDRESS = "localhost";

    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.print("Enter username: ");
            String username = stdIn.readLine();
            System.out.print("Enter password: ");
            String password = stdIn.readLine();

            out.println("LOGIN "+"#"+ username + "#" + password);
            String response = in.readLine();
            System.out.println("Server reply: " + response);


            if ("LOGIN SUCCESSFUL".equals(response)) {
                String roleId = in.readLine();
                String userId = in.readLine();
                System.out.println("User ID: " + userId);
                System.out.println("Connected to the server. Type messages to send:");
                RoleHandler roleHandler = RoleHandlerFactory.getHandler(Integer.valueOf(roleId), socket, out, in);
                roleHandler.handle(userId);


            }
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Server is not connected");
        }
    }
}

