package com.pshchwy.enex.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record DepthStriderEXEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<DepthStriderEXEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("amount").forGetter(DepthStriderEXEffect::amount)
            ).apply(instance, DepthStriderEXEffect::new)
    );

    @Override
    public void apply(ServerLevel world, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {
        // executes per tick
        // any dolphin within 20 blocks of the player not hostile to the player gains Strength and Speed (according to the level of enchantment)
        if (target instanceof Player player && player.isInWater()) {
            AABB area = player.getBoundingBox().inflate(20.0);
            List<Dolphin> dolphins = world.getEntitiesOfClass(
                    Dolphin.class,
                    area
            );
            for (Dolphin dolphin : dolphins) {
                dolphin.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 10, level - 1));
                dolphin.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, level - 1));
                dolphin.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 10, level - 1));
            }
        }
    }

    @Override
    public @NotNull MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
