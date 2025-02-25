package com.slopey.bedwars.persistence

import org.bukkit.Server
import java.io.File

class MapPersistence(private val server: Server) {
    fun saveMap(map: BedwarsMap) {
        val sourceFolder = File(server.worldContainer, map.world.name)
        val templateWorldName = "template_${map.world.name}"
    }

}