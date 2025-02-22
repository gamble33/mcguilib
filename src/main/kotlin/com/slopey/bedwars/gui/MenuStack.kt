package com.slopey.bedwars.gui

import com.slopey.bedwars.Bedwars
import com.slopey.bedwars.listeners.RightClickListener
import org.bukkit.entity.Player
import java.util.*

class MenuStack(player: Player, plugin: Bedwars) {
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
    }

    fun push(menu: HotbarMenu) {
        menuStack.push(menu)
        menu.activate()
    }
}