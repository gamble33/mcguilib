package com.slopey.bedwars.gui

import com.slopey.bedwars.listeners.RightClickListener
import org.bukkit.block.Block
import org.bukkit.entity.Player

class HotbarMenu(private val player: Player, private var selections: List<Selection>, private val rightClickListener: RightClickListener) {
    init {
        if (selections.size > 9)
            selections = selections.subList(9, selections.size) // remove all elements after the 9th.
    }

    fun activate() {
        setHotbar()
        rightClickListener.activePlayers[player.uniqueId] = ::handleSelect;
    }

    fun deactivate() {
        rightClickListener.activePlayers.remove(player.uniqueId)
    }

    fun handleSelect(index: Int, block: Block?) {
        selections[index].action(player, block)
    }

    private fun setHotbar() {
        player.inventory.clear()
        selections.forEachIndexed { index, selection ->
            player.inventory.setItem(index, selection.itemStack)
        }
    }
}