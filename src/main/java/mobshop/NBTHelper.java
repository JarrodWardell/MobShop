package mobshop;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;

public class NBTHelper { // leaving these as static because, well, I don't want to create a new object to just check something

    // Helper function to see if the stack has a specific custom tag
    public static boolean hasNBT(ItemStack item, String tag) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        return nmsItem.hasTag() && nmsItem.getTag().hasKey(tag);
    }

    // Helper function to read an Integer NBT tag
    public static Integer getNBTInt(ItemStack item, String tag) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        return nmsItem.hasTag() ? (nmsItem.getTag().hasKey(tag) ? nmsItem.getTag().getInt(tag) : 0) : 0;
    }

    // Helper function to read an Integer NBT tag
    public static Boolean getNBTBool(ItemStack item, String tag) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        return nmsItem.hasTag() ? (nmsItem.getTag().hasKey(tag) ? nmsItem.getTag().getBoolean(tag) : false) : false;
    }

    // Helper function to read an String NBT tag
    public static String getNBTString(ItemStack item, String tag) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        return nmsItem.hasTag() ? (nmsItem.getTag().hasKey(tag) ? nmsItem.getTag().getString(tag) : "") : "";
    }

    // Helper function to remove a specific NBT tag outright
    public static ItemStack removeNBT(ItemStack item, String tag) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound comp = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        comp.remove(tag);
        nmsItem.setTag(comp);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    // Helper function to make or set an NBT tag to supplied value
    public static ItemStack setNBTInt(ItemStack item, String tag, int value) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound comp = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        comp.setInt(tag, value);
        nmsItem.setTag(comp);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    // Helper function to make or set an NBT tag to supplied value
    public static ItemStack setNBTBool(ItemStack item, String tag, Boolean value) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound comp = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        comp.setBoolean(tag, value);
        nmsItem.setTag(comp);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    // Helper function to make or set an NBT tag to supplied value
    public static ItemStack setNBTString(ItemStack item, String tag, String value) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound comp = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        comp.setString(tag, value);
        nmsItem.setTag(comp);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }
}
