package us.blockgame.cloud.grant.menu.grant.confirmation.button;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import us.blockgame.fabric.menu.Button;
import us.blockgame.fabric.util.ItemBuilder;

public class GrantConfirmationCancelButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.RED_DYE)
                .setName(ChatColor.RED + "Cancel Grant")
                .setLore(ChatColor.GRAY + "Cancel the grant process.")
                .toItemStack();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        player.closeInventory();

        player.sendMessage(ChatColor.YELLOW + "You have canceled the granting process.");
        return;
    }
}
