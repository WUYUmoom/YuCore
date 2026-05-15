package com.wuyumoom.yucore.view

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent

class ClickEvent: Listener {
    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        val holder = event.whoClicked.openInventory.topInventory.holder
        if (holder is GuiSession) {
            for (callback in holder.clickCallbacks) {
                callback.invoke(event)
            }
        }
    }

    @EventHandler
    fun onClose(event: InventoryOpenEvent) {
        val holder = event.player.openInventory.topInventory.holder
        if (holder is GuiSession) {
            for (callback in holder.closeCallbacks) {
                callback.invoke()
            }
        }
    }

    @EventHandler
    fun onOpen(event: InventoryOpenEvent) {
        val holder = event.player.openInventory.topInventory.holder
        if (holder is GuiSession) {
            for (callback in holder.openCallbacks) {
                callback.invoke()
            }
        }
    }
}