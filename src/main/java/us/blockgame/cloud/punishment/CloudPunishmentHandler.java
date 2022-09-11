package us.blockgame.cloud.punishment;

import com.google.common.collect.ImmutableMap;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import us.blockgame.cloud.CloudPlugin;
import us.blockgame.cloud.profile.CloudProfile;
import us.blockgame.cloud.profile.CloudProfileHandler;
import us.blockgame.cloud.punishment.command.*;
import us.blockgame.cloud.punishment.impl.MutePunishment;
import us.blockgame.fabric.FabricPlugin;
import us.blockgame.fabric.command.FabricCommandHandler;
import us.blockgame.fabric.request.RequestResponse;
import us.blockgame.fabric.util.Logger;
import us.blockgame.fabric.util.TimeUtil;

import java.util.UUID;

public class CloudPunishmentHandler {

    public CloudPunishmentHandler() {
        //Register commands
        FabricCommandHandler fabricCommandHandler = FabricPlugin.getInstance().getFabricCommandHandler();
        fabricCommandHandler.registerCommand(new BanCommand());
        fabricCommandHandler.registerCommand(new UnbanCommand());
        fabricCommandHandler.registerCommand(new TempBanCommand());
        fabricCommandHandler.registerCommand(new MuteCommand());
        fabricCommandHandler.registerCommand(new UnmuteCommand());
        fabricCommandHandler.registerCommand(new TempMuteCommand());
        fabricCommandHandler.registerCommand(new HistoryCommand());
        fabricCommandHandler.registerCommand(new KickCommand());

        //Register listeners
        Bukkit.getPluginManager().registerEvents(new CloudPunishmentListener(), CloudPlugin.getInstance());
    }

    public String getFormattedBanReason(String reason, long banTime, long duration, boolean ban) {
        return ChatColor.RED + "You are currently " + (ban ? "banned" : "blacklisted") + " from this server." + "\n" +
                ChatColor.RED + "Reason: " + ChatColor.GRAY + reason + ChatColor.RED + "\n" +
                ChatColor.RED + (ban ? "Expires: " + ChatColor.GRAY + (duration == -1L ? "Never" : TimeUtil.formatTimeMillis((duration + banTime) - System.currentTimeMillis(), false, true)) : "This type of punishment cannot be appealed.");
    }

    public String getFormattedKickReason(String reason) {
        return ChatColor.RED + "You have been kicked from this server." + "\n" +
                ChatColor.RED + "Reason: " + ChatColor.GRAY + reason;
    }

    @SneakyThrows
    public JSONObject getActiveBanOrBlacklist(UUID uuid) {
        JSONObject banObject = getActiveBan(uuid);

        //Return ban
        if (banObject != null) return banObject;

        //Not punished
        return null;
    }

    @SneakyThrows
    public JSONObject getActiveMute(UUID uuid) {
        RequestResponse requestResponse = FabricPlugin.getInstance().getFabricRequestHandler().get("punishment",
                ImmutableMap.of(
                        "uuid", uuid.toString()
                ));

        if (!requestResponse.isSuccessful()) {
            Logger.error(CloudPlugin.getInstance(), "Could not check punishments: " + requestResponse.getErrorMessage());
            return null;
        }

        //Check if player has punishments
        if (!requestResponse.getResponse().containsKey("punishments")) return null;

        JSONObject punishments = (JSONObject) requestResponse.getResponse().get("punishments");

        JSONObject activeBan = null;
        boolean active = false;
        boolean expired = false;

        for (Object o : punishments.keySet()) {

            long time = Long.parseLong(o.toString());

            JSONObject punishment = (JSONObject) punishments.get(o);
            String type = (String) punishment.get("type");

            //Ignore anything other than bans
            if (!type.equalsIgnoreCase("mute")) continue;

            active = (boolean) punishment.get("active");
            long duration = Long.parseLong((String) punishment.get("duration"));
            expired = duration != -1L && (time + duration < System.currentTimeMillis());

            //Make sure found punishment is a mute and that it is active
            if (active && !expired) {
                activeBan = punishment;
                break;
            }
        }
        return activeBan;
    }

    @SneakyThrows
    public JSONObject getActiveBan(UUID uuid) {
        RequestResponse requestResponse = FabricPlugin.getInstance().getFabricRequestHandler().get("punishment",
                ImmutableMap.of(
                        "uuid", uuid.toString()
                ));

        if (!requestResponse.isSuccessful()) {
            Logger.error(CloudPlugin.getInstance(), "Could not check punishments: " + requestResponse.getErrorMessage());
            return null;
        }

        //Check if player has punishments
        if (!requestResponse.getResponse().containsKey("punishments")) return null;

        JSONObject punishments = (JSONObject) requestResponse.getResponse().get("punishments");

        JSONObject activeBan = null;
        boolean active = false;
        boolean expired = false;

        for (Object o : punishments.keySet()) {

            long time = Long.parseLong(o.toString());

            JSONObject punishment = (JSONObject) punishments.get(o);
            String type = (String) punishment.get("type");

            //Ignore anything other than bans
            if (!type.equalsIgnoreCase("ban")) continue;

            active = (boolean) punishment.get("active");
            long duration = Long.parseLong((String) punishment.get("duration"));
            expired = duration != -1L && (time + duration < System.currentTimeMillis());

            //Make sure found punishment is a mute and that it is active
            if (active && !expired) {
                activeBan = punishment;
                break;
            }
        }
        return activeBan;
    }

    public boolean isMuted(Player player) {
        CloudProfileHandler cloudProfileHandler = CloudPlugin.getInstance().getCloudProfileHandler();
        CloudProfile cloudProfile = cloudProfileHandler.getProfile(player);

        //Check if player is muted
        if (cloudProfile.getMutePunishment() != null) {
            MutePunishment mutePunishment = cloudProfile.getMutePunishment();

            //Check if mute is perms
            if (mutePunishment.getDuration() == -1L) {
                return true;
            }
            //Make sure mute isn't expired
            if (mutePunishment.getDuration() + mutePunishment.getMuteTime() > System.currentTimeMillis()) {
                return true;
            }
            cloudProfile.setMutePunishment(null);
            return false;
        }
        return false;
    }
}
