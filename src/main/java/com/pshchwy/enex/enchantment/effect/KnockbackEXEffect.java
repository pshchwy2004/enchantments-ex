package com.pshchwy.enex.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

/**
 * Knockback EX effect. Causes the victim to gain a guaranteed vertical velocity.
 * @param amount
 */
public record KnockbackEXEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<KnockbackEXEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("amount").forGetter(KnockbackEXEffect::amount)
            ).apply(instance, KnockbackEXEffect::new)
    );

    @Override
    public void apply(@NonNull ServerLevel world, int level, @NonNull EnchantedItemInUse context, @NonNull Entity target, @NonNull Vec3 pos) {

        if (target instanceof LivingEntity victim) {
            // Give target vertical knockback corresponding to the weapon's enchantment level
            victim.push(0, this.amount.calculate(level), 0); //
        }
    }

    @Override
    public @NotNull MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
