package us.blockgame.cloud.grant.menu.grant.reason.button;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import us.blockgame.cloud.CloudPlugin;
import us.blockgame.cloud.grant.menu.grant.confirmation.GrantConfirmationMenu;
import us.blockgame.cloud.rank.Rank;
import us.blockgame.fabric.menu.Button;
import us.blockgame.fabric.util.ItemBuilder;

import java.util.UUID;

@AllArgsConstructor
public class GrantReasonReasonButton extends Button {

    private UUID uuid;
    private Rank rank;
    private long duration;
    private Rank playerRank;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.PAPER).setName(ChatColor.YELLOW + "Provide Reason").setLore(ChatColor.GRAY + "Provide a reason for this grant.").toItemStack();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        player.closeInventory();
        player.beginConversation(new ConversationFactory(CloudPlugin.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {

            public String getPromptText(ConversationContext context) {

                return ChatColor.YELLOW + "Please enter the reason for granting " + playerRank.getLitePrefix() + Bukkit.getOfflinePlayer(uuid).getName() + ChatColor.YELLOW + " the " + rank.getLitePrefix() + rank.getDisplayName() + ChatColor.YELLOW + " rank...";
            }

            @Override
            public Prompt acceptInput(ConversationContext cc, String reason) {
                GrantConfirmationMenu grantConfirmationMenu = new GrantConfirmationMenu(uuid, rank, reason, duration);
                grantConfirmationMenu.openMenu(player);
                return null;
            }

        }).withLocalEcho(false).buildConversation(player));
    }

}
