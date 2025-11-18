package com.twk.protagony.system.xp.listener

import com.twk.protagony.system.xp.XpCalculator
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeEntry
import net.minecraft.screen.slot.SlotActionType
import net.minecraft.server.network.ServerPlayerEntity

object CraftingXpListener {

    fun register() {
        // Note: Crafting requires a mixin to ScreenHandler to detect crafting events
        // Placeholder implementation - needs mixin
        
        // TODO: Implement mixin to CraftingResultSlot.onTakeItem() or similar
    }

    // Helper method to be called from mixin
    fun onItemCrafted(player: ServerPlayerEntity, stack: ItemStack) {
        val baseXp = XpCalculator.calculateCraftingXp(stack.item, stack.count)
        XpCalculator.awardXp(
            player,
            baseXp,
            XpCalculator.XpActivity.CRAFTING,
            "Crafting ${stack.item}"
        )
    }
}
