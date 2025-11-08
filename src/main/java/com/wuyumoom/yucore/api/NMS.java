package com.wuyumoom.yucore.api;

import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;

public class NMS {

    public static ItemStack getMNSItemStack(Object itemStack) {
        return CraftItemStack.asNMSCopy((org.bukkit.inventory.ItemStack)itemStack);
    }

    public static org.bukkit.inventory.ItemStack getMNSFaItemStack(Object defaultStack) {
        return CraftItemStack.asBukkitCopy((ItemStack) defaultStack);
    }
}
