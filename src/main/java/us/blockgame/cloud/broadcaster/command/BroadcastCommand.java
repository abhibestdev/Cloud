package us.blockgame.cloud.broadcaster.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import us.blockgame.fabric.command.framework.Command;
import us.blockgame.fabric.command.framework.Param;
import us.blockgame.fabric.util.ColorUtil;

public class BroadcastCommand {

    @Command(name = "broadcast", aliases = {"shout", "bc"}, permission = "cloud.command.broadcast")
    public void broadcast(CommandSender sender, @Param(name = "text", wildcard = true) String text) {
        Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "Alert" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + ColorUtil.colorize(text));
    }
}
