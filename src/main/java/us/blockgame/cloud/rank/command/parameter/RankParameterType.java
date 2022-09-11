package us.blockgame.cloud.rank.command.parameter;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import us.blockgame.cloud.rank.Rank;
import us.blockgame.fabric.command.framework.ParameterType;

public class RankParameterType implements ParameterType<Rank> {

    @Override
    public Rank transform(CommandSender sender, String source) {
        Rank rank = Rank.getRank(source);

        if (rank == null) {
            sender.sendMessage(ChatColor.RED + "No rank with the name \"" + source + "\" found.");
            return null;
        }
        return rank;
    }
}
