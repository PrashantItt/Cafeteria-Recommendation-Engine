package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

public class ClientTest {

    private static final int SERVER_PORT = 12345;
    private ServerSocket serverSocket;

    @Before
    public void setUp() throws IOException {
        serverSocket = new ServerSocket(SERVER_PORT);
        new Thread(() -> {
            try {
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String loginRequest = in.readLine();
                if ("LOGIN #testuser#password".equals(loginRequest)) {
                    out.println("LOGIN SUCCESSFUL");
                    out.println("1"); 
                    out.println("1001");
                } else {
                    out.println("LOGIN FAILED");
                }

                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Test
    public void testClientLoginSuccessful() {
        try (Socket socket = new Socket("localhost", SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String username = "testuser";
            String password = "password";

            out.println("LOGIN #" + username + "#" + password);
            String response = in.readLine();
            assertEquals("LOGIN SUCCESSFUL", response);

            String roleId = in.readLine();
            String userId = in.readLine();
            assertEquals("1", roleId);
            assertEquals("1001", userId);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testClientLoginFailed() {
        try (Socket socket = new Socket("localhost", SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String username = "wronguser";
            String password = "wrongpassword";

            out.println("LOGIN #" + username + "#" + password);
            String response = in.readLine();
            assertEquals("LOGIN FAILED", response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws IOException {
        serverSocket.close();
    }
}
