package com.twk.protagony.system.xp

import com.twk.protagony.system.xp.listener.*

object XpSystem {

    fun register() {
        // Register all XP listeners
        MiningXpListener.register()
        CombatXpListener.register()
        FishingXpListener.register()
        CraftingXpListener.register()
        EnchantingXpListener.register()
        LootingXpListener.register()
    }
}