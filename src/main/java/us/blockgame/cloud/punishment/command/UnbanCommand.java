package us.blockgame.cloud.punishment.command;

import com.google.common.collect.ImmutableMap;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import us.blockgame.fabric.FabricPlugin;
import us.blockgame.fabric.command.framework.Command;
import us.blockgame.fabric.command.framework.Flag;
import us.blockgame.fabric.command.framework.Param;
import us.blockgame.fabric.request.RequestResponse;

public class UnbanCommand {

    @SneakyThrows
    @Command(name = "unban", aliases = {"pardon"}, permission = "cloud.command.unban", runAsync = true)
    public void unban(CommandSender sender, @Param(name = "player") OfflinePlayer offlinePlayer, @Flag(value = {"s", "silent"}) boolean silent) {
        RequestResponse requestResponse = FabricPlugin.getInstance().getFabricRequestHandler().post("punishment",
                ImmutableMap.of(
                        "uuid", offlinePlayer.getUniqueId().toString(),
                        "type", "unban"
                ));

        if (!requestResponse.isSuccessful()) {
            sender.sendMessage(ChatColor.RED + requestResponse.getErrorMessage());
            return;
        }

        //Broadcast unban
        Bukkit.getOnlinePlayers().stream().filter(p -> (silent ? p.hasPermission("cloud.staff") : true)).forEach(p -> p.sendMessage((silent ? ChatColor.GRAY + "(Silent) " : "") + ChatColor.GREEN + offlinePlayer.getName() + " has been unbanned by " + sender.getName() + "."));
    }
}
