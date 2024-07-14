package command;

import service.CommonService;
import java.io.PrintWriter;

public class ViewMenuCommand implements Command {
    private CommonService commonService;
    private PrintWriter out;

    public ViewMenuCommand(CommonService commonService, PrintWriter out) {
        this.commonService = commonService;
        this.out = out;
    }

    @Override
    public void execute(String arguments) {
        String response = commonService.handleViewMenu();
        out.println(response);
    }
}
