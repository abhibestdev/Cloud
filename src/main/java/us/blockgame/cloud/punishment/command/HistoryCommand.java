package us.blockgame.cloud.punishment.command;

import com.google.common.collect.ImmutableMap;
import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import us.blockgame.cloud.punishment.menu.history.regular.HistoryRegularMenu;
import us.blockgame.fabric.FabricPlugin;
import us.blockgame.fabric.command.framework.Command;
import us.blockgame.fabric.command.framework.Param;
import us.blockgame.fabric.request.RequestResponse;

public class HistoryCommand {

    @SneakyThrows
    @Command(name = "history", permission = "cloud.command.history", inGameOnly = true, runAsync = true)
    public void history(CommandSender sender, @Param(name = "player") OfflinePlayer offlinePlayer) {
        Player player = (Player) sender;

        RequestResponse requestResponse = FabricPlugin.getInstance().getFabricRequestHandler().get("punishment",
                ImmutableMap.of(
                        "uuid", offlinePlayer.getUniqueId().toString()
                ));

        if (!requestResponse.isSuccessful()) {
            sender.sendMessage(ChatColor.RED + requestResponse.getErrorMessage());
            return;
        }

        JSONObject punishments = (JSONObject) requestResponse.getResponse().get("punishments");

        //Open menu
        HistoryRegularMenu historyRegularMenu = new HistoryRegularMenu(offlinePlayer.getUniqueId(),punishments);
        historyRegularMenu.openMenu(player);
    }
}
