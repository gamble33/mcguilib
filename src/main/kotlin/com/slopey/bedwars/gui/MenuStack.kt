package com.slopey.bedwars.gui

import com.slopey.bedwars.Bedwars
import com.slopey.bedwars.listeners.RightClickListener
import org.bukkit.entity.Player
import java.util.*

class MenuStack(private val player: Player, private val plugin: Bedwars) {
    val rightClickListener = RightClickListener()
    private val menuStack: Stack<HotbarMenu> = Stack()

    init {
        plugin.server.pluginManager.registerEvents(rightClickListener, plugin)
        player.inventory.clear()
        plugin.inventoryLocker.activePlayers.add(player.uniqueId)
    }

    fun pop() {
        val menu = menuStack.pop();
        menu?.deactivate()

        if (menuStack.isEmpty()) {
            plugin.inventoryLocker.activePlayers.remove(player.uniqueId)
            return;
        } else {
            menuStack.peek()?.activate()
        }
    }

    fun push(menu: HotbarMenu) {
        menuStack.push(menu)
        menu.activate()
    }
}