package client;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

class HandlerParameters {
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;

    public HandlerParameters(Socket socket, PrintWriter out, BufferedReader in) {
        this.socket = socket;
        this.out = out;
        this.in = in;
    }

    public Socket getSocket() {
        return socket;
    }

    public PrintWriter getOut() {
        return out;
    }

    public BufferedReader getIn() {
        return in;
    }
}
