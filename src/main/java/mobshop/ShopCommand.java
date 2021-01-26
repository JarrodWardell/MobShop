package mobshop;

import org.bukkit.command.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import org.apache.commons.lang3.math.NumberUtils;

public class ShopCommand implements CommandExecutor {
    private ShopInventoryEvents handler;
    private EditShopEvents editHandler;

    ShopCommand(Mobshop mobShop, ShopInventoryEvents handler, EditShopEvents editHandler) {
        this.handler = handler;
        this.editHandler = editHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) { // this is disgusting, but I don't know a better way to go about it.
        if (args.length > 0 && args[0].equalsIgnoreCase("edit") && sender.hasPermission("mobshop.shop.edit")) {
            if (args.length > 1 && args[1].equalsIgnoreCase("item")) {
                ItemStack heldItem = ((Player)sender).getInventory().getItemInMainHand();
                if (args.length > 2 && args[2].equalsIgnoreCase("close") && heldItem.getType() != Material.AIR) {
                    ((Player)sender).getInventory().setItemInMainHand(NBTHelper.setNBTString(NBTHelper.removeNBT(NBTHelper.removeNBT(NBTHelper.removeNBT(NBTHelper.removeNBT(NBTHelper.removeNBT(heldItem, "mobshop.price"), "mobshop.amount"), "mobshop.action"), "mobshop.amount"), "mobshop.price"), "mobshop.action", "close"));
                    sender.sendMessage(ChatColor.YELLOW + "Converted held item to a closer.");
                    return true;
                } else if (args.length > 2 && args[2].equalsIgnoreCase("buy") && heldItem.getType() != Material.AIR && args.length > 4 && NumberUtils.isParsable(args[3]) && NumberUtils.isParsable(args[4])) {
                    ((Player)sender).getInventory().setItemInMainHand(NBTHelper.setNBTInt(NBTHelper.setNBTInt(NBTHelper.setNBTString(NBTHelper.removeNBT(NBTHelper.removeNBT(NBTHelper.removeNBT(NBTHelper.removeNBT(NBTHelper.removeNBT(heldItem, "mobshop.price"), "mobshop.amount"), "mobshop.action"), "mobshop.amount"), "mobshop.price"), "mobshop.action", "buy"), "mobshop.price", Integer.parseInt(args[3])), "mobshop.amount", Integer.parseInt(args[4])));
                    sender.sendMessage(ChatColor.YELLOW + "Converted held item to a sellable object.");
                    return true;
                }
                sender.sendMessage(ChatColor.YELLOW + "Invalid command usage!\nFor editing an item, you must do '/mobshop edit item [close/buy] [-/price (int)] [-/amount (int)]' while holding the item!");
                return true;
            } else if (args.length > 1 && args[1].equalsIgnoreCase("page")) {
                editHandler.openEditingInventory((Player)sender);
                return true;
            } else if (args.length > 2 && args[1].equalsIgnoreCase("setdisplayname") ) {
                handler.updateInventoryName(String.join(" ", args).replace(String.join(" ", args[0], args[1]) + " ", ""));
                sender.sendMessage(ChatColor.YELLOW + "Changed display name of page.");
                return true;
            }
            sender.sendMessage(ChatColor.YELLOW + "Invalid edit option!\nOptions are item, setdisplayname, or page.");
            return true;
        }
        handler.openInventory((Player)sender);
        return true;
    }
}
