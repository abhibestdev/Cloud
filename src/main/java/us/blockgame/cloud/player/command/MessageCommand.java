package us.blockgame.cloud.player.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockgame.cloud.CloudPlugin;
import us.blockgame.cloud.player.CloudPlayerHandler;
import us.blockgame.cloud.profile.CloudProfile;
import us.blockgame.cloud.profile.CloudProfileHandler;
import us.blockgame.cloud.punishment.CloudPunishmentHandler;
import us.blockgame.fabric.command.framework.Command;
import us.blockgame.fabric.command.framework.Param;

public class MessageCommand {

    @Command(name = "message", aliases = {"m", "msg", "whisper", "w", "tell"}, inGameOnly = true)
    public void message(CommandSender sender, @Param(name = "player") Player target, @Param(name = "text", wildcard = true) String text) {
        Player player = (Player) sender;

        CloudPunishmentHandler cloudPunishmentHandler = CloudPlugin.getInstance().getCloudPunishmentHandler();

        //Check if player is muted
        if (cloudPunishmentHandler.isMuted(player)) {
            sender.sendMessage(ChatColor.RED + "You are currently muted.");
            return;
        }

        CloudProfileHandler cloudProfileHandler = CloudPlugin.getInstance().getCloudProfileHandler();
        CloudProfile cloudProfile = cloudProfileHandler.getProfile(player);

        if (!cloudProfile.isPrivateMessaging()) {
            sender.sendMessage(ChatColor.RED + "You have private messaging disabled.");
            return;
        }
        //Check if they are ignoring the target
        if (cloudProfile.getIgnored().contains(target.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "You are currently ignoring that player.");
            return;
        }
        CloudProfile targetProfile = cloudProfileHandler.getProfile(target);

        //Check if the target is ignoring the sender
        if (targetProfile.getIgnored().contains(player.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "That player is currently ignoring you.");
            return;
        }

        //Check if target has private messaging disabled
        if (!targetProfile.isPrivateMessaging()) {
            sender.sendMessage(ChatColor.RED + "That player is currently not accepting private messages.");
            return;
        }

        CloudPlayerHandler cloudPlayerHandler = CloudPlugin.getInstance().getCloudPlayerHandler();

        //Send private message
        cloudPlayerHandler.sendPrivateMessage(player, target, text);

        //Save the UUID of the last messaged player so they can reply
        cloudProfile.setLastMessage(target.getUniqueId());
        targetProfile.setLastMessage(player.getUniqueId());
        return;
    }
}
