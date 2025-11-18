package com.twk.protagony.system.xp.listener

import com.twk.protagony.system.xp.XpCalculator
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.minecraft.server.network.ServerPlayerEntity

object MiningXpListener {

    fun register() {
        PlayerBlockBreakEvents.AFTER.register { world, player, pos, state, blockEntity ->
            if (player is ServerPlayerEntity && !world.isClient) {
                val baseXp = XpCalculator.calculateMiningXp(state.block)
                XpCalculator.awardXp(
                    player,
                    baseXp,
                    XpCalculator.XpActivity.MINING,
                    "Mining ${state.block}"
                )
            }
        }
    }
}
