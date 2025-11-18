package com.twk.protagony

import com.twk.protagony.network.ClassSelectionNetworking
import com.twk.protagony.system.xp.XpSystem
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object Protagony : ModInitializer {
    const val MOD_ID = "protagony"
    private val logger = LoggerFactory.getLogger(MOD_ID)

    override fun onInitialize() {
        logger.info("Initializing Protagony mod")

        // Register class selection networking
        ClassSelectionNetworking.register()
        logger.info("Registered class selection networking")

        // Register XP system
        XpSystem.register()
        logger.info("Registered XP system")


        logger.info("Protagony mod initialized successfully!")
    }
}