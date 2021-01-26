package mobshop;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class ShopInventoryEvents implements Listener {
    private Inventory invent;
    private Mobshop mobShop;

    ShopInventoryEvents(Inventory invent, Mobshop handler) {
        mobShop = handler;
        this.invent = Bukkit.createInventory(null, invent.getSize(), invent.getName());
        this.invent.setContents(invent.getContents());
    }

    public void updateInventoryContents(Inventory invent) {
        this.invent.setContents(invent.getContents());
    }

    public void updateInventoryName(String newName) {
        Inventory toSet = Bukkit.createInventory(null, 27, newName);
        toSet.setContents(invent.getContents());
        invent = toSet;
    }

    public void openInventory(Player player) {
        player.openInventory(invent);
    }

    public Inventory getInventory() {
        return invent;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!e.getInventory().equals(invent)) return;
        e.setCancelled(true);
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;

        Player player = (Player) e.getWhoClicked();

        String action = NBTHelper.getNBTString(e.getCurrentItem(), "mobshop.action");
        if (action.equals("")) return; // do nothing

        ItemStack item = e.getCurrentItem();

        if (action.equals("buy")) {
            if (mobShop.getCoins(player) >= NBTHelper.getNBTInt(item, "mobshop.price")) {
                ItemStack toGive = new ItemStack(NBTHelper.removeNBT(NBTHelper.removeNBT(NBTHelper.removeNBT(item, "mobshop.action"), "mobshop.amount"), "mobshop.price"));
                toGive.setAmount(NBTHelper.getNBTInt(item, "mobshop.amount"));
                int space = 0;
                for (ItemStack invItem : player.getInventory().getContents() ) {
                    if (invItem.isSimilar(toGive)) space += toGive.getMaxStackSize() - invItem.getAmount();
                    if (invItem == null || invItem.getType() == Material.AIR) space += toGive.getMaxStackSize();
                }
                if (space >= toGive.getAmount()) {
                    mobShop.removeCoins(player, NBTHelper.getNBTInt(item, "mobshop.price"));
                    player.getInventory().addItem(toGive);
                } else {
                    player.sendMessage(ChatColor.YELLOW + "Your inventory is too full for that!");
                }
            } else {
                player.sendMessage(ChatColor.YELLOW + "You don't have enough coins for that! It costs " + NBTHelper.getNBTInt(item, "mobshop.price") + " coins!");
            }
        } else if (action.equals("sell")) {
            // 
        } else if (action.equals("close")) {
            player.closeInventory();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryDragEvent e) {
        if (e.getInventory().equals(invent)) e.setCancelled(true);
    }

}
