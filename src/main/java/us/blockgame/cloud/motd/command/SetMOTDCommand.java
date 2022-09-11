package us.blockgame.cloud.motd.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import us.blockgame.cloud.CloudPlugin;
import us.blockgame.cloud.motd.CloudMOTDHandler;
import us.blockgame.fabric.command.framework.Command;
import us.blockgame.fabric.command.framework.Param;
import us.blockgame.fabric.util.ColorUtil;
import us.blockgame.fabric.util.ThreadUtil;

public class SetMOTDCommand {

    @Command(name = "setmotd", permission = "op")
    public void setMotd(CommandSender sender, @Param(name = "text", wildcard = true) String text) {
        CloudMOTDHandler cloudMOTDHandler = CloudPlugin.getInstance().getCloudMOTDHandler();

        //Set MOTD
        cloudMOTDHandler.setMotd(ColorUtil.colorize(text).replace("\\n", "\n"));

        sender.sendMessage(new String[]{
                ChatColor.GREEN + "The MOTD has been updated to: ",
                ChatColor.RESET + cloudMOTDHandler.getMotd()
        });

        //Save motd

        ThreadUtil.runAsync(() -> {
            CloudPlugin.getInstance().getConfig().set("motd", text);
            CloudPlugin.getInstance().saveConfig();
        });
        return;
    }
}
