package com.twk.protagony.client

import com.twk.protagony.client.screen.ClassSelectionScreen
import com.twk.protagony.network.OpenClassSelectionPayload
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

object ClassSelectionClientHandler {

    fun register() {
        // Client: Receive packet from server to open screen
        ClientPlayNetworking.registerGlobalReceiver(OpenClassSelectionPayload.ID) { payload, context ->
            context.client().execute {
                context.client().setScreen(ClassSelectionScreen())
            }
        }
    }
}