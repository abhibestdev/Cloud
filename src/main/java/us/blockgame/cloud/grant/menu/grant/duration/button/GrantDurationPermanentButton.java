package us.blockgame.cloud.grant.menu.grant.duration.button;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;
import us.blockgame.cloud.grant.menu.grant.reason.GrantReasonMenu;
import us.blockgame.cloud.rank.Rank;
import us.blockgame.fabric.menu.Button;
import us.blockgame.fabric.util.ItemBuilder;

import java.util.UUID;

@AllArgsConstructor
public class GrantDurationPermanentButton extends Button {

    private UUID uuid;
    private Rank rank;
    private Rank playerRank;

    @Override
    public ItemStack getButtonItem(Player player) {

        return new ItemBuilder(Material.DIAMOND)
                .setName(ChatColor.AQUA + "Permanent Grant")
                .setLore(ChatColor.GRAY + "Click to grant " + Bukkit.getOfflinePlayer(uuid).getName() + " the " + rank.getDisplayName() + " rank forever.")
                .toItemStack();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {

        GrantReasonMenu grantReasonMenu = new GrantReasonMenu(uuid, rank, -1L, playerRank);
        grantReasonMenu.openMenu(player);
    }
}