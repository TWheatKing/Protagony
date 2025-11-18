package com.twk.protagony.system.xp.listener

import com.twk.protagony.system.xp.XpCalculator
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.minecraft.server.network.ServerPlayerEntity

object CombatXpListener {

    fun register() {
        ServerLivingEntityEvents.AFTER_DEATH.register { entity, damageSource ->
            val attacker = damageSource.attacker
            if (attacker is ServerPlayerEntity) {
                val baseXp = XpCalculator.calculateCombatXp(entity)
                XpCalculator.awardXp(
                    attacker,
                    baseXp,
                    XpCalculator.XpActivity.COMBAT,
                    "Killing ${entity.type.name.string}"
                )
            }
        }
    }
}
