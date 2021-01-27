package mobshop;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class GiveCommand implements CommandExecutor {
    private Mobshop mobShop;

    GiveCommand (Mobshop mobShop) {
        this.mobShop = mobShop;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length == 2 && sender.hasPermission("mobshop.add.others") && Bukkit.getPlayer(args[0]) != null) {
            try {
                Integer.parseInt(args[1]);
            } catch (Exception e) {
                sender.sendMessage(ChatColor.YELLOW + "The amount you're giving must be an Integer! (Max 2,147,483,647)");
                return false;
            }
            if (sender instanceof Player && sender.getName().equalsIgnoreCase(args[0])) { // check if player is giving themselves coins
                sender.sendMessage(ChatColor.YELLOW + "You can't give yourself coins!");
                return true;
            }
            if (Bukkit.getPlayer(args[0]) != null) {
                if (mobShop.getCoins((Player)sender) >= Integer.parseInt(args[1])) {
                    if (Integer.parseInt(args[1]) <= 0) {
                        sender.sendMessage(ChatColor.YELLOW + "You must give more than 0 coins!");
                        return true;
                    }
                    if (mobShop.getCoins(Bukkit.getPlayer(args[0])) + Integer.parseInt(args[1]) < mobShop.getCoins(Bukkit.getPlayer(args[0]))) {
                        sender.sendMessage(ChatColor.YELLOW + "Unfortunately, this user is too rich, and as such cannot take your coins.");
                        return true;
                    }
                    mobShop.removeCoins((Player)sender, Integer.parseInt(args[1]));
                    mobShop.addCoins(Bukkit.getPlayer(args[0]), Integer.parseInt(args[1]));
                    sender.sendMessage(ChatColor.YELLOW + "Given " + args[1] + " coins to " + Bukkit.getPlayer(args[0]).getDisplayName() + ChatColor.YELLOW + ".");
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "You don't have that many coins.");
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
