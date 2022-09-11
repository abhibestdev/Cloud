package us.blockgame.cloud.motd;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import us.blockgame.cloud.CloudPlugin;

public class CloudMOTDListener implements Listener {

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        CloudMOTDHandler cloudMOTDHandler = CloudPlugin.getInstance().getCloudMOTDHandler();

        //Set MOTD
        event.setMotd(cloudMOTDHandler.getMotd());
    }
}
