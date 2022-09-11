package us.blockgame.cloud.punishment.menu.history.regular.button;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import us.blockgame.cloud.punishment.menu.history.delete.HistoryDeleteMenu;
import us.blockgame.fabric.menu.Button;
import us.blockgame.fabric.util.DateUtil;
import us.blockgame.fabric.util.ItemBuilder;
import us.blockgame.fabric.util.TimeUtil;

import java.util.UUID;

@AllArgsConstructor
public class HistoryRegularPunishmentButton extends Button {

    private UUID uuid;
    private JSONObject punishmentObject;

    @Override
    public ItemStack getButtonItem(Player player) {

        String type = (String) punishmentObject.get("type");

        String reason = (String) punishmentObject.get("reason");
        long duration = punishmentObject.containsKey("duration") ? Long.parseLong((String) punishmentObject.get("duration")) : -2L;
        boolean perm = duration == -1L;
        long time = Long.parseLong((String) punishmentObject.get("time"));
        UUID punisher = UUID.fromString((String) punishmentObject.get("uuid"));


        return new ItemBuilder(type.equals("mute") ? Material.GOLD_INGOT : type.equals("kick") ? Material.COAL : type.equals("blacklist") ? Material.DIAMOND : Material.IRON_INGOT)
                .setName(ChatColor.WHITE + (type.substring(0, 1).toUpperCase() + type.substring(1)) + ChatColor.GRAY + " ┃ " + ChatColor.WHITE + DateUtil.millisToDate(time))
                .setLore(
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-------------------------------",
                        ChatColor.BLUE + "Punished By: " + ChatColor.RESET + Bukkit.getOfflinePlayer(punisher).getName(),
                        ChatColor.BLUE + "Reason: " + ChatColor.WHITE + reason,
                        ChatColor.BLUE + "Duration: " + ChatColor.WHITE + (perm || duration == -2L ? "Forever" : TimeUtil.formatTimeMillis(duration, false, true) + (time + duration < System.currentTimeMillis() && duration != -1L ? ChatColor.GRAY + " (Expired)" : "")),
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-------------------------------",
                        ChatColor.RED.toString() + ChatColor.BOLD + "RIGHT CLICK TO DELETE THIS PUNISHMENT",
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-------------------------------"
                ).toItemStack();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (!clickType.isRightClick()) return;

        HistoryDeleteMenu historyDeleteMenu = new HistoryDeleteMenu(uuid, punishmentObject);
        historyDeleteMenu.openMenu(player);
    }
}