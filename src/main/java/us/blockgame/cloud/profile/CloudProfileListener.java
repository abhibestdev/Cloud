package us.blockgame.cloud.profile;

import com.google.common.collect.ImmutableMap;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.blockgame.cloud.CloudPlugin;
import us.blockgame.fabric.FabricPlugin;
import us.blockgame.fabric.request.RequestResponse;
import us.blockgame.fabric.util.Logger;

public class CloudProfileListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        CloudProfileHandler cloudProfileHandler = CloudPlugin.getInstance().getCloudProfileHandler();

        //Create a profile if player doesn't have one
        if (!cloudProfileHandler.hasProfile(player)) {
            cloudProfileHandler.addProfile(player);

            //TODO: load profile
        }

        RequestResponse requestResponse = FabricPlugin.getInstance().getFabricRequestHandler().post("profile",
                ImmutableMap.of(
                        "uuid", player.getUniqueId().toString(),
                        "action", "create"
                ));

        if (!requestResponse.isSuccessful()) {
            Logger.error(CloudPlugin.getInstance(), "Could not create profile: " + requestResponse.getErrorMessage());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        CloudProfileHandler cloudProfileHandler = CloudPlugin.getInstance().getCloudProfileHandler();

        //Remove player's profile
        if (cloudProfileHandler.hasProfile(player)) {
            cloudProfileHandler.removeProfile(player);

            //TODO: save profile
        }
    }
}
