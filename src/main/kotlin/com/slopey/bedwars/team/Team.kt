package com.slopey.bedwars.team

import org.bukkit.Location

class Team(val teamColor: TeamColor, private val spawnPoint: Location, val bedLocation: Location) {
    val name: String = teamColor.name
    val players: MutableList<String> = mutableListOf()
}