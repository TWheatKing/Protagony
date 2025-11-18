package com.twk.protagony.mixin;

import com.twk.protagony.system.xp.listener.LootingXpListener;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.village.TradeOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MerchantEntity.class)
public abstract class MerchantEntityMixin {

    @Shadow
    public abstract PlayerEntity getCustomer();

    @Inject(method = "trade", at = @At("HEAD"))
    private void onTrade(TradeOffer offer, CallbackInfo ci) {
        PlayerEntity customer = getCustomer();
        
        if (customer instanceof ServerPlayerEntity serverPlayer && !customer.getWorld().isClient()) {
            LootingXpListener.INSTANCE.onVillagerTrade(serverPlayer);
        }
    }
}
