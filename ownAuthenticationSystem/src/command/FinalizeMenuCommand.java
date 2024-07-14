package command;

import service.ChefService;

import java.io.PrintWriter;

public class FinalizeMenuCommand implements Command {
    private ChefService chefService;
    private PrintWriter out;

    public FinalizeMenuCommand(ChefService chefService, PrintWriter out) {
        this.chefService = chefService;
        this.out = out;
    }

    @Override
    public void execute(String arguments) {
        chefService.handleFinalizeMenu(out);
    }
}
