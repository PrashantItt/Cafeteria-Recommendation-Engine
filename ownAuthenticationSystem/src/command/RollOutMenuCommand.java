package command;

import service.ChefService;

import java.io.PrintWriter;
import java.sql.SQLException;

public class RollOutMenuCommand implements Command {
    private ChefService chefService;
    private PrintWriter out;

    public RollOutMenuCommand(ChefService chefService, PrintWriter out) {
        this.chefService = chefService;
        this.out = out;
    }

    @Override
    public void execute(String arguments) throws SQLException {
        chefService.handleRoleItemMenu(arguments, out);
    }
}