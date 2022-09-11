package us.blockgame.cloud.punishment.menu.history.delete.button;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import us.blockgame.fabric.FabricPlugin;
import us.blockgame.fabric.menu.Button;
import us.blockgame.fabric.request.RequestResponse;
import us.blockgame.fabric.util.DateUtil;
import us.blockgame.fabric.util.ItemBuilder;
import us.blockgame.fabric.util.TimeUtil;

import java.util.UUID;

@AllArgsConstructor
public class HistoryDeleteConfirmationButton extends Button {

    private UUID uuid;
    private JSONObject punishmentObject;

    @Override
    public ItemStack getButtonItem(Player player) {
        String reason = (String) punishmentObject.get("reason");
        long duration = punishmentObject.containsKey("duration") ? Long.parseLong((String) punishmentObject.get("duration")) : -2L;
        boolean perm = duration == -1L;
        long time = Long.parseLong((String) punishmentObject.get("time"));
        UUID punisher = UUID.fromString((String) punishmentObject.get("uuid"));

        return new ItemBuilder(Material.LIME_DYE)
                .setName(ChatColor.GREEN + "Confirm Deletion")
                .setLore(
                        ChatColor.YELLOW + "Delete Punishment from " + ChatColor.GOLD + DateUtil.millisToDate(time) + ChatColor.YELLOW + ": ",
                        ChatColor.GOLD + " * " + ChatColor.YELLOW + "Punished By: " + ChatColor.RESET + Bukkit.getOfflinePlayer(punisher).getName(),
                        ChatColor.GOLD + " * " + ChatColor.YELLOW + "Duration: " + ChatColor.GOLD + (duration == -2L || perm ? "Forever" : TimeUtil.formatTimeMillis(time, false, true)),
                        ChatColor.GOLD + " * " + ChatColor.YELLOW + "Reason: " + ChatColor.GOLD + reason
                )
                .toItemStack();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        player.closeInventory();
        RequestResponse requestResponse = FabricPlugin.getInstance().getFabricRequestHandler().post("punishment",
                ImmutableMap.of(
                        "action", "delete",
                        "uuid", uuid.toString(),
                        "banTime", (String) punishmentObject.get("time")
                ));

        if (!requestResponse.isSuccessful()) {
            player.sendMessage(ChatColor.RED + requestResponse.getErrorMessage());
            return;
        }
        player.sendMessage(ChatColor.YELLOW + "You have successfully deleted a punishment.");
    }
}
