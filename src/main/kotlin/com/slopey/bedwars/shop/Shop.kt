package com.slopey.bedwars.shop

import com.slopey.bedwars.persistence.LocationSerializer
import kotlinx.serialization.Serializable
import org.bukkit.Location

@Serializable
class Shop(
    @Serializable(with = LocationSerializer::class) val location: Location
)