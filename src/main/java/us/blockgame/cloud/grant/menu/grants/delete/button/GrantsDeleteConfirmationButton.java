package us.blockgame.cloud.grant.menu.grants.delete.button;

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
import us.blockgame.fabric.FabricPlugin;
import us.blockgame.fabric.menu.Button;
import us.blockgame.fabric.request.RequestResponse;
import us.blockgame.fabric.util.DateUtil;
import us.blockgame.fabric.util.ItemBuilder;
import us.blockgame.fabric.util.TimeUtil;

import java.util.UUID;

@AllArgsConstructor
public class GrantsDeleteConfirmationButton extends Button {

    private UUID uuid;
    private Grant grant;

    @Override
    public ItemStack getButtonItem(Player player) {

        return new ItemBuilder(Material.LIME_DYE)
                .setName(ChatColor.GREEN + "Confirm Deletion")
                .setLore(
                        ChatColor.YELLOW + "Delete Grant from " + ChatColor.GOLD + DateUtil.millisToDate(grant.getTime()) + ChatColor.YELLOW + ": ",
                        ChatColor.GOLD + " * " + ChatColor.YELLOW + "Granted By: " + ChatColor.RESET + Bukkit.getOfflinePlayer(grant.getUuid()).getName(),
                        ChatColor.GOLD + " * " + ChatColor.YELLOW + "Duration: " + ChatColor.GOLD + TimeUtil.formatTimeMillis(grant.getDuration(), false, true),
                        ChatColor.GOLD + " * " + ChatColor.YELLOW + "Reason: " + ChatColor.GOLD + grant.getReason())
                .toItemStack();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {

        player.closeInventory();

        RequestResponse requestResponse = FabricPlugin.getInstance().getFabricRequestHandler().post("grant", ImmutableMap.of("action", "delete", "uuid", uuid.toString(), "grantTime", String.valueOf(grant.getTime())));
        if (!requestResponse.isSuccessful()) {
            player.sendMessage(requestResponse.getErrorMessage());
            return;
        }

        Player target = Bukkit.getPlayer(uuid);

        //Delete the grant from their saved list of grants if they are online
        if (target != null) {
            CloudProfileHandler cloudProfileHandler = CloudPlugin.getInstance().getCloudProfileHandler();
            CloudProfile cloudProfile = cloudProfileHandler.getProfile(target);

            Grant foundGrant = cloudProfile.getGrantList().stream().filter(g -> g.getTime() == grant.getTime()).findFirst().orElse(null);

            //Remove found grant
            if (foundGrant != null) cloudProfile.getGrantList().remove(foundGrant);
        }

        player.sendMessage(ChatColor.YELLOW + "You have successfully deleted a grant.");
    }
}
