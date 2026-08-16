package com.pshchwy.enex.mixin;

import com.pshchwy.enex.enchantment.EXEnchantmentEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ApplyBonusCount.class)
public class ApplyBonusCountMixin {

    @Shadow
    @Final
    private Holder<Enchantment> enchantment;

    @Redirect(
            method = "run",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getItemEnchantmentLevel(Lnet/minecraft/core/Holder;Lnet/minecraft/world/item/ItemStack;)I"
            )
    )
    private int redirectGetItemEnchantmentLevel(Holder<Enchantment> enchantment, ItemStack piece, ItemStack itemStack, LootContext context) {
        int vanillaLevel = EnchantmentHelper.getItemEnchantmentLevel(enchantment, piece);

        if (this.enchantment.is(Enchantments.FORTUNE)) {
            Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
            if (entity instanceof LivingEntity livingEntity) {
                HolderLookup.RegistryLookup<Enchantment> lookup = livingEntity.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
                Holder<Enchantment> fortuneExHolder = lookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX);

                int exLevel = EnchantmentHelper.getItemEnchantmentLevel(fortuneExHolder, piece);
                return vanillaLevel + exLevel;
            }
        }

        return vanillaLevel;
    }
}
