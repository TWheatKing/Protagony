package com.twk.protagony.mixin;

import com.twk.protagony.system.xp.listener.EnchantingXpListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantmentScreenHandlerMixin {

    @Inject(method = "onButtonClick", at = @At("RETURN"))
    private void onEnchant(PlayerEntity player, int id, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && player instanceof ServerPlayerEntity serverPlayer) {
            // Map slot ID to estimated enchantment levels
            int estimatedLevels;
            switch (id) {
                case 0 -> estimatedLevels = 10;
                case 1 -> estimatedLevels = 17;
                case 2 -> estimatedLevels = 25;
                default -> estimatedLevels = 10;
            }
            
            EnchantingXpListener.INSTANCE.onItemEnchanted(serverPlayer, estimatedLevels);
        }
    }
}
