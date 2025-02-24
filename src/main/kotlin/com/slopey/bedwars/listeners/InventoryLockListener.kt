package com.slopey.bedwars.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.player.PlayerDropItemEvent
import java.util.UUID

class InventoryLockListener : Listener {
    val activePlayers: HashSet<UUID> = hashSetOf()

    @EventHandler
    fun onInventoryClicked(event: InventoryClickEvent) {
        if (activePlayers.contains(event.whoClicked.uniqueId)) event.isCancelled = true;
    }

    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent) {
        if (activePlayers.contains(event.whoClicked.uniqueId)) event.isCancelled = true;
    }

    @EventHandler
    fun onPlayerDropItem(event: PlayerDropItemEvent) {
        if (activePlayers.contains(event.player.uniqueId)) event.isCancelled = true;
    }

    @EventHandler
    fun onEntityPickupItem(event: EntityPickupItemEvent) {
        if (activePlayers.contains(event.entity.uniqueId)) event.isCancelled = true;
    }

    @EventHandler
    fun onPlaceBlocK(event: BlockPlaceEvent) {
        if (activePlayers.contains(event.player.uniqueId)) event.isCancelled = true;
    }
}