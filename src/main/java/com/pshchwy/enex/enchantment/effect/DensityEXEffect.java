package com.pshchwy.enex.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record DensityEXEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<DensityEXEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("amount").forGetter(DensityEXEffect::amount)
            ).apply(instance, DensityEXEffect::new)
    );

    @Override
    public void apply(ServerLevel world, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {
        // executes post-attack
        // target gets slowness, nausea, and weakness, attacker gets resistance
        if (context.owner() instanceof Player p && target instanceof LivingEntity le && MaceItem.canSmashAttack(p)) {
            le.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2000, level - 1));
            le.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 2000, level - 1));
            le.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 2000, level - 1));
            p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, level - 1));
        }
    }

    @Override
    public @NotNull MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
