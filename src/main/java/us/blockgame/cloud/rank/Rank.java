package us.blockgame.cloud.rank;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Rank {

    OWNER("Owner", "Owner", "&4[Owner] &c", "&4", true, 0),
    ADMIN("Admin", "Admin", "&2[Admin] &a", "&2", true, 1),
    MOD("Moderator", "Moderator", "&3[Mod] &b", "&3", true, 2),
    CHAT_MOD("Chat-Mod", "Chat-Mod", "&3[Chat-Mod] &b&o", "&b&o", true, 3),
    DEFAULT("Default", "Default", "&f", "&f", false, 999);

    @Getter private String name;
    @Getter private String displayName;
    private String prefix;
    private String litePrefix;
    @Getter private boolean staff;
    @Getter private int weight;
    @Setter @Getter private List<String> permissions;

    Rank(String name, String displayName, String prefix, String litePrefix, boolean staff, int weight) {
        this.name = name;
        this.displayName = displayName;
        this.prefix = prefix;
        this.litePrefix = litePrefix;
        this.staff = staff;
        this.weight = weight;

        //Set permissions to empty list
        permissions = new ArrayList<>();
    }

    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', prefix);
    }

    public String getLitePrefix() {
        return ChatColor.translateAlternateColorCodes('&', litePrefix);
    }

    public static Rank getRank(String name) {
        return Arrays.stream(values()).filter(r -> r.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
