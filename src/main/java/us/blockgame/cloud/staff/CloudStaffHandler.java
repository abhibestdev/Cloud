package us.blockgame.cloud.staff;

import us.blockgame.cloud.staff.command.SeenCommand;
import us.blockgame.fabric.FabricPlugin;
import us.blockgame.fabric.command.FabricCommandHandler;

public class CloudStaffHandler {

    public CloudStaffHandler() {
        //Register commands
        FabricCommandHandler fabricCommandHandler = FabricPlugin.getInstance().getFabricCommandHandler();
        fabricCommandHandler.registerCommand(new SeenCommand());
    }

}
