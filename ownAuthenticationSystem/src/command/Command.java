package command;

import java.io.IOException;
import java.sql.SQLException;

public interface Command {
    void execute(String arguments) throws SQLException, IOException;
}
