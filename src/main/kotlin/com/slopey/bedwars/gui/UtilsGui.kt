package com.slopey.bedwars.gui

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object UtilsGui {
    fun item(material: Material, name: String, vararg lores: String) : ItemStack {
        val itemStack = ItemStack(material)
        val meta = itemStack.itemMeta
        meta.displayName(Component.text(name))
        meta.lore(lores.map { Component.text(it) })
        itemStack.itemMeta = meta
        return itemStack
    }
}