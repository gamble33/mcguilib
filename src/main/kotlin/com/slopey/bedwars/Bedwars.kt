package com.slopey.bedwars

import com.slopey.bedwars.commands.*
import com.slopey.bedwars.listeners.InventoryLockListener
import com.slopey.bedwars.persistence.MapPersistence
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
    val mapPersistence: MapPersistence = MapPersistence(this)
    private var generator: Location? = null;

    override fun onEnable() {
        logger.info("Bedwars enabled!")

        getCommand("createmap")?.setExecutor(CreateMapCmd(this))
            ?: logger.warning("Failed to register /createmap command")
        getCommand("loadmap")?.setExecutor(LoadMapCmd(mapPersistence))
            ?: logger.warning("Failed to register /loadmap command")
        getCommand("loadworld")?.setExecutor(LoadWorldCmd(mapPersistence))
            ?: logger.warning("Failed to register /loadworld command")
        getCommand("listworlds")?.setExecutor(ListWorldsCmd())
            ?: logger.warning("Failed to register /listworlds command")
        getCommand("startgame")?.setExecutor(StartGameCmd())
            ?: logger.warning("Failed to register /startgame command")

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
