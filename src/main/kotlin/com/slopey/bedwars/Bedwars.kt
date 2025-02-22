package com.slopey.bedwars

import com.slopey.bedwars.commands.CreateMapCommand
import com.slopey.bedwars.listeners.InventoryLockListener
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class Bedwars : JavaPlugin(), Listener {

    val inventoryLocker = InventoryLockListener();
    private var generator: Location? = null;

    override fun onEnable() {
        logger.info("Bedwars enabled!")

        getCommand("createmap")?.setExecutor(CreateMapCommand(this))
            ?: logger.warning("Failed to register /createmap command")

        server.pluginManager.registerEvents(this, this)
        server.pluginManager.registerEvents(inventoryLocker, this)

        startRepeatingTask()
    }

    override fun onDisable() {
        logger.info("Bedwars disabled!")
    }

    private fun startRepeatingTask() {
        val emerald = ItemStack(Material.EMERALD);
        object : BukkitRunnable() {
            override fun run() {
                if (generator != null)
                    generator!!.world.dropItem(generator!!, emerald)
            }
        }.runTaskTimer(this, 20L, 30L) // 20 ticks = 1 second
    }

    @EventHandler
    fun onPlayerRightClick(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return;

        val clickedBlock = event.clickedBlock ?: return;

        if (clickedBlock.type != Material.EMERALD_BLOCK) return;
        generator = clickedBlock.location;
        generator!!.y += 1;
        event.player.sendMessage("Generator set at ${generator!!.blockX}, ${generator!!.blockY}, ${generator!!.blockZ}!")
    }
}
