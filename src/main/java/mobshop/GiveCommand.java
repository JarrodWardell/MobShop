package mobshop;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.apache.commons.lang3.math.NumberUtils;

public class GiveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length == 2 && sender.hasPermission("mobshop.add.others") && NumberUtils.isParsable(args[1]) && Bukkit.getPlayer(args[0]) != null) {
            if (sender instanceof Player && sender.getName().equalsIgnoreCase(args[0])) { // check if player is giving themselves coins
                sender.sendMessage(ChatColor.YELLOW + "You can't give yourself coins!");
                return true;
            }
            if (Bukkit.getPlayer(args[0]) != null) {
                if (Mobshop.getCoins((Player)sender) >= Integer.parseInt(args[1])) {
                    Mobshop.removeCoins((Player)sender, Integer.parseInt(args[1]));
                    Mobshop.addCoins(Bukkit.getPlayer(args[0]), Integer.parseInt(args[1]));
                    sender.sendMessage(ChatColor.YELLOW + "Given " + args[1] + " coins to " + Bukkit.getPlayer(args[0]).getDisplayName() + ChatColor.YELLOW + ".");
                }
                return true;
            }
        }
        if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender) {
            sender.sendMessage("You can't use commands on yourself if you aren't a player!");
        }
        return false;
    }
}
