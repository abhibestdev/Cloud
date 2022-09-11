package us.blockgame.cloud.player;

import com.google.common.collect.ImmutableMap;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.blockgame.cloud.CloudPlugin;
import us.blockgame.cloud.grant.CloudGrantHandler;
import us.blockgame.cloud.profile.CloudProfile;
import us.blockgame.cloud.profile.CloudProfileHandler;
import us.blockgame.fabric.FabricPlugin;
import us.blockgame.fabric.request.RequestResponse;
import us.blockgame.fabric.util.ColorUtil;
import us.blockgame.fabric.util.Logger;

import java.util.UUID;

public class CloudPlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPrelogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        CloudProfileHandler cloudProfileHandler = CloudPlugin.getInstance().getCloudProfileHandler();
        CloudGrantHandler cloudGrantHandler = CloudPlugin.getInstance().getCloudGrantHandler();

        long loginTime = System.currentTimeMillis();

        RequestResponse requestResponse = FabricPlugin.getInstance().getFabricRequestHandler().post("profile",
                ImmutableMap.of(
                        "uuid", player.getUniqueId().toString(),
                        "action", "set-last-login",
                        "time", String.valueOf(loginTime)
                ));

        if (!requestResponse.isSuccessful()) {
            Logger.error(CloudPlugin.getInstance(), "Could not set last login: " + requestResponse.getErrorMessage());
        }
        CloudProfile cloudProfile = cloudProfileHandler.getProfile(player);
        cloudProfile.setGrantList(cloudGrantHandler.getGrantsFromMongo(player.getUniqueId()));

        cloudProfile.setLoginTime(loginTime);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        long logoutTime = System.currentTimeMillis();

        RequestResponse requestResponse = FabricPlugin.getInstance().getFabricRequestHandler().post("profile",
                ImmutableMap.of(
                        "uuid", player.getUniqueId().toString(),
                        "action", "set-last-logout",
                        "time", String.valueOf(logoutTime)
                ));

        if (!requestResponse.isSuccessful()) {
            Logger.error(CloudPlugin.getInstance(), "Could not set last logout: " + requestResponse.getErrorMessage());
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        CloudProfileHandler cloudProfileHandler = CloudPlugin.getInstance().getCloudProfileHandler();
        CloudProfile cloudProfile = cloudProfileHandler.getProfile(player);

        CloudPlayerHandler cloudPlayerHandler = CloudPlugin.getInstance().getCloudPlayerHandler();

        if (player.isOp()) {
            event.setMessage(ColorUtil.colorize(event.getMessage()));
        }

        event.setFormat(cloudProfile.getRank().getPrefix() + player.getName() + ChatColor.GRAY + ": " + ChatColor.RESET + event.getMessage().replace("%", "%%"));

        if (cloudPlayerHandler.violatesFilter(event.getMessage().replace(" ", "")) || cloudPlayerHandler.violatesFilter(event.getMessage())) {
            event.setCancelled(true);

            player.sendMessage(ChatColor.RED + "Your message was not sent because it contains inappropriate content.");
            cloudPlayerHandler.flagFilter(event.getFormat());
            return;
        }
    }
}
