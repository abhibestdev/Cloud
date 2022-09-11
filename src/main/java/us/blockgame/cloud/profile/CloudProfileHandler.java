package us.blockgame.cloud.profile;

import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.blockgame.cloud.CloudPlugin;
import us.blockgame.cloud.profile.command.DeleteProfileCommand;
import us.blockgame.fabric.FabricPlugin;
import us.blockgame.fabric.command.FabricCommandHandler;

import java.util.Map;
import java.util.UUID;

public class CloudProfileHandler {

    private Map<UUID, CloudProfile> profileMap = Maps.newHashMap();

    public CloudProfileHandler() {
        //Register commands
        FabricCommandHandler fabricCommandHandler = FabricPlugin.getInstance().getFabricCommandHandler();
        fabricCommandHandler.registerCommand(new DeleteProfileCommand());

        Bukkit.getPluginManager().registerEvents(new CloudProfileListener(), CloudPlugin.getInstance());
    }

    public void addProfile(Player player) {
        profileMap.put(player.getUniqueId(), new CloudProfile());
    }

    public void removeProfile(Player player) {
        profileMap.remove(player.getUniqueId());
    }

    public boolean hasProfile(Player player) {
        return profileMap.containsKey(player.getUniqueId());
    }

    public CloudProfile getProfile(Player player) {
        return profileMap.get(player.getUniqueId());
    }
}
