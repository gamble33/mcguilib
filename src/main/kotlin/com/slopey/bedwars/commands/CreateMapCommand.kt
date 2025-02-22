package com.slopey.bedwars.commands

import com.slopey.bedwars.Bedwars
import com.slopey.bedwars.gui.HotbarMenu
import com.slopey.bedwars.gui.MenuStack
import com.slopey.bedwars.gui.Selection
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class CreateMapCommand(private val plugin: Bedwars) : CommandExecutor {

    private fun setHotbar(player: Player) {
        val teamItem = ItemStack(Material.WHITE_WOOL);
        var meta = teamItem.itemMeta!!
        meta.lore(listOf(
            Component.text("This will start the team creation process")
        ))
        meta.displayName(Component.text("Create Team"))
        teamItem.itemMeta = meta;

        val quitItem = ItemStack(Material.BARRIER);
        meta = quitItem.itemMeta!!
        meta.lore(listOf(
            Component.text("This will quit the map creation process")
        ))
        meta.displayName(Component.text("Quit"))
        quitItem.itemMeta = meta;

        val menuStack = MenuStack(player, plugin)

        val hotbarMenu = HotbarMenu(player, listOf(
            Selection(teamItem) { p -> p.sendMessage("Starting map creation process!") },
            Selection(quitItem) { p -> p.sendMessage("Quit map creation process!"); menuStack.pop(); player.inventory.clear() }
        ), menuStack.rightClickListener)

        menuStack.push(hotbarMenu)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return true;

        sender.sendMessage("Creating New Bedwars Map!")
        setHotbar(sender);
        return true;
    }
}