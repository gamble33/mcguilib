package com.slopey.bedwars.setup

import com.slopey.bedwars.generator.Generator
import com.slopey.bedwars.generator.GeneratorKind
import com.slopey.bedwars.gui.HotbarMenu
import com.slopey.bedwars.gui.MenuStack
import com.slopey.bedwars.gui.Selection
import com.slopey.bedwars.gui.UtilsGui
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class GeneratorSetupWizard(player: Player, menuStack: MenuStack, private val onCreateGenerator: (Generator) -> Boolean) {
    init {
        val ironItem = UtilsGui.item(
            Material.IRON_INGOT,
            "Set Iron Generator",
            "Right click on an iron block to set it to an iron generator"
        )

        val goldItem = UtilsGui.item(
            Material.GOLD_INGOT,
            "Set Gold Generator",
            "Right click on a gold block to set it to an gold generator"
        )

        val diamondItem = UtilsGui.item(
            Material.DIAMOND,
            "Set Diamond Generator",
            "Right click on a diamond block to set it to an diamond generator"
        )

        val emeraldItem = UtilsGui.item(
            Material.EMERALD,
            "Set Emerald Generator",
            "Right click on an emerald block to set it to an emerald generator"
        )

        val quitItem = UtilsGui.item(
            Material.BARRIER,
            "Go Back"
        )

        val hotbarMenu = HotbarMenu(player, listOf(
            generatorSelection(ironItem, GeneratorKind.IRON),
            generatorSelection(goldItem, GeneratorKind.GOLD),
            generatorSelection(diamondItem, GeneratorKind.DIAMOND),
            generatorSelection(emeraldItem, GeneratorKind.EMERALD),
            Selection(quitItem) { _, _ -> menuStack.pop() }
        ), menuStack.rightClickListener)

        menuStack.push(hotbarMenu)
    }

    private fun generatorSelection(item: ItemStack, generatorKind: GeneratorKind): Selection {
        return Selection(item) { player, block ->
            if (block == null || block.type != generatorKind.getBlockType()) {
                player.sendMessage("Click on an ${generatorKind.name} block to set it as a generator.")
            } else {
                if (onCreateGenerator(Generator(generatorKind, block)))
                    player.sendMessage("Created ${generatorKind.name} generator!")
                else
                    player.sendMessage("Generator already exists here.")
            }
        }
    }
}