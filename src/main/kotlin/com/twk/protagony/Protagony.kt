package com.twk.protagony

import com.twk.protagony.network.ClassSelectionNetworking
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object Protagony : ModInitializer {
    const val MOD_ID = "protagony"
    private val logger = LoggerFactory.getLogger(MOD_ID)

    override fun onInitialize() {
        logger.info("Initializing Protagony mod")

        // Register class selection networking
        ClassSelectionNetworking.register()

        logger.info("Protagony mod initialized successfully!")
    }
}