package com.slopey.bedwars.setup

import com.slopey.bedwars.generator.Generator
import com.slopey.bedwars.gui.HotbarMenu
import com.slopey.bedwars.gui.MenuStack
import com.slopey.bedwars.gui.Selection
import com.slopey.bedwars.gui.UtilsGui
import org.bukkit.Material
import org.bukkit.entity.Player

class IslandSetupWizard(player: Player, menuStack: MenuStack, onCreateGenerator: (Generator) -> Boolean) {
    init {
        val generatorItem = UtilsGui.item(
            Material.EMERALD,
            "Setup Generators",
        )

        val shopItem = UtilsGui.item(
            Material.STICK,
            "Set Shop",
            "Right clicking on the ground will set the location of a shop (villager)"
        )

        val quitItem = UtilsGui.item(
            Material.BARRIER,
            "Go Back"
        )

        val hotbarMenu = HotbarMenu(player, listOf(
            Selection(generatorItem) { _,_ -> GeneratorSetupWizard(player, menuStack, onCreateGenerator) },
            Selection(shopItem) { _,block ->
                if (block == null) {
                    player.sendMessage("Right click on a block to set a shop location.")
                    return@Selection
                }
            },
            Selection(quitItem) { _,_ -> menuStack.pop() }

        ), menuStack.rightClickListener)

        menuStack.push(hotbarMenu)
    }
}