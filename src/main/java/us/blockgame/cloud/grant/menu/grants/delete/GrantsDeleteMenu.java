package us.blockgame.cloud.grant.menu.grants.delete;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.blockgame.cloud.grant.Grant;
import us.blockgame.cloud.grant.menu.grants.delete.button.GrantsDeleteCancelButton;
import us.blockgame.cloud.grant.menu.grants.delete.button.GrantsDeleteConfirmationButton;
import us.blockgame.fabric.menu.Button;
import us.blockgame.fabric.menu.Menu;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class GrantsDeleteMenu extends Menu {

    private UUID uuid;
    private Grant grant;

    @Override
    public String getTitle(Player player) {
        return ChatColor.BLUE.toString() + ChatColor.BOLD + "Confirm Grant Deletion";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = Maps.newHashMap();

        //Set menu items
        buttonMap.put(3, new GrantsDeleteConfirmationButton(uuid, grant));
        buttonMap.put(5, new GrantsDeleteCancelButton());

        return buttonMap;
    }
}
