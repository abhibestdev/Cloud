package us.blockgame.cloud.profile.command;

import com.google.common.collect.ImmutableMap;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import us.blockgame.fabric.FabricPlugin;
import us.blockgame.fabric.command.framework.Command;
import us.blockgame.fabric.command.framework.Param;
import us.blockgame.fabric.request.RequestResponse;

public class DeleteProfileCommand {

    @Command(name = "deleteprofile", permission = "op")
    public void deleteProfile(CommandSender sender, @Param(name = "player") OfflinePlayer offlinePlayer) {

        RequestResponse requestResponse = FabricPlugin.getInstance().getFabricRequestHandler().post("profile",
                ImmutableMap.of(
                        "uuid", offlinePlayer.getUniqueId().toString(),
                        "action", "delete"
                ));

        if (!requestResponse.isSuccessful()) {
            sender.sendMessage(ChatColor.RED + requestResponse.getErrorMessage());
            return;
        }
        sender.sendMessage(ChatColor.GREEN + "Deleted " + offlinePlayer.getName() + "'s profile.");
        return;
    }
}
