package us.blockgame.cloud.motd;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import us.blockgame.cloud.CloudPlugin;
import us.blockgame.cloud.motd.command.SetMOTDCommand;
import us.blockgame.fabric.FabricPlugin;
import us.blockgame.fabric.command.FabricCommandHandler;
import us.blockgame.fabric.util.ColorUtil;

public class CloudMOTDHandler {

    @Setter @Getter private String motd = "";

    public CloudMOTDHandler() {
        //Load motd
        loadMOTD();

        //Register commands
        FabricCommandHandler fabricCommandHandler = FabricPlugin.getInstance().getFabricCommandHandler();
        fabricCommandHandler.registerCommand(new SetMOTDCommand());

        //Register listeners
        Bukkit.getPluginManager().registerEvents(new CloudMOTDListener(), CloudPlugin.getInstance());
    }

    private void loadMOTD() {
        motd = ColorUtil.colorize(CloudPlugin.getInstance().getConfig().getString("motd").replace("\\n", "\n"));
    }
}
