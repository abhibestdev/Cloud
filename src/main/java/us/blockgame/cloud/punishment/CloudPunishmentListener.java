package us.blockgame.cloud.punishment;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.simple.JSONObject;
import us.blockgame.cloud.CloudPlugin;
import us.blockgame.cloud.profile.CloudProfile;
import us.blockgame.cloud.profile.CloudProfileHandler;
import us.blockgame.cloud.punishment.impl.MutePunishment;
import us.blockgame.fabric.redis.event.RedisReceieveEvent;
import us.blockgame.fabric.util.ThreadUtil;
import us.blockgame.fabric.util.TimeUtil;

import java.util.Map;

public class CloudPunishmentListener implements Listener {

    @SneakyThrows
    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        CloudPunishmentHandler cloudPunishmentHandler = CloudPlugin.getInstance().getCloudPunishmentHandler();

        JSONObject activeBan = cloudPunishmentHandler.getActiveBanOrBlacklist(event.getUniqueId());

        //Check if player is banned
        if (activeBan != null) {
            boolean ban = ((String) activeBan.get("type")).equals("ban");
            String reason = (String) activeBan.get("reason");
            long duration = Long.parseLong((String) activeBan.get("duration"));
            boolean perm = duration == -1L;
            long time = Long.parseLong((String) activeBan.get("time"));

            //Don't allow them to join
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, cloudPunishmentHandler.getFormattedBanReason(reason, time, duration, ban));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        CloudPunishmentHandler cloudPunishmentHandler = CloudPlugin.getInstance().getCloudPunishmentHandler();

        JSONObject activeMute = cloudPunishmentHandler.getActiveMute(player.getUniqueId());

        //Check if player is muted
        if (activeMute != null) {
            String reason = (String) activeMute.get("reason");
            long duration = Long.parseLong((String) activeMute.get("duration"));
            boolean perm = duration == -1L;
            long time = Long.parseLong((String) activeMute.get("time"));

            CloudProfileHandler profileHandler = CloudPlugin.getInstance().getCloudProfileHandler();
            CloudProfile cloudProfile = profileHandler.getProfile(player);

            //Set player's mute
            cloudProfile.setMutePunishment(new MutePunishment(reason, time, duration));
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        CloudProfileHandler cloudProfileHandler = CloudPlugin.getInstance().getCloudProfileHandler();
        CloudProfile cloudProfile = cloudProfileHandler.getProfile(player);

        CloudPunishmentHandler cloudPunishmentHandler = CloudPlugin.getInstance().getCloudPunishmentHandler();

        if (cloudPunishmentHandler.isMuted(player)) {

            MutePunishment mutePunishment = cloudProfile.getMutePunishment();

            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You are currently muted for " + mutePunishment.getReason() + ". This mute " + (mutePunishment.getDuration() == -1L ? "will never expire" : "will expire in " + TimeUtil.formatTimeMillis(mutePunishment.getDuration() + mutePunishment.getMuteTime() - System.currentTimeMillis(), false, true)) + ".");
            return;
        }
    }

    @EventHandler
    public void onRedis(RedisReceieveEvent event) {
        Map<String, Object> messageMap = event.getMessageMap();

        CloudPunishmentHandler cloudPunishmentHandler = CloudPlugin.getInstance().getCloudPunishmentHandler();
        CloudProfileHandler cloudProfileHandler = CloudPlugin.getInstance().getCloudProfileHandler();

        String type = (String) messageMap.get("type");

        switch (type) {
            case "ban": {
                String playerName = (String) messageMap.get("player");
                String reason = (String) messageMap.get("reason");
                long duration = Long.parseLong((String) messageMap.get("duration"));
                long banTime = Long.parseLong((String) messageMap.get("time"));

                Player target = Bukkit.getPlayer(playerName);

                //Make sure player is online
                if (target != null) {
                    //Kick the player
                    ThreadUtil.runSync(() -> {
                        target.kickPlayer(cloudPunishmentHandler.getFormattedBanReason(reason, banTime, duration, true));
                    });
                }
                break;
            }
            case "mute": {
                String playerName = (String) messageMap.get("player");
                String reason = (String) messageMap.get("reason");
                long duration = Long.parseLong((String) messageMap.get("duration"));
                long muteTime = Long.parseLong((String) messageMap.get("time"));

                Player target = Bukkit.getPlayer(playerName);

                //Make sure player is online
                if (target != null) {
                    CloudProfile cloudProfile = cloudProfileHandler.getProfile(target);

                    //Set player's mute
                    cloudProfile.setMutePunishment(new MutePunishment(reason, muteTime, duration));
                }
                break;
            }
            case "unmute": {
                String playerName = (String) messageMap.get("player");

                Player target = Bukkit.getPlayer(playerName);

                //Make sure player is online
                if (target != null) {
                    CloudProfile cloudProfile = cloudProfileHandler.getProfile(target);

                    //Remove player's mute
                    cloudProfile.setMutePunishment(null);
                    break;
                }
            }
            case "blacklist": {
                String playerName = (String) messageMap.get("player");
                String reason = (String) messageMap.get("reason");
                String ip = (String) messageMap.get("ip");

                Bukkit.getOnlinePlayers().forEach(p -> {
                    CloudProfile cloudProfile = cloudProfileHandler.getProfile(p);

                    if (cloudProfile.getIps().contains(ip)) {

                        //Kick everyone who has matching ips
                        ThreadUtil.runSync(() -> {
                            p.kickPlayer(cloudPunishmentHandler.getFormattedBanReason(reason, 0L, 0L, false));
                        });
                    }
                });
                break;
            }
        }
    }
}
