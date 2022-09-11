package us.blockgame.cloud.punishment.command;

import com.google.common.collect.ImmutableMap;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import us.blockgame.fabric.FabricPlugin;
import us.blockgame.fabric.command.framework.Command;
import us.blockgame.fabric.command.framework.Flag;
import us.blockgame.fabric.command.framework.Param;
import us.blockgame.fabric.redis.FabricRedisHandler;
import us.blockgame.fabric.request.RequestResponse;
import us.blockgame.fabric.util.MapUtil;
import us.blockgame.fabric.util.TimeUtil;

import java.util.Map;
import java.util.UUID;

public class TempBanCommand {

    @SneakyThrows
    @Command(name = "tempban", permission = "cloud.command.ban", runAsync = true)
    public void tempBan(CommandSender sender, @Param(name = "player") OfflinePlayer offlinePlayer, @Flag(value = {"s", "silent"}) boolean silent, @Param(name = "duration") String duration, @Param(name = "reason", wildcard = true, optional = true, defaultValue = "Misconduct") String reason) {

        long banTime = System.currentTimeMillis();
        long time = TimeUtil.parseTime(duration);
        UUID senderUUID = sender instanceof ConsoleCommandSender ? Bukkit.getOfflinePlayer("CONSOLE").getUniqueId() : ((Player) sender).getUniqueId();

        RequestResponse requestResponse = FabricPlugin.getInstance().getFabricRequestHandler().post("punishment",
                ImmutableMap.of(
                        "uuid", offlinePlayer.getUniqueId().toString(),
                        "type", "ban",
                        "reason", reason,
                        "duration", String.valueOf(time),
                        "senderUUID", senderUUID.toString(),
                        "time", String.valueOf(banTime)
                ));

        if (!requestResponse.isSuccessful()) {
            sender.sendMessage(ChatColor.RED + requestResponse.getErrorMessage());
            return;
        }

        FabricRedisHandler redisHandler = FabricPlugin.getInstance().getFabricRedisHandler();

        Map<String, Object> messageMap = MapUtil.createMap(
                "type", "ban",
                "player", offlinePlayer.getName(),
                "reason", reason,
                "duration", time,
                "time", banTime);

        //Send ban to redis
        redisHandler.send(messageMap);

        //Broadcast ban
        Bukkit.getOnlinePlayers().stream().filter(p -> (silent ? p.hasPermission("cloud.staff") : true)).forEach(p -> p.sendMessage((silent ? ChatColor.GRAY + "(Silent) " : "") + ChatColor.GREEN + offlinePlayer.getName() + " has been" + (time != -1L ? " temporarily" : "") + " banned by " + sender.getName() + " for " + reason + "."));
        return;
    }
}