package com.twk.protagony.system.xp.listener

import com.twk.protagony.system.xp.XpCalculator
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity

object EnchantingXpListener {

    fun register() {
        // Note: Enchanting requires a mixin to EnchantmentScreenHandler
        // Placeholder implementation - needs mixin
        
        // TODO: Implement mixin to EnchantmentScreenHandler.onButtonClick()
    }

    // Helper method to be called from mixin
    fun onItemEnchanted(player: ServerPlayerEntity, levels: Int) {
        val baseXp = XpCalculator.calculateEnchantingXp(levels)
        XpCalculator.awardXp(
            player,
            baseXp,
            XpCalculator.XpActivity.ENCHANTING,
            "Enchanting (Level $levels)"
        )
    }
}
