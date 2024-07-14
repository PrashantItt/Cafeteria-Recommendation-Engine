package command;

import service.ChefService;
import java.io.PrintWriter;

public class GetAllFeedbackByFoodNameCommand implements Command {
    private ChefService chefService;
    private PrintWriter out;

    public GetAllFeedbackByFoodNameCommand(ChefService chefService, PrintWriter out) {
        this.chefService = chefService;
        this.out = out;
    }

    @Override
    public void execute(String arguments) {
        chefService.handleDisplayDiscardMenuList(arguments, out);
    }
}
