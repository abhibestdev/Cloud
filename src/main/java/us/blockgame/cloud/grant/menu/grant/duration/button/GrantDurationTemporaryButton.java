package us.blockgame.cloud.grant.menu.grant.duration.button;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import us.blockgame.cloud.CloudPlugin;
import us.blockgame.cloud.grant.menu.grant.reason.GrantReasonMenu;
import us.blockgame.cloud.rank.Rank;
import us.blockgame.fabric.menu.Button;
import us.blockgame.fabric.util.ItemBuilder;
import us.blockgame.fabric.util.TimeUtil;

import java.util.UUID;

@AllArgsConstructor
public class GrantDurationTemporaryButton extends Button {

    private UUID uuid;
    private Rank rank;
    private Rank playerRank;

    @Override
    public ItemStack getButtonItem(Player player) {


        return new ItemBuilder(Material.PAPER)
                .setName(ChatColor.YELLOW + "Temporary Grant")
                .setLore(ChatColor.GRAY + "Click to grant " + Bukkit.getOfflinePlayer(uuid).getName() + " the " + rank.getDisplayName() + " rank temporarily.")
                .toItemStack();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        player.closeInventory();
        player.beginConversation(new ConversationFactory(CloudPlugin.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {

            public String getPromptText(ConversationContext context) {


                return ChatColor.YELLOW + "Please enter how long you would like to grant " + playerRank.getLitePrefix() + Bukkit.getOfflinePlayer(uuid).getName() + ChatColor.YELLOW + " the " + rank.getLitePrefix() + rank.getDisplayName() + ChatColor.YELLOW + " for...";
            }

            @Override
            public Prompt acceptInput(ConversationContext cc, String sDuration) {
                long duration = TimeUtil.parseTime(sDuration);

                GrantReasonMenu grantReasonMenu = new GrantReasonMenu(uuid, rank, duration, playerRank);
                grantReasonMenu.openMenu(player);
                return null;
            }

        }).withLocalEcho(false).buildConversation(player));
    }
}