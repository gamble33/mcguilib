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
import org.bukkit.Tag

class TeamSetupWizard(
    private val player: Player,
    menuStack: MenuStack,
    private val teams: List<Team>,
    private val onCreateTeam: (Team) -> Unit
) {
    private var spawnPoint: Location? = null
    private var bedLocation: Location? = null
    private var teamColor: TeamColor? = null

    init {
        // Spawn point item
        val spawnPointItem = ItemStack(Material.CAMEL_SPAWN_EGG)
        var meta = spawnPointItem.itemMeta!!
        meta.lore(
            listOf(
                Component.text("This will set the team's spawn point where you are standing")
            )
        )
        meta.displayName(Component.text("Set Spawn Point"))
        spawnPointItem.itemMeta = meta;

        val selectColorItem = ItemStack(Material.PINK_WOOL)
        meta = selectColorItem.itemMeta!!
        meta.lore(
            listOf(
                Component.text("Select to choose the color of your team (blocks, bed, etc.)")
            )
        )
        meta.displayName(Component.text("Select Team Color"))
        selectColorItem.itemMeta = meta;

        val setBedItem = UtilsGui.item(
            Material.WHITE_BED,
            "Set Bed Location",
            "Right click on a bed to set it as the team's bed location"
        )

        val confirmItem = UtilsGui.item(
            Material.GREEN_CONCRETE,
            "Confirm & Save Team",
            "This will confirm and save your team."
        )

        val quitItem = ItemStack(Material.BARRIER);
        meta = quitItem.itemMeta!!
        meta.lore(
            listOf(
                Component.text("This will quit the team setup wizard")
            )
        )
        meta.displayName(Component.text("Back"))
        quitItem.itemMeta = meta;

        val hotbarMenu = HotbarMenu(player, listOf(
            Selection(spawnPointItem, { p, _ ->
                spawnPoint = p.location
                p.sendMessage("Set spawn point!")
            }
            ),
            Selection(selectColorItem, { p, _ ->
                WoolSelectGUI(p, menuStack, teams, ::onTeamColorSelected)
            }),
            Selection(setBedItem) { p, block ->
                if (block == null || !Tag.BEDS.isTagged(block.type)) {
                    p.sendMessage("Right click on a bed to set it as the team's bed location.")
                    return@Selection
                }
                bedLocation = block.location
                p.sendMessage("Bed location set at ${bedLocation.toString()}!")
            },
            Selection(confirmItem, { p, _ ->
                if (isValid()) {
                    createTeam()
                    menuStack.pop()
                } else {
                    p.sendMessage("Not everything was set!")
                    p.sendMessage("Unable to create team.")
                }
            }),
            Selection(quitItem, { p, _ -> p.sendMessage("Exiting team creation"); menuStack.pop() })
        ), menuStack.rightClickListener
        )

        menuStack.push(hotbarMenu)
    }

    fun onTeamColorSelected(color: TeamColor) {
        player.sendMessage("Set team color!")
        teamColor = color
    }

    private fun createTeam() {
        val team = Team(teamColor!!, spawnPoint!!, bedLocation!!)
        bedLocation?.block?.type = Material.AIR
        player.sendMessage("Created Team: ${team}")
        onCreateTeam(team)
    }

    private fun isValid(): Boolean {
        return spawnPoint != null &&
                teamColor != null &&
                bedLocation != null
    }
}