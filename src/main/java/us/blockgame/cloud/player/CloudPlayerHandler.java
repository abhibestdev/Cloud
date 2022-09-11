package us.blockgame.cloud.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import us.blockgame.cloud.CloudPlugin;
import us.blockgame.cloud.player.command.*;
import us.blockgame.cloud.profile.CloudProfile;
import us.blockgame.cloud.profile.CloudProfileHandler;
import us.blockgame.cloud.rank.Rank;
import us.blockgame.fabric.FabricPlugin;
import us.blockgame.fabric.command.FabricCommandHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CloudPlayerHandler {

    private List<Pattern> bannedChatPatterns = new ArrayList<>();

    public CloudPlayerHandler() {
        //Register commands
        FabricCommandHandler fabricCommandHandler = FabricPlugin.getInstance().getFabricCommandHandler();
        fabricCommandHandler.registerCommand(new SpeedCommand());
        fabricCommandHandler.registerCommand(new MessageCommand());
        fabricCommandHandler.registerCommand(new ReplyCommand());
        fabricCommandHandler.registerCommand(new PingCommand());
        fabricCommandHandler.registerCommand(new RegisterCommand());

        //Register banned chat patterns
        addBannedChatPattern(Pattern.compile("n+[i1l|]+gg+[e3]+r+", 2));
        addBannedChatPattern(Pattern.compile("k+i+l+l+ *y*o*u+r+ *s+e+l+f+", 2));
        addBannedChatPattern(Pattern.compile("f+a+g+[o0]+t+", 2));
        addBannedChatPattern(Pattern.compile("\\bk+y+s+\\b", 2));
        addBannedChatPattern(Pattern.compile("b+e+a+n+e+r+", 2));
        addBannedChatPattern(Pattern.compile("\\d{1,3}[,.]\\d{1,3}[,.]\\d{1,3}[,.]\\d{1,3}", 2));

        //Register listeners
        Bukkit.getPluginManager().registerEvents(new CloudPlayerListener(), CloudPlugin.getInstance());
    }

    public void sendPrivateMessage(Player from, Player to, String message) {
        CloudProfileHandler cloudProfileHandler = CloudPlugin.getInstance().getCloudProfileHandler();

        CloudProfile fromProfile = cloudProfileHandler.getProfile(from);
        CloudProfile toProfile = cloudProfileHandler.getProfile(to);

        from.sendMessage(ChatColor.GRAY + "(To " + toProfile.getRank().getLitePrefix() + to.getName() + ChatColor.GRAY + ") " + message);
        to.sendMessage(ChatColor.GRAY + "(From " + fromProfile.getRank().getLitePrefix() + from.getName() + ChatColor.GRAY + ") " + message);

        //If player has private messaging noises enabled then play sound
        if (toProfile.isSounds()) {
            to.playSound(to.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100.0f, 0.0f);
        }
    }

    public void flagFilter(String format) {
        Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("cloud.staff")).forEach(p -> p.sendMessage(ChatColor.RED + "[Filtered] " + format));
    }

    public void addBannedChatPattern(Pattern pattern) {
        bannedChatPatterns.add(pattern);
    }

    public boolean violatesFilter(String source) {
       return bannedChatPatterns.stream().filter(p -> p.matcher(source).find()).findFirst().orElse(null) != null;
    }

    public void unsetPermissions(Player player) {
        CloudProfileHandler cloudProfileHandler = CloudPlugin.getInstance().getCloudProfileHandler();
        CloudProfile cloudProfile = cloudProfileHandler.getProfile(player);

        //Don't run if player doesn't have an attachment
        if (cloudProfile.getPermissionAttachment() == null) return;

        PermissionAttachment permissionAttachment = cloudProfile.getPermissionAttachment();

        //Clear permissions
        permissionAttachment.getPermissions().keySet().forEach(p -> permissionAttachment.unsetPermission(p));
        return;
    }

    public void setPermissions(Player player) {
        CloudProfileHandler cloudProfileHandler = CloudPlugin.getInstance().getCloudProfileHandler();
        CloudProfile cloudProfile = cloudProfileHandler.getProfile(player);

        //Don't run if player doesn't have an attachment
        if (cloudProfile.getPermissionAttachment() == null) return;

        Rank rank = cloudProfile.getRank();

        PermissionAttachment permissionAttachment = cloudProfile.getPermissionAttachment();

        //Get all ranks below their current rank
        Arrays.stream(Rank.values()).filter(r -> r.getWeight() >= rank.getWeight()).forEach(r -> {
            //Set permission
            r.getPermissions().forEach(p -> permissionAttachment.setPermission(p, true));
        });
    }

    public void updatePermissions(Player player) {
        unsetPermissions(player);
        setPermissions(player);
    }
}
