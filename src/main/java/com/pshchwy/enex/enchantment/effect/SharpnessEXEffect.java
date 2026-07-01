package com.pshchwy.enex.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record SharpnessEXEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<SharpnessEXEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("amount").forGetter(SharpnessEXEffect::amount)
            ).apply(instance, SharpnessEXEffect::new)
    );

    @Override
    public void apply(ServerLevel world, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {
        // Post-damage effect
        if (target instanceof LivingEntity livingTarget) {
            // random chance to apply Wither 2 for 10s
            RandomSource random = world.getRandom();
            if (random.nextDouble() < 0.25) { // TODO actually test this
                livingTarget.addEffect(
                        new MobEffectInstance(
                                MobEffects.WITHER,
                                100,
                                1
                        )
                );
            }
        }
    }

    @Override
    public @NotNull MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
