package us.blockgame.cloud.rank;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import us.blockgame.cloud.CloudPlugin;
import us.blockgame.cloud.player.CloudPlayerHandler;
import us.blockgame.fabric.redis.event.RedisReceieveEvent;

import java.util.Map;

public class CloudRankListener implements Listener {

    @EventHandler
    public void onRedis(RedisReceieveEvent event) {
        Map<String, Object> messageMap = event.getMessageMap();

        String type = (String) messageMap.get("type");

        CloudRankHandler cloudRankHandler = CloudPlugin.getInstance().getCloudRankHandler();
        CloudPlayerHandler cloudPlayerHandler = CloudPlugin.getInstance().getCloudPlayerHandler();

        switch (type) {
            case "rank-add-perm": {
                String rankName = (String) messageMap.get("rank");
                String permission = (String) messageMap.get("permission");

                Rank rank = Rank.getRank(rankName);

                //Add permission to rank
                if (!rank.getPermissions().contains(permission)) {
                    rank.getPermissions().add(permission);
                }

                //Update permissions
                cloudRankHandler.affectedPlayersOfRankOrAboveAction(rank, p -> cloudPlayerHandler.updatePermissions(p));
                break;
            }
            case "rank-del-perm": {
                String rankName = (String) messageMap.get("rank");
                String permission = (String) messageMap.get("permission");

                Rank rank = Rank.getRank(rankName);

                //Remove permission from rank
                if (rank.getPermissions().contains(permission)) {
                    rank.getPermissions().remove(permission);
                }
                //Update permissions
                cloudRankHandler.affectedPlayersOfRankOrAboveAction(rank, p -> cloudPlayerHandler.updatePermissions(p));
                break;
            }
        }
    }

}
