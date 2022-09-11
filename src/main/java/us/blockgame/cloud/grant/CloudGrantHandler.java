package us.blockgame.cloud.grant;

import com.google.common.collect.ImmutableMap;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import us.blockgame.cloud.CloudPlugin;
import us.blockgame.cloud.grant.command.ConsoleGrantCommand;
import us.blockgame.cloud.grant.command.GrantCommand;
import us.blockgame.cloud.grant.command.GrantsCommand;
import us.blockgame.cloud.grant.task.CloudGrantTask;
import us.blockgame.cloud.rank.CloudRankHandler;
import us.blockgame.cloud.rank.Rank;
import us.blockgame.fabric.FabricPlugin;
import us.blockgame.fabric.command.FabricCommandHandler;
import us.blockgame.fabric.request.RequestResponse;
import us.blockgame.fabric.util.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class CloudGrantHandler {

    public CloudGrantHandler() {
        //Register commands
        FabricCommandHandler fabricCommandHandler = FabricPlugin.getInstance().getFabricCommandHandler();
        fabricCommandHandler.registerCommand(new GrantCommand());
        fabricCommandHandler.registerCommand(new GrantsCommand());
        fabricCommandHandler.registerCommand(new ConsoleGrantCommand());

        //Register task
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(CloudPlugin.getInstance(), new CloudGrantTask(), 1L, 1L);
    }

    @SneakyThrows
    public List<Grant> getGrantsFromMongo(UUID uuid) {
        CloudRankHandler cloudRankHandler = CloudPlugin.getInstance().getCloudRankHandler();

        List<Grant> grantList = new ArrayList<>();

        RequestResponse requestResponse = FabricPlugin.getInstance().getFabricRequestHandler().get("grant", ImmutableMap.of("uuid", uuid.toString()));
        if (!requestResponse.isSuccessful()) {
            Logger.error(CloudPlugin.getInstance(), "Could not fetch grants for " + uuid.toString() + ": " + requestResponse.getErrorMessage());
            return grantList;
        }

        JSONObject grants = (JSONObject) requestResponse.getResponse().get("grants");

        //Get grant data
        grants.keySet().forEach(key -> {

            JSONObject grant = (JSONObject) grants.get(key);

            long grantTime = Long.parseLong(key.toString());
            long duration = Long.parseLong((String) grant.get("duration"));
            String reason = (String) grant.get("reason");
            Rank rank = Rank.getRank((String) grant.get("rank"));
            UUID executor = UUID.fromString((String) grant.get("uuid"));

            grantList.add(new Grant(grantTime, duration, reason, rank, executor, cloudRankHandler.getOfflineRank(executor)));
        });

        //Puts all grants in ascending order
        grantList.sort(Comparator.comparing(Grant::getTime));

        return grantList;
    }
}
