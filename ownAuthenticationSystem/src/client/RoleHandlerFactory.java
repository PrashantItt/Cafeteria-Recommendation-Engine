package client;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class RoleHandlerFactory {

    private static final Map<Integer, Function<HandlerParameters, RoleHandler>> roleHandlerMap = new HashMap<>();

    static {
        roleHandlerMap.put(1, params -> new AdminHandler(params.getSocket(), params.getOut(), params.getIn()));
        roleHandlerMap.put(2, params -> new ChefHandler(params.getSocket(), params.getOut(), params.getIn()));
        roleHandlerMap.put(3, params -> new EmployeeHandler(params.getSocket(), params.getOut(), params.getIn()));
    }

    public static RoleHandler getHandler(int roleId, Socket socket, PrintWriter out, BufferedReader in) {
        Function<HandlerParameters, RoleHandler> handlerFunction = roleHandlerMap.get(roleId);
        if (handlerFunction != null) {
            return handlerFunction.apply(new HandlerParameters(socket, out, in));
        } else {
            throw new IllegalArgumentException("Invalid role ID: " + roleId);
        }
    }
}
