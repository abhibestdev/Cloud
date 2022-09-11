package us.blockgame.cloud.grant.command;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import us.blockgame.cloud.CloudPlugin;
import us.blockgame.cloud.grant.Grant;
import us.blockgame.cloud.profile.CloudProfile;
import us.blockgame.cloud.profile.CloudProfileHandler;
import us.blockgame.cloud.rank.CloudRankHandler;
import us.blockgame.cloud.rank.Rank;
import us.blockgame.fabric.FabricPlugin;
import us.blockgame.fabric.command.framework.Command;
import us.blockgame.fabric.command.framework.Param;
import us.blockgame.fabric.request.RequestResponse;
import us.blockgame.fabric.util.TimeUtil;

import java.util.UUID;

public class ConsoleGrantCommand {

    @Command(name = "consolegrant", aliases = {"cgrant"}, permission = "cloud.command.consolegrant")
    public void consoleGrant(CommandSender sender, @Param(name = "player") OfflinePlayer offlinePlayer, @Param(name = "rank") Rank rank, @Param(name = "duration") String duration, @Param(name = "reason", wildcard = true, optional = true, defaultValue = "No Reason") String reason) {
        UUID senderUUID = sender instanceof ConsoleCommandSender ? Bukkit.getOfflinePlayer("CONSOLE").getUniqueId() : ((Player) sender).getUniqueId();

        long grantTime = System.currentTimeMillis();
        long time = TimeUtil.parseTime(duration);
        RequestResponse requestResponse = FabricPlugin.getInstance().getFabricRequestHandler().post("grant", ImmutableMap.of("uuid", offlinePlayer.getUniqueId().toString(), "rank", rank.getName(), "reason", reason, "duration", String.valueOf(time), "senderUUID", senderUUID.toString(), "grantTime", String.valueOf(grantTime)));

        //Check if the player's document exists
        if (!requestResponse.isSuccessful()) {
            sender.sendMessage(ChatColor.RED + requestResponse.getErrorMessage());
            return;
        }
        if (offlinePlayer.isOnline()) {
            CloudProfileHandler cloudProfileHandler = CloudPlugin.getInstance().getCloudProfileHandler();
            CloudProfile cloudProfile = cloudProfileHandler.getProfile(offlinePlayer.getPlayer());

            CloudRankHandler cloudRankHandler = CloudPlugin.getInstance().getCloudRankHandler();

            //Add grant to grants list and the grants task will automatically update the players rank
            cloudProfile.getGrantList().add(new Grant(grantTime, time, reason, rank, offlinePlayer.getUniqueId(), cloudRankHandler.getOfflineRank(senderUUID)));
        }

        sender.sendMessage(ChatColor.YELLOW + "You have granted " + rank.getLitePrefix() + offlinePlayer.getName() + ChatColor.YELLOW + " the " + rank.getLitePrefix() + rank.getDisplayName() + ChatColor.YELLOW + " for " + ChatColor.GOLD + TimeUtil.formatTimeMillis(time, false, true) + ChatColor.YELLOW + " for " + ChatColor.GOLD + reason + ChatColor.YELLOW + ".");
    }
}
