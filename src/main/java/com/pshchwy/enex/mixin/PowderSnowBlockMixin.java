package com.pshchwy.enex.mixin;

import com.pshchwy.enex.enchantment.EXEnchantmentEffects;
import com.pshchwy.enex.misc.EXEnchantmentHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixin {
    @Inject(method = "canEntityWalkOnPowderSnow", at = @At(value = "HEAD"), cancellable = true)
    private static void modCanEntityWalkOnPowderSnow(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof LivingEntity le && EXEnchantmentHelper.hasEnchantment(le.getItemBySlot(EquipmentSlot.FEET), EXEnchantmentEffects.FROST_WALKER_EX)) {
            cir.setReturnValue(true);
        }
    }
}
