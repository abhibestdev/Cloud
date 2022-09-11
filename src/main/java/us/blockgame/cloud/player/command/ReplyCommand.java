package us.blockgame.cloud.player.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockgame.cloud.CloudPlugin;
import us.blockgame.cloud.player.CloudPlayerHandler;
import us.blockgame.cloud.profile.CloudProfile;
import us.blockgame.cloud.profile.CloudProfileHandler;
import us.blockgame.cloud.punishment.CloudPunishmentHandler;
import us.blockgame.fabric.command.framework.Command;
import us.blockgame.fabric.command.framework.Param;

public class ReplyCommand {

    @Command(name = "reply", aliases = {"r"}, inGameOnly = true)
    public void reply(CommandSender sender, @Param(name = "text", wildcard = true, optional = true) String text) {
        Player player = (Player) sender;

        CloudProfileHandler cloudProfileHandler = CloudPlugin.getInstance().getCloudProfileHandler();
        CloudProfile cloudProfile = cloudProfileHandler.getProfile(player);

        if (cloudProfile.getLastMessage() == null) {
            sender.sendMessage(ChatColor.RED + "You are not currently engaged in a conversation.");
            return;
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(cloudProfile.getLastMessage());
        if (!offlinePlayer.isOnline()) {
            sender.sendMessage(ChatColor.RED + offlinePlayer.getName() + " is no longer online.");
            return;
        }
        Player target = offlinePlayer.getPlayer();
        if (text.equals("")) {
            sender.sendMessage(ChatColor.GREEN + "You are currently in a conversation with " + target.getName() + ".");
            return;
        }
        CloudPunishmentHandler cloudPunishmentHandler = CloudPlugin.getInstance().getCloudPunishmentHandler();
        if (cloudPunishmentHandler.isMuted(player)) {
            sender.sendMessage(ChatColor.RED + "You are currently muted.");
            return;
        }
        if (!cloudProfile.isPrivateMessaging()) {
            sender.sendMessage(ChatColor.RED + "You have private messaging disabled.");
            return;
        }
        if (cloudProfile.getIgnored().contains(target.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "You are currently ignoring that player.");
            return;
        }
        CloudProfile targetProfile = cloudProfileHandler.getProfile(target);
        if (targetProfile.getIgnored().contains(player.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "That player is currently ignoring you.");
            return;
        }
        if (!targetProfile.isPrivateMessaging()) {
           sender.sendMessage(ChatColor.RED + "That player is currently not accepting private messages.");
            return;
        }
        CloudPlayerHandler cloudPlayerHandler = CloudPlugin.getInstance().getCloudPlayerHandler();

        cloudPlayerHandler.sendPrivateMessage(player, target, text);
        cloudProfile.setLastMessage(target.getUniqueId());
        targetProfile.setLastMessage(player.getUniqueId());
        return;
    }
}
