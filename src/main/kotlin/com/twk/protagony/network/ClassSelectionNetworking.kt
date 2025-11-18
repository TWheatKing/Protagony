package com.twk.protagony.network

import com.twk.protagony.component.protagonyData
import com.twk.protagony.system.playerclass.PlayerClass
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object ClassSelectionNetworking {

    fun register() {
        // Register packet types
        PayloadTypeRegistry.playS2C().register(OpenClassSelectionPayload.ID, OpenClassSelectionPayload.CODEC)
        PayloadTypeRegistry.playC2S().register(ClassSelectionPayload.ID, ClassSelectionPayload.CODEC)

        // Server: Check on player join
        ServerPlayConnectionEvents.JOIN.register { handler, sender, server ->
            val player = handler.player

            // Check if player has not selected a class
            if (!player.protagonyData.hasSelectedClass) {
                // Send packet to client to open screen
                ServerPlayNetworking.send(player, OpenClassSelectionPayload())
            }
        }

        // Server: Receive class selection from client
        ServerPlayNetworking.registerGlobalReceiver(ClassSelectionPayload.ID) { payload, context ->
            context.server().execute {
                val player = context.player()
                val data = player.protagonyData

                // Validate: Player hasn't already selected
                if (data.hasSelectedClass) {
                    player.sendMessage(
                        Text.literal("You have already selected a class!").formatted(Formatting.RED),
                        false
                    )
                    return@execute
                }

                // Validate: Class ID exists
                val selectedClass = PlayerClass.fromId(payload.classId)
                if (selectedClass == null) {
                    player.sendMessage(
                        Text.literal("Invalid class selection!").formatted(Formatting.RED),
                        false
                    )
                    return@execute
                }

                // Save selection
                data.hasSelectedClass = true
                data.selectedClass = selectedClass.id

                // Apply starting bonuses (placeholder - implement in separate system)
                applyStartingBonuses(player, selectedClass)

                // Confirm to player
                player.sendMessage(
                    Text.literal("You have become ").formatted(Formatting.GRAY)
                        .append(Text.literal(selectedClass.displayName).formatted(Formatting.GOLD, Formatting.BOLD)),
                    false
                )
            }
        }
    }

    private fun applyStartingBonuses(player: net.minecraft.server.network.ServerPlayerEntity, playerClass: PlayerClass) {
        // TODO: Implement starting bonus application
        // Examples:
        // - SCAVENGER: Give Fortune I fishing rod, modify hunger drain rate
        // - INITIATE: Modify enchanting cost, enchant level cap
        // - VANGUARD: Apply attribute modifiers for damage/armor

        // For now, just log
        println("[Protagony] Applied starting bonuses for ${playerClass.displayName} to ${player.name.string}")
    }
}