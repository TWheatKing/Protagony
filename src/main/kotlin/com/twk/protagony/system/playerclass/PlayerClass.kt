package com.twk.protagony.system.playerclass

enum class PlayerClass(
    val id: String,
    val displayName: String,
    val description: String,
    val xpMultipliers: XpMultipliers,
    val startingBonuses: List<String>
) {
    SCAVENGER(
        id = "scavenger",
        displayName = "The Scavenger",
        description = "Focused on immediate survival, conserving resources, and exploiting the environment",
        xpMultipliers = XpMultipliers(
            fishing = 2.0,
            looting = 2.0,
            mining = 1.0,
            combat = 0.5,
            crafting = 1.0,
            enchanting = 1.0
        ),
        startingBonuses = listOf(
            "Fortune I on fishing rods",
            "+10% hunger efficiency"
        )
    ),
    INITIATE(
        id = "initiate",
        displayName = "The Initiate",
        description = "Focused on rapid progression, crafting, and knowledge acquisition",
        xpMultipliers = XpMultipliers(
            fishing = 1.0,
            looting = 1.0,
            mining = 1.0,
            combat = 1.0,
            crafting = 2.0,
            enchanting = 2.0
        ),
        startingBonuses = listOf(
            "-10% XP cost for enchanting",
            "+1 max enchant level on books"
        )
    ),
    VANGUARD(
        id = "vanguard",
        displayName = "The Vanguard",
        description = "Focused on direct combat, defense, and facing threats head-on",
        xpMultipliers = XpMultipliers(
            fishing = 1.0,
            looting = 1.0,
            mining = 1.0,
            combat = 2.0,
            crafting = 1.0,
            enchanting = 1.0
        ),
        startingBonuses = listOf(
            "+10% melee damage",
            "+1 armor toughness"
        )
    );

    companion object {
        fun fromId(id: String): PlayerClass? = entries.find { it.id == id }
    }
}

data class XpMultipliers(
    val fishing: Double,
    val looting: Double,
    val mining: Double,
    val combat: Double,
    val crafting: Double,
    val enchanting: Double
)