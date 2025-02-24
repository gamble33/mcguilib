package com.slopey.bedwars.commands

import com.slopey.bedwars.Bedwars
import com.slopey.bedwars.generator.Generator
import com.slopey.bedwars.gui.HotbarMenu
import com.slopey.bedwars.gui.MenuStack
import com.slopey.bedwars.gui.Selection
import com.slopey.bedwars.gui.UtilsGui
import com.slopey.bedwars.setup.IslandSetupWizard
import com.slopey.bedwars.setup.TeamSetupWizard
import com.slopey.bedwars.shop.Shop
import com.slopey.bedwars.team.Team
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class CreateMapCommand(private val plugin: Bedwars) : CommandExecutor {
    private val teams: MutableList<Team> = mutableListOf()
    private val generators: MutableList<Generator> = mutableListOf()
    private val shops: MutableList<Shop> = mutableListOf()

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
                IslandSetupWizard(p, menuStack, ::onCreateGenerator)
            },
            Selection(quitItem) { p, _ ->
                p.sendMessage("Quit map creation process!");
                menuStack.pop();
                player.inventory.clear()
            }
        ), menuStack.rightClickListener)

        menuStack.push(hotbarMenu)
    }

    private fun onCreateTeam(team: Team) {
        teams.add(team)
    }

    private fun onCreateGenerator(generator: Generator): Boolean {
        if (generators.any { it.block.location == generator.block.location}) return false
        generators.add(generator)
        return true
    }

    private fun onCreateShop(shop: Shop) {
        shops.add(shop)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return true;

        sender.sendMessage("Creating New Bedwars Map!")
        setHotbar(sender);
        return true;
    }
}