package mobshop;

import org.bukkit.command.CommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalCommand implements CommandExecutor {
    private Mobshop mobShop;

    BalCommand (Mobshop mobShop) {
        this.mobShop = mobShop;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && sender.hasPermission("mobshop.get.others")) {
            if (sender instanceof Player && sender.getName().equalsIgnoreCase(args[0]) && !sender.hasPermission("mobshop.get.self")) { // check if player is trying to check their coins without permission
                return false;
            }
            if (Bukkit.getPlayer(args[0]) != null) {
                sender.sendMessage(Bukkit.getPlayer(args[0]).getDisplayName() + ChatColor.YELLOW + " has " + mobShop.getCoins(Bukkit.getPlayer(args[0])) + " Mob coins.");
                return true;
            }
        }
        if (sender instanceof Player && sender.hasPermission("mobshop.get.self")) {
            sender.sendMessage(((Player)sender).getDisplayName() + ChatColor.YELLOW + " has " + mobShop.getCoins((Player)sender) + " Mob coins.");
            return true;
        } else {
            sender.sendMessage("You can't use commands on yourself if you aren't a player!");
        }
        return false;
    }
}
