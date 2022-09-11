package us.blockgame.cloud.broadcaster.task;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import us.blockgame.cloud.CloudPlugin;
import us.blockgame.cloud.broadcaster.CloudBroadcasterHandler;
import us.blockgame.fabric.util.ColorUtil;

public class CloudBroadcasterTask extends BukkitRunnable {

    private int i = 0;

    @Override
    public void run() {
        CloudBroadcasterHandler cloudBroadcasterHandler = CloudPlugin.getInstance().getCloudBroadcasterHandler();

        //Reset index
        if (i >= cloudBroadcasterHandler.getBroadcasts().size()) {
            i = 0;
        }

        //Broadcast message
        String message = cloudBroadcasterHandler.getBroadcasts().get(i);
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(ColorUtil.colorize(message));
        Bukkit.broadcastMessage(" ");

        //Move to next broadcast
        i += 1;
    }
}
