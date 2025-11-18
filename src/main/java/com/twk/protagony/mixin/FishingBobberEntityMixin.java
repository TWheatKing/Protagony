package com.twk.protagony.mixin;

import com.twk.protagony.system.xp.XpCalculator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin {

    @Shadow
    public abstract PlayerEntity getPlayerOwner();

    @Inject(method = "use", at = @At("RETURN"))
    private void onFishCaught(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        int returnValue = cir.getReturnValue();
        
        if (returnValue > 0) {
            PlayerEntity player = getPlayerOwner();
            if (player instanceof ServerPlayerEntity serverPlayer) {
                XpCalculator.INSTANCE.awardXp(
                    serverPlayer,
                    XpCalculator.INSTANCE.calculateFishingXp(),
                    XpCalculator.XpActivity.FISHING,
                    "Fishing"
                );
            }
        }
    }
}
