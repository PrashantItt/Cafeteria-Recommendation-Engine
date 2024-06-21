package client;

import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RoleHandlerFactory {
    public static RoleHandler getHandler(int roleId, Socket socket, PrintWriter out, BufferedReader in) {
        switch (roleId) {
            case 1:
                return new AdminHandler(socket, out, in);
            case 2:
                return new ChefHandler(socket, out, in);
            case 3:
                return new EmployeeHandler(socket, out, in);
            default:
                throw new IllegalArgumentException("Invalid role ID: " + roleId);
        }
    }
}

/*
public class RoleHandlerFactory {
    private static final Map<Role, RoleHandler> handlers = new HashMap<>();

    static {
        handlers.put(Role.ADMIN, new AdminHandler());
        handlers.put(Role.CHEF, new ChefHandler());
        handlers.put(Role.EMPLOYEE, new EmployeeHandler());
    }

    public static RoleHandler getHandler(Role role) {
        if (!handlers.containsKey(role)) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
        return handlers.get(role);
    }
}
 */
