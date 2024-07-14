package command;

import service.ChefService;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.sql.SQLException;

public class DiscardMenuListCommand implements Command {
    private ChefService chefService;
    private PrintWriter out;
    private BufferedReader in;

    public DiscardMenuListCommand(ChefService chefService, PrintWriter out, BufferedReader in) {
        this.chefService = chefService;
        this.out = out;
        this.in = in;
    }

    @Override
    public void execute(String arguments) throws SQLException, IOException {
        chefService.discardItemList(out, in);
    }
}
