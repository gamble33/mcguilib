package com.slopey.bedwars.setup

import com.slopey.bedwars.gui.HotbarMenu
import com.slopey.bedwars.gui.MenuStack
import com.slopey.bedwars.gui.Selection
import com.slopey.bedwars.gui.UtilsGui
import com.slopey.bedwars.team.Team
import com.slopey.bedwars.team.TeamColor
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class TeamSetupWizard(private val player: Player, menuStack: MenuStack) {
    var spawnPoint: Location? = null
    var teamColor: TeamColor? = null

    init {
        // Spawn point item
        val spawnPointItem = ItemStack(Material.CAMEL_SPAWN_EGG)
        var meta = spawnPointItem.itemMeta!!
        meta.lore(listOf(
            Component.text("This will set the team's spawn point where you are standing")
        ))
        meta.displayName(Component.text("Set Spawn Point"))
        spawnPointItem.itemMeta = meta;

        val selectColorItem = ItemStack(Material.PINK_WOOL)
        meta = selectColorItem.itemMeta!!
        meta.lore(listOf(
            Component.text("Select to choose the color of your team (blocks, bed, etc.)")
        ))
        meta.displayName(Component.text("Select Team Color"))
        selectColorItem.itemMeta = meta;

        val confirmItem = UtilsGui.item(
            Material.GREEN_CONCRETE,
            "Confirm & Save Team",
            "This will confirm and save your team."
        )

        val quitItem = ItemStack(Material.BARRIER);
        meta = quitItem.itemMeta!!
        meta.lore(listOf(
            Component.text("This will quit the team setup wizard")
        ))
        meta.displayName(Component.text("Back"))
        quitItem.itemMeta = meta;

        val hotbarMenu = HotbarMenu(player, listOf(
            Selection(spawnPointItem, { p ->
                spawnPoint = p.location
                p.sendMessage("Set spawn point!")}
            ),
            Selection(selectColorItem, { p ->
                WoolSelectGUI(p, menuStack, ::onTeamColorSelected)
            }),
            Selection(confirmItem, {p ->
                if (isValid()) {
                    createTeam()
                    menuStack.pop()
                } else {
                    p.sendMessage("Not everything was set!")
                    p.sendMessage("Unable to create team.")
                }
            }),
            Selection(quitItem, { p -> p.sendMessage("Exiting team creation"); menuStack.pop()})
        ), menuStack.rightClickListener)

        menuStack.push(hotbarMenu)
    }

    fun onTeamColorSelected(color: TeamColor) {
        player.sendMessage("Set team color!")
        teamColor = color
    }

    private fun createTeam() {
        val team = Team(teamColor!!, spawnPoint!!)
        player.sendMessage("Created Team: ${team}")
        // todo
    }

    private fun isValid(): Boolean {
        return spawnPoint != null && teamColor != null
    }
}