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
import us.blockgame.fabric.redis.FabricRedisHandler;
import us.blockgame.fabric.request.RequestResponse;
import us.blockgame.fabric.util.MapUtil;

import java.util.Map;

public class UnmuteCommand {

    @SneakyThrows
    @Command(name = "unmute", permission = "cloud.command.unmute", runAsync = true)
    public void unmute(CommandSender sender, @Param(name = "player") OfflinePlayer offlinePlayer, @Flag(value = {"s", "silent"}) boolean silent) {
        RequestResponse requestResponse = FabricPlugin.getInstance().getFabricRequestHandler().post("punishment",
                ImmutableMap.of(
                        "uuid", offlinePlayer.getUniqueId().toString(),
                        "type", "unmute"
                ));

        if (!requestResponse.isSuccessful()) {
            sender.sendMessage(ChatColor.RED + requestResponse.getErrorMessage());
            return;
        }
        FabricRedisHandler redisHandler = FabricPlugin.getInstance().getFabricRedisHandler();

        Map<String, Object> messageMap = MapUtil.createMap(
                "type", "unmute",
                "player", offlinePlayer.getName());

        //Send unmute to redis
        redisHandler.send(messageMap);

        //Broadcast unban
        Bukkit.getOnlinePlayers().stream().filter(p -> (silent ? p.hasPermission("cloud.staff") : true)).forEach(p -> p.sendMessage((silent ? ChatColor.GRAY + "(Silent) " : "") + ChatColor.GREEN + offlinePlayer.getName() + " has been unmuted by " + sender.getName() + "."));
    }
}
