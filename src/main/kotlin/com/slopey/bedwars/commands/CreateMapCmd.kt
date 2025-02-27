package com.slopey.bedwars.commands

import com.slopey.bedwars.Bedwars
import com.slopey.bedwars.generator.Generator
import com.slopey.bedwars.gui.*
import com.slopey.bedwars.setup.IslandSetupWizard
import com.slopey.bedwars.setup.TeamSetupWizard
import com.slopey.bedwars.persistence.BedwarsMap
import com.slopey.bedwars.shop.Shop
import com.slopey.bedwars.team.Team
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class CreateMapCmd(private val plugin: Bedwars) : CommandExecutor {
    private val teams: MutableList<Team> = mutableListOf()
    private val generators: MutableList<Generator> = mutableListOf()
    private val shops: MutableList<Shop> = mutableListOf()
    private var mapName: String? = null

    private fun setHotbar(player: Player) {
        val teamItem = ItemStack(Material.WHITE_WOOL);
        var meta = teamItem.itemMeta!!
        meta.lore(
            listOf(
                Component.text("This will start the team creation process")
            )
        )
        meta.displayName(Component.text("Create Team"))
        teamItem.itemMeta = meta;

        val islandItem = UtilsGui.item(
            Material.SAND,
            "Island Setup",
            "Setup generators, shops, etc."
        )

        val setNameItem = UtilsGui.item(
            Material.NAME_TAG,
            "Set Name",
            "Right click and type the name of this map into chat"
        )

        val createMapItem = UtilsGui.item(
            Material.GREEN_CONCRETE,
            "Confirm & Save Map",
            "Right click to create the map. This will exit the map creation process.",
            "This may cause some lag as the server creates a copy of this world."
        )

        val quitItem = ItemStack(Material.BARRIER);
        meta = quitItem.itemMeta!!
        meta.lore(
            listOf(
                Component.text("This will quit the map creation process")
            )
        )
        meta.displayName(Component.text("Quit"))
        quitItem.itemMeta = meta;

        val menuStack = MenuStack(player, plugin)

        val hotbarMenu = HotbarMenu(player, listOf(
            Selection(teamItem) { p, _ ->
                p.sendMessage("Starting map creation process!");
                TeamSetupWizard(p, menuStack, teams, ::onCreateTeam)
            },
            Selection(islandItem) { p, _ ->
                IslandSetupWizard(p, menuStack, ::onCreateGenerator, ::onCreateShop)
            },
            Selection(setNameItem) { p, _ ->
                menuStack.push(TextInput(player, "Enter the name of the map in chat.", menuStack.chatListener) { response ->
                    mapName = response.trim()
                    p.sendMessage("Map name set to ${mapName!!}")
                    p.sendMessage("Map world saved as template_${mapName!!.lowercase()}")
                    menuStack.pop()
                })
            },
            Selection(createMapItem) { p, _ ->
                p.sendMessage("Map ${mapName ?: "NULL"} validity: ${isMapValid()}")
                p.sendMessage("  Teams: ${teams.size}")
                p.sendMessage("  Generators: ${generators.size}")
                p.sendMessage("  Shops: ${shops.size}")
                if (!isMapValid()) {
                    diagnoseMapValidity(player)
                    p.sendMessage("Map is not valid")
                    return@Selection
                }
                val map = BedwarsMap(teams, generators, shops, mapName!!)
                menuStack.pop()
                player.inventory.clear()
                createMap(map)
                player.sendMessage("Map created successfully!")
            },
            Selection(quitItem) { p, _ ->
                p.sendMessage("Quit map creation process!");
                menuStack.pop();
                player.inventory.clear()
            }
        ), menuStack.rightClickListener)

        menuStack.push(hotbarMenu)
    }

    private fun createMap(map: BedwarsMap) {
        plugin.mapPersistence.saveMap(map)
    }

    private fun diagnoseMapValidity(player: Player) {
        if (teams.size <= 1) player.sendMessage("Create at least 2 teams.")
        if (generators.size < 1) player.sendMessage("Create at least 1 generator.")
        if (shops.size < 1) player.sendMessage("Create at least 1 shop.")
        if (mapName == null) player.sendMessage("Set a map name.")
    }

    private fun isMapValid() = teams.size > 1 && generators.size > 0 && shops.size > 0 && mapName != null

    private fun onCreateTeam(team: Team) {
        teams.add(team)
    }

    private fun onCreateGenerator(generator: Generator): Boolean {
        if (generators.any { it.location == generator.location}) return false
        generators.add(generator)
        return true
    }

    private fun onCreateShop(shop: Shop): Boolean {
        if (shops.any { it.location.block == shop.location.block }) return false
        shops.add(shop)
        return true
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return true;

        sender.sendMessage("Creating New Bedwars Map!")
        setHotbar(sender);
        return true;
    }
}