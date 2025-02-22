package com.slopey.bedwars.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import java.util.*
import kotlin.collections.HashMap

class RightClickListener : Listener {
    val activePlayers: HashMap<UUID, (Int) -> Unit> = hashMapOf();

    @EventHandler
    fun onPlayerRightClick(event: PlayerInteractEvent) {
        if (
            !activePlayers.contains(event.player.uniqueId) ||
            event.action != Action.RIGHT_CLICK_AIR &&
            event.action != Action.RIGHT_CLICK_BLOCK
        ) return;

        val player = event.player;
        val currentSlot = player.inventory.heldItemSlot;
        activePlayers[player.uniqueId]?.invoke(currentSlot);
    }
}