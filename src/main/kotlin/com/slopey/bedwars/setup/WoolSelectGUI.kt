package com.slopey.bedwars.setup

import com.slopey.bedwars.gui.HotbarMenu
import com.slopey.bedwars.gui.MenuStack
import com.slopey.bedwars.gui.Selection
import com.slopey.bedwars.team.Team
import com.slopey.bedwars.team.TeamColor
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class WoolSelectGUI(player: Player, private val menuStack: MenuStack, teams: List<Team>, private val onSelect: (TeamColor) -> Unit) {
    init {
        val quitItem = ItemStack(Material.BARRIER);
        val meta = quitItem.itemMeta!!
        meta.displayName(Component.text("Go Back"))
        quitItem.itemMeta = meta;

        val woolSelections = listOf(
            createWoolSelection(Material.RED_WOOL, "Red Team", TeamColor.RED),
            createWoolSelection(Material.ORANGE_WOOL, "Orange Team", TeamColor.ORANGE),
            createWoolSelection(Material.YELLOW_WOOL, "Yellow Team", TeamColor.YELLOW),
            createWoolSelection(Material.GREEN_WOOL, "Green Team", TeamColor.GREEN),
            createWoolSelection(Material.BLUE_WOOL, "Blue Team", TeamColor.BLUE),
            createWoolSelection(Material.PURPLE_WOOL, "Purple Team", TeamColor.PURPLE),
            createWoolSelection(Material.PINK_WOOL, "Pink Team", TeamColor.PINK),
            createWoolSelection(Material.BROWN_WOOL, "Brown Team", TeamColor.BROWN),
        ).filter { s ->
            teams.none { s.itemStack.type.name.lowercase().contains(it.teamColor.name.lowercase()) }
        }

        val menu = HotbarMenu(player, listOf(
            *woolSelections.toTypedArray(),
            Selection(quitItem) { p,_ -> p.sendMessage("No color selected."); menuStack.pop() },
        ), menuStack.rightClickListener)
        menuStack.push(menu)
    }

    private fun createWoolSelection(material: Material, name: String, color: TeamColor): Selection {
        val wool = ItemStack(material)
        val meta = wool.itemMeta!!
        meta.displayName(Component.text(name))
        wool.itemMeta = meta
        return Selection(wool) { p, _ ->
            p.sendMessage("Selected {$color.name} color!")
            menuStack.pop()
            onSelect(color)
        }
    }
}