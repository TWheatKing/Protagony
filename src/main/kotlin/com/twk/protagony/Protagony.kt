package com.twk.protagony

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object Protagony : ModInitializer {
    const val MOD_ID = "protagony"
    private val logger = LoggerFactory.getLogger(MOD_ID)

    override fun onInitialize() {
        logger.info("Initializing Protagony mod")
        // Your initialization code here
    }
}