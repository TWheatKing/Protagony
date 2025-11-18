package com.twk.protagony.system.xp

import com.twk.protagony.component.protagonyData
import com.twk.protagony.system.playerclass.PlayerClass
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.ItemTags
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object XpCalculator {

    // ========== MINING XP ==========
    fun calculateMiningXp(block: Block): Long {
        return when {
            // Valuable Ores
            block == Blocks.DIAMOND_ORE || block == Blocks.DEEPSLATE_DIAMOND_ORE -> 50L
            block == Blocks.EMERALD_ORE || block == Blocks.DEEPSLATE_EMERALD_ORE -> 50L

            // Nether Valuable Ores
            block == Blocks.NETHER_QUARTZ_ORE -> 40L
            block == Blocks.ANCIENT_DEBRIS -> 100L

            // Rare Ores
            block == Blocks.IRON_ORE || block == Blocks.DEEPSLATE_IRON_ORE -> 30L
            block == Blocks.GOLD_ORE || block == Blocks.DEEPSLATE_GOLD_ORE -> 30L
            block == Blocks.NETHER_GOLD_ORE -> 40L
            block == Blocks.REDSTONE_ORE || block == Blocks.DEEPSLATE_REDSTONE_ORE -> 30L
            block == Blocks.LAPIS_ORE || block == Blocks.DEEPSLATE_LAPIS_ORE -> 30L

            // Common Ores
            block == Blocks.COAL_ORE || block == Blocks.DEEPSLATE_COAL_ORE -> 20L
            block == Blocks.COPPER_ORE || block == Blocks.DEEPSLATE_COPPER_ORE -> 20L

            // Logs
            block.defaultState.isIn(BlockTags.LOGS) -> 10L

            // Default (stone, dirt, etc)
            else -> 10L
        }
    }

    // ========== COMBAT XP ==========
    fun calculateCombatXp(entity: LivingEntity): Long {
        val maxHealth = entity.maxHealth
        return (maxHealth / 10.0 * 10.0).toLong().coerceAtLeast(10L)
    }

    // ========== FISHING XP ==========
    fun calculateFishingXp(): Long = 10L

    // ========== CRAFTING XP ==========
    fun calculateCraftingXp(item: Item, stackSize: Int): Long {
        val baseXp = when {
            // Complex items
            item == net.minecraft.item.Items.ENCHANTING_TABLE -> 100L
            item == net.minecraft.item.Items.ANVIL -> 100L
            item == net.minecraft.item.Items.BREWING_STAND -> 100L
            item == net.minecraft.item.Items.BEACON -> 200L

            // Armor pieces
            item.toString().contains("helmet") ||
                    item.toString().contains("chestplate") ||
                    item.toString().contains("leggings") ||
                    item.toString().contains("boots") -> 50L

            // Tools
            item.toString().contains("pickaxe") ||
                    item.toString().contains("axe") ||
                    item.toString().contains("shovel") ||
                    item.toString().contains("hoe") ||
                    item.toString().contains("sword") -> 20L

            // Simple items (multiply by stack size)
            else -> 1L * stackSize
        }

        return baseXp
    }

    // ========== ENCHANTING XP ==========
    fun calculateEnchantingXp(levels: Int): Long {
        return (levels * 10L).coerceAtLeast(10L)
    }

    // ========== LOOTING XP ==========
    fun calculateLootingXp(source: LootSource): Long {
        return when (source) {
            LootSource.CHEST -> 50L
            LootSource.POT -> 10L
            LootSource.VILLAGER_TRADE -> 5L
        }
    }

    enum class LootSource {
        CHEST, POT, VILLAGER_TRADE
    }

    // ========== MULTIPLIER APPLICATION ==========
    fun applyClassMultiplier(baseXp: Long, player: ServerPlayerEntity, activity: XpActivity): Long {
        val data = player.protagonyData

        // No class selected = no XP
        if (!data.hasSelectedClass) return 0L

        val playerClass = PlayerClass.fromId(data.selectedClass ?: return 0L) ?: return 0L

        val multiplier = when (activity) {
            XpActivity.MINING -> playerClass.xpMultipliers.mining
            XpActivity.COMBAT -> playerClass.xpMultipliers.combat
            XpActivity.FISHING -> playerClass.xpMultipliers.fishing
            XpActivity.LOOTING -> playerClass.xpMultipliers.looting
            XpActivity.CRAFTING -> playerClass.xpMultipliers.crafting
            XpActivity.ENCHANTING -> playerClass.xpMultipliers.enchanting
        }

        return (baseXp * multiplier).toLong()
    }

    enum class XpActivity {
        MINING, COMBAT, FISHING, LOOTING, CRAFTING, ENCHANTING
    }

    // ========== AWARD XP HELPER ==========
    fun awardXp(player: ServerPlayerEntity, baseXp: Long, activity: XpActivity, source: String) {
        val finalXp = applyClassMultiplier(baseXp, player, activity)

        if (finalXp <= 0) return

        val leveledUp = player.protagonyData.addXp(finalXp)

        // Optional: Send feedback to player
        if (leveledUp) {
            player.sendMessage(
                Text.literal("§6§lLEVEL UP! §r§eYou are now level ${player.protagonyData.currentLevel}"),
                true // Action bar
            )
        }
    }
}