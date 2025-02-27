package com.slopey.bedwars.persistence

import com.slopey.bedwars.generator.Generator
import com.slopey.bedwars.shop.Shop
import com.slopey.bedwars.team.Team
import kotlinx.serialization.Serializable
import org.bukkit.World

@Serializable
data class BedwarsMap (
    val teams: List<Team>,
    val generators: List<Generator>,
    val shops: List<Shop>,
    val name: String
) {
    val world: World
        get() {
            return generators.first().location.world!!
        }
}