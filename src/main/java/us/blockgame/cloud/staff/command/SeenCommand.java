package us.blockgame.cloud.staff.command;

import com.google.common.collect.ImmutableMap;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.json.simple.JSONObject;
import us.blockgame.cloud.rank.Rank;
import us.blockgame.fabric.FabricPlugin;
import us.blockgame.fabric.command.framework.Command;
import us.blockgame.fabric.command.framework.Param;
import us.blockgame.fabric.request.RequestResponse;
import us.blockgame.fabric.util.DateUtil;
import us.blockgame.fabric.util.TimeUtil;

public class SeenCommand {

    @Command(name = "seen", permission = "cloud.command.seen")
    public void seen(CommandSender sender, @Param(name = "player") OfflinePlayer offlinePlayer) {
        RequestResponse requestResponse = FabricPlugin.getInstance().getFabricRequestHandler().get("profile",
                ImmutableMap.of(
                        "uuid", offlinePlayer.getUniqueId().toString()
                ));

        if (!requestResponse.isSuccessful()) {
            sender.sendMessage(ChatColor.RED + requestResponse.getErrorMessage());
            return;
        }

        Rank rank = Rank.getRank((String) requestResponse.getResponse().get("rank"));
        long firstLogin = (long) requestResponse.getResponse().get("firstLogin");
        long lastLogin = (long) requestResponse.getResponse().get("lastLogin");
        long lastLogout = (long) requestResponse.getResponse().get("lastLogout");

        sender.sendMessage(new String[]{
                ChatColor.RED + offlinePlayer.getName() + "'s Info:",
                ChatColor.RED + " Rank: " + rank.getLitePrefix() + rank.getName(),
                ChatColor.RED + " First Login: " + ChatColor.WHITE + DateUtil.millisToDate(firstLogin),
                ChatColor.RED + " Status: " + ChatColor.WHITE + (lastLogin > lastLogout ? "Online for " + TimeUtil.formatTimeMillis(System.currentTimeMillis() - lastLogin, false, true) : "Last seen " + TimeUtil.formatTimeMillis(System.currentTimeMillis() - lastLogout, false, true) + " ago")
        });
    }
}
