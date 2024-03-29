package us.blockgame.cloud.grant.menu.grant.reason;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.blockgame.cloud.grant.menu.grant.reason.button.GrantReasonNoReasonButton;
import us.blockgame.cloud.grant.menu.grant.reason.button.GrantReasonReasonButton;
import us.blockgame.cloud.rank.Rank;
import us.blockgame.fabric.menu.Button;
import us.blockgame.fabric.menu.Menu;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class GrantReasonMenu extends Menu {

    private UUID uuid;
    private Rank rank;
    private long duration;
    private Rank playerRank;

    @Override
    public String getTitle(Player player) {
        return ChatColor.BLUE.toString() + ChatColor.BOLD + "Select a grant reason";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = Maps.newHashMap();

        buttonMap.put(3, new GrantReasonReasonButton(uuid, rank, duration, playerRank));
        buttonMap.put(5, new GrantReasonNoReasonButton(uuid, rank, duration));

        return buttonMap;
    }
}