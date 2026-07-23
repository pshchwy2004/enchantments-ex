package com.pshchwy.enex.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

/**
 * Respiration EX effect. When a player has an effect includes water breathing, gains Regeneration.
 * @param amount
 */
public record RespirationEXEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<RespirationEXEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("amount").forGetter(RespirationEXEffect::amount)
            ).apply(instance, RespirationEXEffect::new)
    );

    @Override
    public void apply(@NonNull ServerLevel world, int level, @NonNull EnchantedItemInUse context, @NonNull Entity target, @NonNull Vec3 pos) {
        // executes per tick
        // if the player has Dolphin's Grace, Water Breathing, or Conduit Power, apply Regeneration per enchantment level
        if (target instanceof Player player && (player.hasEffect(MobEffects.DOLPHINS_GRACE) || player.hasEffect(MobEffects.WATER_BREATHING) || player.hasEffect(MobEffects.CONDUIT_POWER))) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, level - 1));
        }

    }

    @Override
    public @NotNull MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
