package com.pshchwy.enex.mixin;

import com.pshchwy.enex.enchantment.EXEnchantmentEffects;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LootItemRandomChanceWithEnchantedBonusCondition.class)
public class LootItemRandomChanceWithEnchantedBonusConditionMixin {
    @Redirect(
            method = "test*",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getEnchantmentLevel(Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/LivingEntity;)I"
            )
    )
    private int redirectGetEnchantmentLevel(Holder<Enchantment> enchantmentHolder, LivingEntity entity) {
        int baseLevel = EnchantmentHelper.getEnchantmentLevel(enchantmentHolder, entity);

        // Check if the condition is looking for vanilla Looting
        if (enchantmentHolder.is(net.minecraft.world.item.enchantment.Enchantments.LOOTING)) {
            // Retrieve your Looting EX holder/level
            Holder<Enchantment> lootingExHolder = entity.registryAccess()
                    .lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT)
                    .getOrThrow(EXEnchantmentEffects.LOOTING_EX);

            int exLevel = EnchantmentHelper.getEnchantmentLevel(lootingExHolder, entity);

            // Return whichever is higher or combine them
            return baseLevel + exLevel;
        }

        return baseLevel;
    }
}
