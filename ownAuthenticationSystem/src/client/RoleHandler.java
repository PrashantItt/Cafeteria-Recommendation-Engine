package client;

import java.io.IOException;

public interface RoleHandler {
    void handle(String userId) throws IOException;
}
