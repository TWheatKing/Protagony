package com.twk.protagony.component

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtString
import net.minecraft.registry.RegistryWrapper
import net.minecraft.storage.ReadView
import net.minecraft.storage.WriteView
import kotlin.math.pow
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent

class PlayerDataComponentImpl(private val player: PlayerEntity) : PlayerDataComponent, AutoSyncedComponent {

    override var hasSelectedClass: Boolean = false
    override var selectedClass: String? = null

    override var currentLevel: Int = 1
    override var currentXp: Long = 0L
    override var totalXp: Long = 0L

    override val unlockedPerks: MutableSet<String> = mutableSetOf()
    override var selectedPath: String? = null

    override fun addXp(amount: Long): Boolean {
        if (!hasSelectedClass) return false

        currentXp += amount
        totalXp += amount

        var leveledUp = false
        while (currentXp >= getXpForNextLevel()) {
            currentXp -= getXpForNextLevel()
            currentLevel++
            leveledUp = true
        }

        ModComponents.PLAYER_DATA.sync(player) // Add this
        return leveledUp
    }

    override fun addPerk(perkId: String) {
        unlockedPerks.add(perkId)
        ModComponents.PLAYER_DATA.sync(player) // Add this
    }

    override fun resetProgression() {
        hasSelectedClass = false
        selectedClass = null
        currentLevel = 1
        currentXp = 0L
        totalXp = 0L
        unlockedPerks.clear()
        selectedPath = null
        ModComponents.PLAYER_DATA.sync(player) // Add this
    }

    override fun readFromNbt(tag: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        hasSelectedClass = tag.getBoolean("HasSelectedClass").orElse(false)

        val classOpt = tag.getString("SelectedClass")
        selectedClass = if (classOpt.isPresent) classOpt.get() else null

        currentLevel = tag.getInt("CurrentLevel").orElse(1)
        currentXp = tag.getLong("CurrentXP").orElse(0L)
        totalXp = tag.getLong("TotalXP").orElse(0L)

        unlockedPerks.clear()
        val perksListOpt = tag.getList("UnlockedPerks")
        if (perksListOpt.isPresent) {
            val perksList = perksListOpt.get()
            for (i in 0 until perksList.size) {
                val element = perksList.get(i)
                if (element is NbtString) {
                    unlockedPerks.add(element.toString().removeSurrounding("\""))
                }
            }
        }

        val pathOpt = tag.getString("SelectedPath")
        selectedPath = if (pathOpt.isPresent) pathOpt.get() else null
    }

    override fun writeToNbt(tag: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        tag.putBoolean("HasSelectedClass", hasSelectedClass)
        selectedClass?.let { tag.putString("SelectedClass", it) }

        tag.putInt("CurrentLevel", currentLevel)
        tag.putLong("CurrentXP", currentXp)
        tag.putLong("TotalXP", totalXp)

        val perksList = NbtList()
        unlockedPerks.forEach { perksList.add(NbtString.of(it)) }
        tag.put("UnlockedPerks", perksList)

        selectedPath?.let { tag.putString("SelectedPath", it) }
    }
    override fun hasPerk(perkId: String): Boolean {
        return unlockedPerks.contains(perkId)
    }

    override fun getXpForNextLevel(): Long {
        return (100.0 * 1.15.pow(currentLevel.toDouble())).toLong()
    }

    override fun readData(p0: ReadView) {
        TODO("Not yet implemented")
    }

    override fun writeData(p0: WriteView) {
        TODO("Not yet implemented")
    }
}