package com.wuyumoom.yucore.view

import com.wuyumoom.yucore.api.ItemStackAPI
import com.wuyumoom.yucore.file.view.ViewConfiguration
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

open class GuiSession(
    val viewConfiguration: ViewConfiguration,
    val player: Player,
    val title: String = viewConfiguration.title,
) : InventoryHolder {
    private val inventory = Bukkit.createInventory(this, viewConfiguration.size, title)
    override fun getInventory(): Inventory {
        return inventory
    }
    init {
        viewConfiguration.button.forEach { (name, button) ->
            if (button.load) {
                button.slot.forEach { slot ->
                    inventory.setItem(slot, button.itemStack)
                }
            }
        }
    }
    /**
     * 布局界面
     */
    fun draw(){

    }

    /**
     * 添加按钮
     */
    fun addButton(slot: IntArray, itemStack: ItemStack) {
        slot.forEach {
            inventory.setItem(it, itemStack)
        }
    }

    /**
     * 执行按钮指令
     */
    private fun executeCommand() {
        onClick { event ->
            val currentItem = event.currentItem ?: return@onClick
            val nbt = ItemStackAPI.getNBT(currentItem, "yubutton") ?: return@onClick
            val button = viewConfiguration.button[nbt] ?: return@onClick
            button.cmd.forEach { cmd ->
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.name))
            }
        }

    }

    /**
     * 打开界面
     */
    fun open() {
        executeCommand()
        draw()
        player.openInventory(inventory)
    }

    /**
     * 关闭界面
     */
    fun close() {
        inventory.clear()
        player.closeInventory()
    }

    /**
     * 关闭回调
     */
    internal val closeCallbacks = mutableListOf<() -> Unit>()
    fun onClose(callback: () -> Unit) = apply { closeCallbacks += callback }

    /**
     * 打开界面的回调
     */
    internal val openCallbacks = mutableListOf<() -> Unit>()
    fun onOpen(callback: () -> Unit) = apply { openCallbacks += callback }


    /**
     * 点击菜单的回调
     */
    internal val clickCallbacks = mutableListOf<(InventoryClickEvent) -> Unit>()
    fun onClick(callback: (InventoryClickEvent) -> Unit) = apply { clickCallbacks += callback }

}