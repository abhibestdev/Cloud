package us.blockgame.cloud.rank.command;

import com.google.common.collect.ImmutableMap;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import us.blockgame.cloud.rank.Rank;
import us.blockgame.fabric.FabricPlugin;
import us.blockgame.fabric.command.framework.Command;
import us.blockgame.fabric.command.framework.Param;
import us.blockgame.fabric.redis.FabricRedisHandler;
import us.blockgame.fabric.request.RequestResponse;
import us.blockgame.fabric.util.MapUtil;

import java.util.Map;

public class RankCommand {

    @Command(name = "rank", aliases = {"group"}, permission = "cloud.command.rank")
    public void rank(CommandSender sender) {
        //Send rank help
        sender.sendMessage(new String[]{
                ChatColor.RED + "Rank Commands: ",
                ChatColor.RED + " /rank addPerm <rank> <permission>",
                ChatColor.RED + " /rank delPerm <rank> <permission>",
                ChatColor.RED + " /rank listPerm <rank>"
        });
    }

    @Command(name = "rank.addperm", aliases = {"group.addperm"}, permission = "cloud.command.rank", runAsync = true)
    public void rankAddPerm(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "permission") String permission) {
        RequestResponse requestResponse = FabricPlugin.getInstance().getFabricRequestHandler().post("ranks",
                ImmutableMap.of(
                        "action", "add-perm",
                        "rank", rank.getName(),
                        "permission", permission
                ));

        if (!requestResponse.isSuccessful()) {
            sender.sendMessage(ChatColor.RED + requestResponse.getErrorMessage());
            return;
        }

        FabricRedisHandler redisHandler = FabricPlugin.getInstance().getFabricRedisHandler();

        Map<String, Object> messageMap = MapUtil.createMap(
                "type", "rank-add-perm",
                "rank", rank.getName(),
                "permission", permission
        );

        //Send permission update to redis
        redisHandler.send(messageMap);

        sender.sendMessage(ChatColor.YELLOW + "You have granted the rank " + rank.getLitePrefix() + rank.getDisplayName() + ChatColor.YELLOW + " the permission " + ChatColor.AQUA + permission + ChatColor.YELLOW + ".");
        return;
    }

    @Command(name = "rank.delperm", aliases = {"group.delperm"}, permission = "cloud.command.rank", runAsync = true)
    public void rankDelPerm(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "permission") String permission) {
        RequestResponse requestResponse = FabricPlugin.getInstance().getFabricRequestHandler().post("ranks",
                ImmutableMap.of(
                        "action", "del-perm",
                        "rank", rank,
                        "permission", permission
                ));

        if (!requestResponse.isSuccessful()) {
            sender.sendMessage(ChatColor.RED + requestResponse.getErrorMessage());
            return;
        }

        FabricRedisHandler redisHandler = FabricPlugin.getInstance().getFabricRedisHandler();

        Map<String, Object> messageMap = MapUtil.createMap(
                "type", "rank-del-perm",
                "rank", rank.toString(),
                "permission", permission
        );

        //Send permission update to redis
        redisHandler.send(messageMap);

        sender.sendMessage(ChatColor.YELLOW + "You have revoked the rank " + rank.getLitePrefix() + rank.getDisplayName() + ChatColor.YELLOW + " of the permission " + ChatColor.AQUA + permission + ChatColor.YELLOW + ".");
        return;
    }

    @Command(name = "rank.listperm", aliases = {"group.listperm"}, permission = "cloud.command.rank", runAsync = true)
    public void rankListPerm(CommandSender sender, @Param(name = "rank") Rank rank) {
        if (rank.getPermissions().size() > 0) {
            sender.sendMessage(ChatColor.RED + rank.getName() + "'s permissions: ");
            rank.getPermissions().stream().forEach(p -> {
                sender.sendMessage(" " + p);
            });
        } else {
            sender.sendMessage(ChatColor.RED + "No permissions found for that rank.");
        }
    }
}
