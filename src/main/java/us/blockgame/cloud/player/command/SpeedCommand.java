package us.blockgame.cloud.player.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockgame.fabric.command.framework.Command;
import us.blockgame.fabric.command.framework.Param;

public class SpeedCommand {

    @Command(name = "speed", permission = "op", inGameOnly = true)
    public void speed(CommandSender sender, @Param(name = "speed") Integer multiplier, @Param(name = "player", optional = true) Player target) {

        //Make sure entered speed is within the speed range we use
        if (multiplier <= 0 || multiplier > 10) {
            sender.sendMessage(ChatColor.RED + "Please enter a speed between 1 and 10.");
            return;
        }
        float speed = !target.isFlying() ? 0.2f * ((multiplier + 1) / 2) : 0.1f * multiplier;

        //Set player's fly speed
        if (target.isFlying()) {
            target.setFlySpeed(speed);

            if (target != sender) {
                sender.sendMessage(ChatColor.GREEN + target.getName() + "'s fly speed has been set to " + multiplier + ".");
            }

            target.sendMessage(ChatColor.GREEN + "Your flight speed has been set to " + multiplier + ".");
            return;
        }
        //Set player's walk speed
        target.setWalkSpeed(speed);

        if (target != sender) {
            sender.sendMessage(ChatColor.GREEN + target.getName() + "'s walk speed has been set to " + multiplier + ".");
        }

        target.sendMessage(ChatColor.GREEN + "Your walk speed has been set to " + multiplier + ".");
    }
}
