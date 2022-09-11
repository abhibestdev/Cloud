package us.blockgame.cloud.grant.command;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockgame.cloud.grant.menu.grants.regular.GrantsRegularMenu;
import us.blockgame.fabric.command.framework.Command;
import us.blockgame.fabric.command.framework.Param;

public class GrantsCommand {

    @Command(name = "grants", permission = "cloud.command.grants", inGameOnly = true)
    public void grants(CommandSender sender, @Param(name = "player") OfflinePlayer offlinePlayer) {
        Player player = (Player) sender;

        //Open grants menu
        GrantsRegularMenu grantsRegularMenu = new GrantsRegularMenu(offlinePlayer.getUniqueId());
        grantsRegularMenu.openMenu(player);
    }
}
