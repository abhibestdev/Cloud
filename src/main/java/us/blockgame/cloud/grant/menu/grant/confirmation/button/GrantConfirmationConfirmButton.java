package us.blockgame.cloud.grant.menu.grant.confirmation.button;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import us.blockgame.cloud.CloudPlugin;
import us.blockgame.cloud.grant.Grant;
import us.blockgame.cloud.profile.CloudProfile;
import us.blockgame.cloud.profile.CloudProfileHandler;
import us.blockgame.cloud.rank.CloudRankHandler;
import us.blockgame.cloud.rank.Rank;
import us.blockgame.fabric.FabricPlugin;
import us.blockgame.fabric.menu.Button;
import us.blockgame.fabric.request.RequestResponse;
import us.blockgame.fabric.util.ItemBuilder;
import us.blockgame.fabric.util.TimeUtil;

import java.util.UUID;

@AllArgsConstructor
public class GrantConfirmationConfirmButton extends Button {

    private UUID uuid;
    private Rank rank;
    private String reason;
    private long duration;

    @Override
    public ItemStack getButtonItem(Player player) {

        return new ItemBuilder(Material.LIME_DYE)
                .setName(ChatColor.GREEN + "Confirm Grant")
                .setLore(
                        ChatColor.YELLOW + "Grant " + ChatColor.RESET + Bukkit.getOfflinePlayer(uuid).getName() + ChatColor.YELLOW + " the " + rank.getLitePrefix() + rank.getDisplayName() + ChatColor.YELLOW + " rank",
                        ChatColor.GOLD + " * " + ChatColor.YELLOW + "Duration: " + ChatColor.GOLD + TimeUtil.formatTimeMillis(duration, false, true),
                        ChatColor.GOLD + " * " + ChatColor.YELLOW + "Reason: " + ChatColor.GOLD + reason
                )
                .toItemStack();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {

        Player target = Bukkit.getPlayer(uuid);

        long grantTime = System.currentTimeMillis();

        RequestResponse requestResponse = FabricPlugin.getInstance().getFabricRequestHandler().post("grant", ImmutableMap.of("uuid", target.getUniqueId().toString(), "rank", rank.getName(), "reason", reason, "duration", String.valueOf(duration), "senderUUID", player.getUniqueId().toString(), "grantTime", String.valueOf(grantTime)));
        if (!requestResponse.isSuccessful()) {
            player.sendMessage(ChatColor.RED + requestResponse.getErrorMessage());
            return;
        }

        if (target != null) {
            CloudProfileHandler cloudProfileHandler = CloudPlugin.getInstance().getCloudProfileHandler();
            CloudProfile cloudProfile = cloudProfileHandler.getProfile(target);

            CloudRankHandler cloudRankHandler = CloudPlugin.getInstance().getCloudRankHandler();

            //Add grant to grants list and the grants task will automatically update the players rank
            cloudProfile.getGrantList().add(new Grant(grantTime, duration, reason, rank, uuid, cloudRankHandler.getOfflineRank(player.getUniqueId())));
        }
        player.closeInventory();

        player.sendMessage(ChatColor.YELLOW + "You have granted " + rank.getLitePrefix() + Bukkit.getOfflinePlayer(uuid).getName() + ChatColor.YELLOW + " the " + rank.getLitePrefix() + rank.getDisplayName() + ChatColor.YELLOW + " for " + ChatColor.GOLD + TimeUtil.formatTimeMillis(duration, false, true) + ChatColor.YELLOW + " for " + ChatColor.GOLD + reason + ChatColor.YELLOW + ".");
        return;
    }
}
