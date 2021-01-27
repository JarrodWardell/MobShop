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

import org.apache.commons.lang3.text.WordUtils;

public class ShopInventoryEvents implements Listener {
    private Inventory invent;
    private Inventory editInvent;
    private Mobshop mobShop;

    ShopInventoryEvents(Inventory invent, Inventory editInvent, Mobshop handler) {
        mobShop = handler;
        this.invent = Bukkit.createInventory(null, invent.getSize(), invent.getName());
        this.invent.setContents(invent.getContents());
        this.editInvent = Bukkit.createInventory(null, editInvent.getSize());
        this.editInvent.setContents(editInvent.getContents());
    }

    public void updateInventoryContents(Inventory invent) {
        this.editInvent.setContents(invent.getContents());
        for (int i = 0; i < this.invent.getSize(); i++) { // update the inventory with the top half of the edit inventory
            this.invent.setItem(i, editInvent.getItem(i));
        }
    }

    public void updateInventoryName(String newName) {
        Inventory toSet = Bukkit.createInventory(null, invent.getSize(), newName);
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

        ItemStack clicked = e.getCurrentItem();
        ItemStack toSell = editInvent.getItem(e.getRawSlot() + invent.getSize());

        if (action.equals("buy")) {
            if (mobShop.getCoins(player) >= NBTHelper.getNBTInt(clicked, "mobshop.price")) {
                ItemStack toGive = new ItemStack(toSell);
                toGive.setAmount(NBTHelper.getNBTInt(clicked, "mobshop.amount"));
                int space = 0;
                for (int i = 0; i < 36; i++) {
                    if (player.getInventory().getItem(i) == null || player.getInventory().getItem(i).getType() == Material.AIR) {
                        space += toGive.getMaxStackSize();
                        continue;
                    }
                    if (player.getInventory().getItem(i).isSimilar(toGive)) space += toGive.getMaxStackSize() - player.getInventory().getItem(i).getAmount();
                }
                if (space >= toGive.getAmount()) {
                    mobShop.removeCoins(player, NBTHelper.getNBTInt(clicked, "mobshop.price"));
                    player.getInventory().addItem(toGive);
                    player.sendMessage(ChatColor.YELLOW + "Purchased " + NBTHelper.getNBTInt(clicked, "mobshop.amount") + "x " + (toGive.getItemMeta().hasDisplayName() ? toGive.getItemMeta().getDisplayName() : WordUtils.capitalize(toGive.getType().name().replace("_", " ").toLowerCase())) + " for " + NBTHelper.getNBTInt(clicked, "mobshop.price") + " coins!");
                } else {
                    player.sendMessage(ChatColor.YELLOW + "Your inventory is too full for that!");
                }
            } else {
                player.sendMessage(ChatColor.YELLOW + "You don't have enough coins for that! It costs " + NBTHelper.getNBTInt(clicked, "mobshop.price") + " coins!");
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
