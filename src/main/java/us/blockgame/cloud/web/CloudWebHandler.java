package us.blockgame.cloud.web;

import us.blockgame.cloud.CloudPlugin;
import us.blockgame.cloud.web.task.WebTask;

public class CloudWebHandler {

    public CloudWebHandler() {
        if (CloudPlugin.getInstance().getConfig().getBoolean("web.upload-stats")) {
            //Start upload task

            new WebTask().runTaskTimerAsynchronously(CloudPlugin.getInstance(), 20L, 20L);
        }
    }


}
