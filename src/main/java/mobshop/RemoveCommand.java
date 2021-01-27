package mobshop;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class RemoveCommand implements CommandExecutor {
    private Mobshop mobShop;

    RemoveCommand (Mobshop mobShop) {
        this.mobShop = mobShop;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            Integer.parseInt(args[0]);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.YELLOW + "The amount you're taking must be an Integer! (Max 2,147,483,647)");
            return false;
        }
        if (sender instanceof Player && args.length == 1 && sender.hasPermission("mobshop.remove.self")) {
            mobShop.removeCoins((Player)sender, Integer.parseInt(args[0]));
            sender.sendMessage(((Player)sender).getDisplayName() + ChatColor.YELLOW + " had " + args[0] + " coins taken. They now have " + mobShop.getCoins(((Player)sender)) + " Mob coins.");
            return true;
        } else if (args.length == 2 && sender.hasPermission("mobshop.remove.others") && Bukkit.getPlayer(args[1]) != null) {
            if (sender instanceof Player && sender.getName().equalsIgnoreCase(args[1]) && !sender.hasPermission("mobshop.remove.self")) { // check if player is trying to increment their coins without permission
                return false;
            }
            if (Bukkit.getPlayer(args[1]) != null) {
                mobShop.removeCoins(Bukkit.getPlayer(args[1]), Integer.parseInt(args[0]));
                sender.sendMessage(Bukkit.getPlayer(args[1]).getDisplayName() + ChatColor.YELLOW + " had " + args[0] + " coins taken. They now have " + mobShop.getCoins(Bukkit.getPlayer(args[1])) + " Mob coins.");
                return true;
            }
        }
        if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender) {
            sender.sendMessage("You can't use commands on yourself if you aren't a player!");
        }
        return false;
    }
}
