package com.slopey.bedwars.listeners

import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import java.util.*
import kotlin.collections.HashMap

class RightClickListener : Listener {
    val activePlayers: HashMap<UUID, (Int, Block?) -> Unit> = hashMapOf();

    @EventHandler
    fun onPlayerRightClick(event: PlayerInteractEvent) {
        if (
            !activePlayers.contains(event.player.uniqueId) ||
            event.hand != EquipmentSlot.HAND ||
            event.action != Action.RIGHT_CLICK_AIR &&
            event.action != Action.RIGHT_CLICK_BLOCK
        ) return;

        val block: Block? = when {
            event.action == Action.RIGHT_CLICK_BLOCK -> event.clickedBlock!!
            else -> null
        }

        val player = event.player;
        val currentSlot = player.inventory.heldItemSlot;
        activePlayers[player.uniqueId]?.invoke(currentSlot, block);
    }
}