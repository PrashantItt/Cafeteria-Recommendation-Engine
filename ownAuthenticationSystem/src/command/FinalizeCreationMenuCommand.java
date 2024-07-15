package command;

import service.ChefService;

import java.io.PrintWriter;

public class FinalizeCreationMenuCommand implements Command {
    private ChefService chefService;
    private PrintWriter out;

    public FinalizeCreationMenuCommand(ChefService chefService, PrintWriter out) {
        this.chefService = chefService;
        this.out = out;
    }

    @Override
    public void execute(String arguments) {
        String response = chefService.handleFinalizeCreationMenu(arguments, out);
        out.println(response);
    }
}

