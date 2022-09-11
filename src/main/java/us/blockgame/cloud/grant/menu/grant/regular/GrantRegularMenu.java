package us.blockgame.cloud.grant.menu.grant.regular;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.blockgame.cloud.grant.menu.grant.regular.button.GrantRegularRankButton;
import us.blockgame.cloud.rank.Rank;
import us.blockgame.fabric.menu.Button;
import us.blockgame.fabric.menu.Menu;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class GrantRegularMenu extends Menu {

    private UUID uuid;

    @Override
    public String getTitle(Player player) {
        return ChatColor.BLUE.toString() + ChatColor.BOLD + "Select a rank to grant";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = Maps.newHashMap();

        for (int i = 0; i < Rank.values().length; i++) {
            Rank rank = Rank.values()[i];

            buttonMap.put(i, new GrantRegularRankButton(uuid, rank));
        }

        return buttonMap;
    }
}
