package com.twk.protagony.network

import com.twk.protagony.Protagony
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier

// Server -> Client: Open class selection screen
data class OpenClassSelectionPayload(val dummy: Boolean = true) : CustomPayload {
    companion object {
        val ID: CustomPayload.Id<OpenClassSelectionPayload> =
            CustomPayload.Id(Identifier.of(Protagony.MOD_ID, "open_class_selection"))

        val CODEC: PacketCodec<RegistryByteBuf, OpenClassSelectionPayload> =
            PacketCodec.unit(OpenClassSelectionPayload())
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> = ID
}

// Client -> Server: Submit class selection
data class ClassSelectionPayload(val classId: String) : CustomPayload {
    companion object {
        val ID: CustomPayload.Id<ClassSelectionPayload> =
            CustomPayload.Id(Identifier.of(Protagony.MOD_ID, "class_selection"))

        val CODEC: PacketCodec<RegistryByteBuf, ClassSelectionPayload> =
            PacketCodecs.STRING.xmap(
                { ClassSelectionPayload(it) },
                { it.classId }
            ).cast()
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> = ID
}