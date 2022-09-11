package us.blockgame.cloud.grant.menu.grant.regular.button;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import us.blockgame.cloud.CloudPlugin;
import us.blockgame.cloud.grant.menu.grant.duration.GrantDurationMenu;
import us.blockgame.cloud.rank.CloudRankHandler;
import us.blockgame.cloud.rank.Rank;
import us.blockgame.cloud.util.Color2Util;
import us.blockgame.cloud.util.CorrespondingWool;
import us.blockgame.fabric.menu.Button;
import us.blockgame.fabric.util.ItemBuilder;

import java.util.UUID;

@RequiredArgsConstructor
public class GrantRegularRankButton extends Button {

    private final UUID uuid;
    private final Rank rank;

    private Rank playerRank;

    @Override
    public ItemStack getButtonItem(Player player) {

        CloudRankHandler rankHandler = CloudPlugin.getInstance().getCloudRankHandler();
        playerRank = rankHandler.getOfflineRank(uuid);

        return new ItemBuilder(CorrespondingWool.getByColor(Color2Util.getChatColor(rank.getLitePrefix())).getWool())
                .setName(rank.getLitePrefix() + rank.getDisplayName())
                .setLore(
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------",
                        ChatColor.BLUE + "Grant " + playerRank.getLitePrefix() + Bukkit.getOfflinePlayer(uuid).getName() + ChatColor.BLUE + " the " + rank.getLitePrefix() + rank.getDisplayName() + ChatColor.BLUE + " rank.",
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------")
                .toItemStack();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        GrantDurationMenu grantDurationMenu = new GrantDurationMenu(uuid, rank, playerRank);
        grantDurationMenu.openMenu(player);
    }
}
