package com.slopey.bedwars.persistence

import com.slopey.bedwars.generator.Generator
import com.slopey.bedwars.shop.Shop
import com.slopey.bedwars.team.Team
import org.bukkit.World

data class BedwarsMap (
    val teams: List<Team>,
    val generators: List<Generator>,
    val shops: List<Shop>,
    val world: World
)