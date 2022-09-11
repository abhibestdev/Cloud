package us.blockgame.cloud;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import us.blockgame.cloud.broadcaster.CloudBroadcasterHandler;
import us.blockgame.cloud.grant.CloudGrantHandler;
import us.blockgame.cloud.motd.CloudMOTDHandler;
import us.blockgame.cloud.player.CloudPlayerHandler;
import us.blockgame.cloud.profile.CloudProfileHandler;
import us.blockgame.cloud.punishment.CloudPunishmentHandler;
import us.blockgame.cloud.rank.CloudRankHandler;
import us.blockgame.cloud.staff.CloudStaffHandler;
import us.blockgame.cloud.web.CloudWebHandler;

public class CloudPlugin extends JavaPlugin {

    @Getter private static CloudPlugin instance;

    @Getter private CloudProfileHandler cloudProfileHandler;
    @Getter private CloudPlayerHandler cloudPlayerHandler;
    @Getter private CloudRankHandler cloudRankHandler;
    @Getter private CloudGrantHandler cloudGrantHandler;
    @Getter private CloudMOTDHandler cloudMOTDHandler;
    @Getter private CloudBroadcasterHandler cloudBroadcasterHandler;
    @Getter private CloudPunishmentHandler cloudPunishmentHandler;
    @Getter private CloudStaffHandler cloudStaffHandler;
    @Getter private CloudWebHandler cloudWebHandler;

    @Override
    public void onEnable() {
        instance = this;

        //Create config
        getConfig().options().copyDefaults(true);
        saveConfig();

        //Register handlers
        registerHandlers();
    }

    private void registerHandlers() {
        cloudProfileHandler = new CloudProfileHandler();
        cloudPlayerHandler = new CloudPlayerHandler();
        cloudRankHandler = new CloudRankHandler();
        cloudGrantHandler = new CloudGrantHandler();
        cloudMOTDHandler = new CloudMOTDHandler();
        cloudBroadcasterHandler = new CloudBroadcasterHandler();
        cloudPunishmentHandler = new CloudPunishmentHandler();
        cloudStaffHandler = new CloudStaffHandler();
        cloudWebHandler = new CloudWebHandler();
    }
}
