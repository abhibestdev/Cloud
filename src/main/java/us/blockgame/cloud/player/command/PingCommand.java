package us.blockgame.cloud.player.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockgame.fabric.command.framework.Command;
import us.blockgame.fabric.command.framework.Param;

public class PingCommand {

    @Command(name = "ping", inGameOnly = true)
    public void ping(CommandSender sender, @Param(name = "player", optional = true) Player player) {
        sender.sendMessage(ChatColor.GREEN + (sender == player ? "Your" : player.getName() + "'s") + " ping is: " + player.getPing() + " ms");
        return;
    }
}
