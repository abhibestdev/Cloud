package us.blockgame.cloud.grant.menu.grants.regular;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.blockgame.cloud.CloudPlugin;
import us.blockgame.cloud.grant.CloudGrantHandler;
import us.blockgame.cloud.grant.Grant;
import us.blockgame.cloud.grant.menu.grants.regular.button.GrantsRegularGrantButton;
import us.blockgame.fabric.menu.Button;
import us.blockgame.fabric.menu.pagination.PaginatedMenu;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class GrantsRegularMenu extends PaginatedMenu {

    private UUID uuid;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return ChatColor.BLUE.toString() + ChatColor.BOLD + "Grant History " + ChatColor.GRAY.toString() + ChatColor.BOLD + "[" + getPage() + "/" + getPages(player) + "]";
    }

    @Override
    @SneakyThrows
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttonMap = Maps.newHashMap();

        CloudGrantHandler cloudGrantHandler = CloudPlugin.getInstance().getCloudGrantHandler();
        List<Grant> grantList = cloudGrantHandler.getGrantsFromMongo(uuid);

        //Set items in menu
        for (int i = 0; i < grantList.size(); i++) {
            Grant grant = grantList.get(i);

            buttonMap.put(i, new GrantsRegularGrantButton(uuid, grant));
        }

        return buttonMap;
    }
}
