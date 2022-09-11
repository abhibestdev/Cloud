package us.blockgame.cloud.grant.command;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockgame.cloud.grant.menu.grant.regular.GrantRegularMenu;
import us.blockgame.fabric.command.framework.Command;
import us.blockgame.fabric.command.framework.Param;

public class GrantCommand {

    @Command(name = "grant", permission = "cloud.command.grant", runAsync = true)
    public void grant(CommandSender sender, @Param(name = "player") OfflinePlayer offlinePlayer) {

        GrantRegularMenu grantRegularMenu = new GrantRegularMenu(offlinePlayer.getUniqueId());
        grantRegularMenu.openMenu((Player) sender);
    }
}
