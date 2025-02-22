package com.slopey.bedwars

import org.bukkit.Location

class Team(val spawnPoint: Location, val isBedAlive: Boolean) {
    private val players: MutableList<String> = mutableListOf()
}