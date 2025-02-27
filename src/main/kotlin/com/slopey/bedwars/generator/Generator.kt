package com.slopey.bedwars.generator

import com.slopey.bedwars.persistence.LocationSerializer
import kotlinx.serialization.Serializable
import org.bukkit.Location
import org.bukkit.block.Block

@Serializable
class Generator(
    val kind: GeneratorKind,
    @Serializable(with = LocationSerializer::class) val location: Location
) {
}