package us.blockgame.cloud.punishment.menu.history.delete.button;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import us.blockgame.fabric.menu.Button;
import us.blockgame.fabric.util.ItemBuilder;

public class HistoryDeleteCancelButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.RED_DYE)
                .setName(ChatColor.RED + "Cancel Punishment Deletion")
                .setLore(ChatColor.GRAY + "Cancel the punishment deletion process.")
                .toItemStack();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        player.closeInventory();

        player.sendMessage(ChatColor.YELLOW + "You have canceled the punishment deletion process.");
        return;
    }
}
