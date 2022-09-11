package us.blockgame.cloud.rank;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.blockgame.cloud.CloudPlugin;
import us.blockgame.cloud.profile.CloudProfile;
import us.blockgame.cloud.profile.CloudProfileHandler;
import us.blockgame.cloud.rank.command.RankCommand;
import us.blockgame.cloud.rank.command.parameter.RankParameterType;
import us.blockgame.fabric.FabricPlugin;
import us.blockgame.fabric.command.FabricCommandHandler;
import us.blockgame.fabric.request.RequestResponse;
import us.blockgame.fabric.util.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class CloudRankHandler {

    public CloudRankHandler() {
        //Register commands
        FabricCommandHandler fabricCommandHandler = FabricPlugin.getInstance().getFabricCommandHandler();
        fabricCommandHandler.registerCommand(new RankCommand());

        //Register parameter types
        fabricCommandHandler.addParameterType(Rank.class, new RankParameterType());

        //Register listeners
        Bukkit.getPluginManager().registerEvents(new CloudRankListener(), CloudPlugin.getInstance());

        loadPermissions();
    }

    private void loadPermissions() {

            Arrays.stream(Rank.values()).forEach(r -> {
                RequestResponse requestResponse = FabricPlugin.getInstance().getFabricRequestHandler().get("ranks", ImmutableMap.of("rank", r.getName()));
                if (!requestResponse.isSuccessful()) {
                    Logger.error(CloudPlugin.getInstance(), "Could not fetch info for " + r.getName() + ": " + requestResponse.getErrorMessage());
                    return;
                }
                r.setPermissions((List<String>) requestResponse.getResponse().get("permissions"));
                Logger.success(CloudPlugin.getInstance(), "Loaded permissions for " + r.getName());
            });
    }

    public Rank getOfflineRank(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);

        //If player is online, don't need to get rank from Mongo
        if (player != null) {
            CloudProfileHandler cloudProfileHandler = CloudPlugin.getInstance().getCloudProfileHandler();
            CloudProfile cloudProfile = cloudProfileHandler.getProfile(player);

            return cloudProfile.getRank();
        }

        RequestResponse requestResponse = FabricPlugin.getInstance().getFabricRequestHandler().get("rank", ImmutableMap.of("uuid", uuid.toString()));
        if (!requestResponse.isSuccessful()) {
            Logger.error(CloudPlugin.getInstance(), "Could not fetch rank: " + requestResponse.getErrorMessage());
            return Rank.DEFAULT;
        }
        return Rank.getRank((String) requestResponse.getResponse().get("rank"));
    }

    public void affectedPlayersOfRankOrAboveAction(Rank rank, Consumer<? super Player> action) {
        //Get all players with the same rank or rank above the rank provided
        Bukkit.getOnlinePlayers().stream().filter(p -> getOfflineRank(p.getUniqueId()).getWeight() <= rank.getWeight()).forEach(p -> {
            //Accept action
            action.accept(p);
        });
    }
}
