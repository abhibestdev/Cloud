package us.blockgame.cloud.player.command;

import com.google.common.collect.ImmutableMap;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockgame.fabric.FabricPlugin;
import us.blockgame.fabric.command.framework.Command;
import us.blockgame.fabric.command.framework.Param;
import us.blockgame.fabric.request.RequestResponse;

public class RegisterCommand {

    @Command(name = "register", inGameOnly = true)
    public void register(CommandSender sender, @Param(name = "email") String email) {
        Player player = (Player) sender;

        RequestResponse requestResponse = FabricPlugin.getInstance().getFabricRequestHandler().post("register",
                ImmutableMap.of(
                        "uuid", player.getUniqueId().toString(),
                        "name", player.getName(),
                        "email", email
                ));

        if (!requestResponse.isSuccessful()) {
            sender.sendMessage(ChatColor.RED + requestResponse.getErrorMessage());
            return;
        }
        sender.sendMessage(ChatColor.GREEN + "You should be receiving an e-mail confirmation shortly.");
        return;
    }
}
