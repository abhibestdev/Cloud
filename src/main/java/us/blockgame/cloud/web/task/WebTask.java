package us.blockgame.cloud.web.task;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import us.blockgame.cloud.CloudPlugin;
import us.blockgame.fabric.FabricPlugin;
import us.blockgame.fabric.request.RequestResponse;
import us.blockgame.fabric.util.Logger;

public class WebTask extends BukkitRunnable {

    public void run() {
        RequestResponse requestResponse = FabricPlugin.getInstance().getFabricRequestHandler().post("stats",
                ImmutableMap.of(
                        "action", "set-player-count",
                        "count", String.valueOf(Bukkit.getOnlinePlayers().size())
                ));

        if (!requestResponse.isSuccessful()) {
            Logger.error(CloudPlugin.getInstance(), "Could not update web stats: " + requestResponse.getErrorMessage());
        }
    }
}
