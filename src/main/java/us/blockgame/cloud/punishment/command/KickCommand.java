package us.blockgame.cloud.punishment.command;

import com.google.common.collect.ImmutableMap;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import us.blockgame.cloud.CloudPlugin;
import us.blockgame.cloud.punishment.CloudPunishmentHandler;
import us.blockgame.fabric.FabricPlugin;
import us.blockgame.fabric.command.framework.Command;
import us.blockgame.fabric.command.framework.Flag;
import us.blockgame.fabric.command.framework.Param;
import us.blockgame.fabric.request.RequestResponse;
import us.blockgame.fabric.util.ThreadUtil;

import java.util.UUID;

public class KickCommand {

    @SneakyThrows
    @Command(name = "kick", permission = "cloud.command.kick", runAsync = true)
    public void kick(CommandSender sender, @Param(name = "player") Player player, @Flag(value = {"s", "silent"}) boolean silent, @Param(name = "reason", wildcard = true, optional = true, defaultValue = "Misconduct") String reason) {

        long banTime = System.currentTimeMillis();
        UUID senderUUID = sender instanceof ConsoleCommandSender ? Bukkit.getOfflinePlayer("CONSOLE").getUniqueId() : ((Player) sender).getUniqueId();

        RequestResponse requestResponse = FabricPlugin.getInstance().getFabricRequestHandler().post("punishment",
                ImmutableMap.of(
                        "uuid", player.getUniqueId().toString(),
                        "type", "kick",
                        "reason", reason,
                        "duration", String.valueOf(-1L),
                        "senderUUID", senderUUID.toString(),
                        "time", String.valueOf(banTime)
                ));

        if (!requestResponse.isSuccessful()) {
            sender.sendMessage(ChatColor.RED + requestResponse.getErrorMessage());
            return;
        }

        CloudPunishmentHandler cloudPunishmentHandler = CloudPlugin.getInstance().getCloudPunishmentHandler();

        ThreadUtil.runSync(() -> player.kickPlayer(cloudPunishmentHandler.getFormattedKickReason(reason)));

        //Broadcast ban
        Bukkit.getOnlinePlayers().stream().filter(p -> (silent ? p.hasPermission("cloud.staff") : true)).forEach(p -> p.sendMessage((silent ? ChatColor.GRAY + "(Silent) " : "") + ChatColor.GREEN + player.getName() + " has been kicked by " + sender.getName() + " for " + reason + "."));
        return;
    }
}
