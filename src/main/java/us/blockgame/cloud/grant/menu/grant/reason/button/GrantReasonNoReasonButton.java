package us.blockgame.cloud.grant.menu.grant.reason.button;

import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import us.blockgame.cloud.grant.menu.grant.confirmation.GrantConfirmationMenu;
import us.blockgame.cloud.rank.Rank;
import us.blockgame.fabric.menu.Button;
import us.blockgame.fabric.util.ItemBuilder;

import java.util.UUID;

@AllArgsConstructor
public class GrantReasonNoReasonButton extends Button {

    private UUID uuid;
    private Rank rank;
    private long duration;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.REDSTONE).setName(ChatColor.RED + "No Reason").setLore(ChatColor.GRAY + "Don't provide a reason for this grant.").toItemStack();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        GrantConfirmationMenu grantConfirmationMenu = new GrantConfirmationMenu(uuid, rank, "No Reason", duration);
        grantConfirmationMenu.openMenu(player);
        return;
    }
}
