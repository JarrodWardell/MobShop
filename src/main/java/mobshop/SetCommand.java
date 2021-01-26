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

public class SetCommand implements CommandExecutor {
    private Mobshop mobShop;

    SetCommand (Mobshop mobShop) {
        this.mobShop = mobShop;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length == 1 && sender.hasPermission("mobshop.set.self") && NumberUtils.isParsable(args[0])) {
            mobShop.setCoins((Player)sender, Integer.parseInt(args[0]));
            sender.sendMessage(((Player)sender).getDisplayName() + ChatColor.YELLOW + "'s coin balance was set to " + args[0] + " coins.");
            return true;
        } else if (args.length == 2 && sender.hasPermission("mobshop.set.others") && NumberUtils.isParsable(args[0]) && Bukkit.getPlayer(args[1]) != null) {
            if (sender instanceof Player && sender.getName().equalsIgnoreCase(args[1]) && !sender.hasPermission("mobshop.set.self")) { // check if player is trying to set their coins without permission
                return false;
            }
            if (Bukkit.getPlayer(args[1]) != null) {
                mobShop.setCoins(Bukkit.getPlayer(args[1]), Integer.parseInt(args[0]));
                sender.sendMessage(Bukkit.getPlayer(args[1]).getDisplayName() + ChatColor.YELLOW + "'s coin balance was set to' " + args[0] + " coins.");
                return true;
            }
        }
        if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender) {
            sender.sendMessage("You can't use commands on yourself if you aren't a player!");
        }
        return false;
    }
}
