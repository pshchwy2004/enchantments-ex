package com.pshchwy.enex.enchantment;

import com.mojang.serialization.MapCodec;
import com.pshchwy.enex.EnchantmentsEX;
import com.pshchwy.enex.enchantment.effect.KnockbackEXEffect;
import com.pshchwy.enex.enchantment.effect.SharpnessEXEffect;
import com.pshchwy.enex.enchantment.effect.SmiteEXEffect;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;

public class EXEnchantmentEffects {
    public static final ResourceKey<Enchantment> KNOCKBACK_EX = of("knockback_ex");
    public static MapCodec<KnockbackEXEffect> KNOCKBACK_EX_EFFECT = register("knockback_ex_effect", KnockbackEXEffect.CODEC);

    public static final ResourceKey<Enchantment> SHARPNESS_EX = of("sharpness_ex");
    public static MapCodec<SharpnessEXEffect> SHARPNESS_EX_EFFECT = register("sharpness_ex_effect", SharpnessEXEffect.CODEC);

    public static final ResourceKey<Enchantment> SMITE_EX = of("smite_ex");
    public static MapCodec<SmiteEXEffect> SMITE_EX_EFFECT = register("smite_ex_effect", SmiteEXEffect.CODEC);

    private static ResourceKey<Enchantment> of(String path) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, path);
        return ResourceKey.create(Registries.ENCHANTMENT, id);
    }

    private static <T extends EnchantmentEntityEffect> MapCodec<T> register(String id, MapCodec<T> codec) {
        return Registry.register(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE, ResourceLocation.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, id), codec);
    }

    public static void registerEnchantmentEffects() {
        EnchantmentsEX.LOGGER.info("Registering enchantment effects for " + EnchantmentsEX.MOD_ID);
    }
}
