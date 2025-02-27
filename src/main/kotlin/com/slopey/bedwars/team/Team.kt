package com.slopey.bedwars.team

import com.slopey.bedwars.persistence.LocationSerializer
import kotlinx.serialization.Serializable
import org.bukkit.Location

@Serializable
class Team(
    val teamColor: TeamColor,
    @Serializable(with = LocationSerializer::class) private val spawnPoint: Location,
    @Serializable(with = LocationSerializer::class) val bedLocation: Location
) {
    @Transient val name: String = teamColor.name
    @Transient val players: MutableList<String> = mutableListOf()
}