package com.twk.protagony.component

import org.ladysnake.cca.api.v3.component.Component
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper

interface PlayerDataComponent : Component {

    // Class Selection
    var hasSelectedClass: Boolean
    var selectedClass: String?

    // Progression
    var currentLevel: Int
    var currentXp: Long
    var totalXp: Long

    // Perks & Path
    val unlockedPerks: MutableSet<String>
    var selectedPath: String?

    // Utility
    fun addXp(amount: Long): Boolean
    fun addPerk(perkId: String)
    fun hasPerk(perkId: String): Boolean
    fun getXpForNextLevel(): Long
    fun resetProgression()

    // Serialization - Updated for CCA 7.x
    fun readFromNbt(tag: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup)
    fun writeToNbt(tag: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup)
}