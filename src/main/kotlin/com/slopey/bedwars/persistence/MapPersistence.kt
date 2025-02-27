package com.slopey.bedwars.persistence

import com.slopey.bedwars.Bedwars
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.WorldCreator
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class MapPersistence(private val plugin: Bedwars) {
    companion object {
        const val MAP_FOLDER = "maps"
        val IGNORED_FILES = listOf("session.lock", "uid.dat")
        private val JSON = Json { prettyPrint = true }
    }

    private val server = plugin.server

    fun saveMap(map: BedwarsMap) {
        saveWorld(map)
        saveSettings(map)
    }

    private fun saveSettings(map: BedwarsMap) {
        try {
            if (!plugin.dataFolder.exists())
                plugin.dataFolder.mkdirs()

            val file = File(plugin.dataFolder, "$MAP_FOLDER/${map.name}.json")
            file.parentFile.mkdirs() // Ensure maps directory exists

            val json = JSON.encodeToString(map)
            file.writeText(json)
        } catch (ex: Exception) {
            plugin.logger.severe("Failed to save map ${map.name}: ${ex.message}")
            ex.printStackTrace()
        }
    }

    /**
     * Loads the settings for a Bedwars map from a JSON file.
     * Settings include generators, shops, team colors, etc.
     *
     * @param mapName The name of the map
     * @return The loaded `BedwarsMap` object if successful, or `null` if the map file does not exist
     *         or an error occurs during loading.
     */
    private fun loadSettings(mapName: String): BedwarsMap? {
        val file = File(plugin.dataFolder, "$MAP_FOLDER/$mapName")
        if (!file.exists()) {
            plugin.logger.warning("Map file for $mapName does not exist!")
            return null
        }

        return try {
            JSON.decodeFromString<BedwarsMap>(file.readText())
        } catch (ex: Exception) {
            plugin.logger.severe("Error loading map $mapName: ${ex.message}")
            null
        }
    }

    private fun createWorld(mapName: String): World? {
        val world = Bukkit.createWorld(WorldCreator(mapName.lowercase()))
        if (world != null)
            plugin.logger.info("Created ${world.name} successfully!")
        else
            plugin.logger.info("Failed to create $mapName.")
        return world
    }

    private fun saveWorld(map: BedwarsMap) {
        val templateWorldName = "template_${map.name.lowercase()}"

        val currentWorld = File(server.worldContainer, map.world.name)
        val templateWorld = File(server.worldContainer, templateWorldName)


        copyWorld(currentWorld, templateWorld) // Copy the actual file structure.
        plugin.logger.info("$templateWorldName's file structure was copied successfully.")
        val world = createWorld(templateWorldName) // Load the world.
        world?.save()

        // Send all players to the world.
        val target = world?.spawnLocation ?: return
        Bukkit.getOnlinePlayers().forEach { player -> player.teleport(target)}
    }

    /**
     * Recursively copies a file or directory from a source location to a target location.
     *
     * If the [src] is a directory, this function will create the corresponding [dst] directory (if it doesn't exist)
     * and then copy each child file and directory recursively into the dst.
     * If the [src] is a file, it will be copied to the [dst] location, replacing any existing file.
     *
     * @param src The source file or directory to copy.
     * @param dst The target file or directory where the source should be copied.
     */
    private fun copyWorld(src: File, dst: File) {
        if (src.isDirectory) {
            if (!dst.exists()) dst.mkdirs()
            src.list()?.forEach { child ->
                copyWorld(File(src, child), File(dst, child))
            }
        } else if (!IGNORED_FILES.contains(src.name)){
            Files.copy(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING)
        }
    }

}