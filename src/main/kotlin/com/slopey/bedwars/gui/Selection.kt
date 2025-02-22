package com.slopey.bedwars.gui

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

data class Selection(val itemStack: ItemStack, val action: (Player) -> Unit)