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

public record KnockbackEXEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<KnockbackEXEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("amount").forGetter(KnockbackEXEffect::amount)
            ).apply(instance, KnockbackEXEffect::new)
    );

    @Override
    public void apply(ServerLevel world, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {

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
