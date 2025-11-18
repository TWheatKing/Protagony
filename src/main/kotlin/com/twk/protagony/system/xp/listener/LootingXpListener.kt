package com.twk.protagony.system.xp.listener

import com.twk.protagony.system.xp.XpCalculator
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.block.Blocks
import net.minecraft.block.entity.ChestBlockEntity
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.ActionResult

object LootingXpListener {

    private val openedChests = mutableSetOf<Pair<Long, String>>()
    private val brokenPots = mutableSetOf<Pair<Long, String>>()

    fun register() {
        // Chest opening detection
        UseBlockCallback.EVENT.register { player, world, hand, hitResult ->
            if (!world.isClient && player is ServerPlayerEntity) {
                val pos = hitResult.blockPos
                val blockEntity = world.getBlockEntity(pos)

                if (blockEntity is LootableContainerBlockEntity) {
                    val lootTable = blockEntity.lootTable
                    if (lootTable != null) {
                        val key = player.uuid.leastSignificantBits to pos.toShortString()
                        if (!openedChests.contains(key)) {
                            openedChests.add(key)
                            XpCalculator.awardXp(
                                player,
                                XpCalculator.calculateLootingXp(XpCalculator.LootSource.CHEST),
                                XpCalculator.XpActivity.LOOTING,
                                "Opening loot chest"
                            )
                        }
                    }
                }
            }
            ActionResult.PASS
        }

        // Pot breaking detection
        net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents.BEFORE.register { world, player, pos, state, blockEntity ->
            if (!world.isClient && player is ServerPlayerEntity) {
                if (state.block == Blocks.DECORATED_POT) {
                    val potEntity = blockEntity as? net.minecraft.block.entity.DecoratedPotBlockEntity
                    if (potEntity?.lootTable != null) {
                        val key = player.uuid.leastSignificantBits to pos.toShortString()
                        if (!brokenPots.contains(key)) {
                            brokenPots.add(key)
                            XpCalculator.awardXp(
                                player,
                                XpCalculator.calculateLootingXp(XpCalculator.LootSource.POT),
                                XpCalculator.XpActivity.LOOTING,
                                "Breaking loot pot"
                            )
                        }
                    }
                }
            }
            true
        }
    }

    fun onVillagerTrade(player: ServerPlayerEntity) {
        XpCalculator.awardXp(
            player,
            XpCalculator.calculateLootingXp(XpCalculator.LootSource.VILLAGER_TRADE),
            XpCalculator.XpActivity.LOOTING,
            "Villager trade"
        )
    }
}