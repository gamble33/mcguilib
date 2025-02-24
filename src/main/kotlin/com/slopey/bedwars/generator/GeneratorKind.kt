package com.slopey.bedwars.generator

import org.bukkit.Material

enum class GeneratorKind {
    IRON, GOLD, DIAMOND, EMERALD;

    fun getBlockType(): Material {
        return when (this) {
            IRON -> Material.IRON_BLOCK
            GOLD -> Material.GOLD_BLOCK
            DIAMOND -> Material.DIAMOND_BLOCK
            EMERALD -> Material.EMERALD_BLOCK
        }
    }
}