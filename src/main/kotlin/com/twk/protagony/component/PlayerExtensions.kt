package com.twk.protagony.component

import net.minecraft.entity.player.PlayerEntity

val PlayerEntity.protagonyData: PlayerDataComponent
    get() = ModComponents.PLAYER_DATA.get(this)