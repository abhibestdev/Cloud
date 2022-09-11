package us.blockgame.cloud.broadcaster;

import lombok.Getter;
import us.blockgame.cloud.CloudPlugin;
import us.blockgame.cloud.broadcaster.command.BroadcastCommand;
import us.blockgame.cloud.broadcaster.task.CloudBroadcasterTask;
import us.blockgame.fabric.FabricPlugin;
import us.blockgame.fabric.command.FabricCommandHandler;

import java.util.List;

public class CloudBroadcasterHandler {

    @Getter private List<String> broadcasts;

    public CloudBroadcasterHandler() {
        //Register commands
        FabricCommandHandler fabricCommandHandler = FabricPlugin.getInstance().getFabricCommandHandler();
        fabricCommandHandler.registerCommand(new BroadcastCommand());

        broadcasts = CloudPlugin.getInstance().getConfig().getStringList("broadcasts");

        new CloudBroadcasterTask().runTaskTimerAsynchronously(CloudPlugin.getInstance(), 1200L, 6000L);
    }
}
