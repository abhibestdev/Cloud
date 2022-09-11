package us.blockgame.cloud.util;

import org.bukkit.ChatColor;

public class Color2Util {

    public static ChatColor getChatColor(String s) {
        String pre = ChatColor.translateAlternateColorCodes('&', s)
                .replace("§o", "")
                .replace("§l", "")
                .replace("§k", "")
                .replace("§m", "")
                .replace("§n", "")
                .replace("§r", "");

        String color = ChatColor.getLastColors(pre);
        return color.length() > 1 ? ChatColor.getByChar(color.charAt(color.length() - 1)) : ChatColor.WHITE;
    }
}
