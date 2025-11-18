package com.twk.protagony.component

import net.minecraft.util.Identifier
import org.ladysnake.cca.api.v3.component.ComponentKey
import org.ladysnake.cca.api.v3.component.ComponentRegistry
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy

object ModComponents : EntityComponentInitializer {

    lateinit var PLAYER_DATA: ComponentKey<PlayerDataComponent>
        private set

    override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
        // Create the key INSIDE the registration method
        PLAYER_DATA = ComponentRegistry.getOrCreate(
            Identifier.of("protagony", "player_data"),
            PlayerDataComponent::class.java
        )

        // Then register it
        registry.registerForPlayers(
            PLAYER_DATA,
            { player -> PlayerDataComponentImpl(player) },
            RespawnCopyStrategy.ALWAYS_COPY
        )
    }
}