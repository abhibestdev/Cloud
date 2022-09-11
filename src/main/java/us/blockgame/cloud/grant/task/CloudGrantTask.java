package us.blockgame.cloud.grant.task;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import us.blockgame.cloud.CloudPlugin;
import us.blockgame.cloud.grant.Grant;
import us.blockgame.cloud.player.CloudPlayerHandler;
import us.blockgame.cloud.profile.CloudProfile;
import us.blockgame.cloud.profile.CloudProfileHandler;
import us.blockgame.cloud.rank.Rank;
import us.blockgame.fabric.FabricPlugin;
import us.blockgame.fabric.request.RequestResponse;
import us.blockgame.fabric.util.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CloudGrantTask extends BukkitRunnable {

    public void run() {
        CloudProfileHandler cloudProfileHandler = CloudPlugin.getInstance().getCloudProfileHandler();
        CloudPlayerHandler cloudPlayerHandler = CloudPlugin.getInstance().getCloudPlayerHandler();

        Bukkit.getOnlinePlayers().stream().filter(p -> cloudProfileHandler.hasProfile(p)).forEach(p -> {
            CloudProfile cloudProfile = cloudProfileHandler.getProfile(p);

            Grant grant = getLatestGrant(cloudProfile.getGrantList());

            //Set players rank to default if not active grant was found
            if (grant == null && cloudProfile.getRank() != Rank.DEFAULT) {
                cloudProfile.setRank(Rank.DEFAULT);
                cloudPlayerHandler.updatePermissions(p);

                RequestResponse requestResponse = FabricPlugin.getInstance().getFabricRequestHandler().post("ranks", ImmutableMap.of("uuid", p.getUniqueId().toString(), "rank", cloudProfile.getRank().getName()));
                if (!requestResponse.isSuccessful()) {
                    Logger.error(CloudPlugin.getInstance(), "Could not update " + p.getName() + "'s rank: " + requestResponse.getResponse());
                }
            } else

                //Check if their rank is not the same as their latest grant
                if (grant != null && grant.getRank() != cloudProfile.getRank()) {
                    cloudProfile.setRank(grant.getRank());
                    cloudPlayerHandler.updatePermissions(p);

                    RequestResponse requestResponse = FabricPlugin.getInstance().getFabricRequestHandler().post("ranks", ImmutableMap.of("uuid", p.getUniqueId().toString(), "rank", cloudProfile.getRank().getName()));
                    if (!requestResponse.isSuccessful()) {
                        Logger.error(CloudPlugin.getInstance(), "Could not update " + p.getName() + "'s rank: " + requestResponse.getResponse());
                    }
                }
        });
    }

    //Method to get last active grant on player
    public Grant getLatestGrant(List<Grant> grantList) {
        List<Grant> grants = new ArrayList<>(grantList);
        Collections.reverse(grants);

        return grants.stream().filter(g -> g.getDuration() == -1L || System.currentTimeMillis() <= g.getTime() + g.getDuration()).findFirst().orElse(null);
    }
}
