package com.twk.protagony.mixin;

import com.twk.protagony.system.xp.listener.CraftingXpListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingResultSlot.class)
public abstract class CraftingResultSlotMixin {

    @Inject(method = "onTakeItem", at = @At("HEAD"))
    private void onItemCrafted(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        if (player instanceof ServerPlayerEntity serverPlayer && !player.getWorld().isClient()) {
            CraftingXpListener.INSTANCE.onItemCrafted(serverPlayer, stack);
        }
    }
}
