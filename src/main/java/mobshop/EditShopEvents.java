package mobshop;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class EditShopEvents implements Listener {
    private Inventory invent;
    private ShopInventoryEvents handler;

    EditShopEvents (Inventory invent, ShopInventoryEvents handler) {
        this.invent = invent;
        this.handler = handler;
    }

    public void openEditingInventory(Player player) {
        player.openInventory(invent);
    }

    public Inventory getInventory() {
        return invent;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!e.getInventory().equals(invent)) return;
        handler.updateInventoryContents(invent);
    }

}
