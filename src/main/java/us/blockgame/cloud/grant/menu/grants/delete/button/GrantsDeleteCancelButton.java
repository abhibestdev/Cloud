package us.blockgame.cloud.grant.menu.grants.delete.button;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import us.blockgame.fabric.menu.Button;
import us.blockgame.fabric.util.ItemBuilder;

public class GrantsDeleteCancelButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.RED_DYE)
                .setName(ChatColor.RED + "Cancel Grant Deletion")
                .setLore(ChatColor.GRAY + "Cancel the grant deletion process.")
                .toItemStack();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        player.closeInventory();

        player.sendMessage(ChatColor.YELLOW + "You have canceled the grant deletion process.");
        return;
    }
}
