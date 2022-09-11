package us.blockgame.cloud.grant.menu.grants.regular.button;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import us.blockgame.cloud.grant.Grant;
import us.blockgame.cloud.grant.menu.grants.delete.GrantsDeleteMenu;
import us.blockgame.cloud.util.Color2Util;
import us.blockgame.cloud.util.CorrespondingWool;
import us.blockgame.fabric.menu.Button;
import us.blockgame.fabric.util.DateUtil;
import us.blockgame.fabric.util.ItemBuilder;
import us.blockgame.fabric.util.TimeUtil;

import java.util.UUID;

@AllArgsConstructor
public class GrantsRegularGrantButton extends Button {

    private UUID uuid;
    private Grant grant;

    @Override
    public ItemStack getButtonItem(Player player) {

        return new ItemBuilder(CorrespondingWool.getByColor(Color2Util.getChatColor(grant.getRank().getLitePrefix())).getWool())
                .setName(grant.getRank().getLitePrefix() + grant.getRank().getName() + ChatColor.GRAY + " ┃ " + ChatColor.WHITE + DateUtil.millisToDate(grant.getTime()))
                .setLore(
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-------------------------------",
                        ChatColor.BLUE + "Granted By: " + grant.getExecutorRank().getLitePrefix() + Bukkit.getOfflinePlayer(grant.getUuid()).getName(),
                        ChatColor.BLUE + "Reason: " + ChatColor.WHITE + grant.getReason(),
                        ChatColor.BLUE + "Duration: " + ChatColor.WHITE + (grant.getDuration() == -1 ? "Forever" : TimeUtil.formatTimeMillis(grant.getDuration(), false, true) + (grant.getTime() + grant.getDuration() < System.currentTimeMillis() && grant.getDuration() != -1L ? ChatColor.GRAY + " (Expired)" : "")),
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-------------------------------",
                        ChatColor.RED.toString() + ChatColor.BOLD + "RIGHT CLICK TO DELETE THIS GRANT",
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-------------------------------"
                ).toItemStack();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (!clickType.isRightClick()) return;

        GrantsDeleteMenu grantsDeleteMenu = new GrantsDeleteMenu(uuid, grant);
        grantsDeleteMenu.openMenu(player);
    }
}