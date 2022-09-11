package us.blockgame.cloud.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CorrespondingWool {

    BOLD(ChatColor.BOLD, Material.WHITE_WOOL),
    RESET(ChatColor.RESET, Material.WHITE_WOOL),
    WHITE(ChatColor.WHITE, Material.WHITE_WOOL),
    ORANGE(ChatColor.GOLD, Material.ORANGE_WOOL),
    LIGHT_PURPLE(ChatColor.LIGHT_PURPLE, Material.MAGENTA_WOOL),
    AQUA(ChatColor.AQUA, Material.LIGHT_BLUE_WOOL),
    YELLOW(ChatColor.YELLOW, Material.YELLOW_WOOL),
    GREEN(ChatColor.GREEN, Material.LIME_WOOL),
    GRAY(ChatColor.GRAY, Material.LIGHT_GRAY_WOOL),
    DARK_GRAY(ChatColor.DARK_GRAY, Material.GRAY_WOOL),
    DARK_AQUA(ChatColor.DARK_AQUA, Material.CYAN_WOOL),
    PURPLE(ChatColor.DARK_PURPLE, Material.PURPLE_WOOL),
    BLUE(ChatColor.BLUE, Material.BLUE_WOOL),
    DARK_GREEN(ChatColor.DARK_GREEN, Material.GREEN_WOOL),
    RED(ChatColor.RED, Material.RED_WOOL),
    DARK_RED(ChatColor.DARK_RED, Material.RED_WOOL),
    BLACK(ChatColor.BLACK, Material.BLACK_WOOL);

    private ChatColor chatColor;
    private Material wool;

    public static CorrespondingWool getByColor(ChatColor chatColor) {
        return Arrays.stream(CorrespondingWool.values()).filter(c -> c.getChatColor().equals(chatColor)).findFirst().orElse(null);
    }
}
