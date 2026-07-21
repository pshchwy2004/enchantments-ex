package com.pshchwy.enex.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Blast Protection EX effect. This causes all creepers in a 5 block radius to get Slowness.
 * @param amount
 */
public record BlastProtectionEXEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<BlastProtectionEXEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("amount").forGetter(BlastProtectionEXEffect::amount)
            ).apply(instance, BlastProtectionEXEffect::new)
    );

    @Override
    public void apply(ServerLevel world, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {
        // executes per tick
        if (target instanceof Player player) { // players only: creepers get Slowness (amplifier depending on level)
            double radius = 5.0; // var declared for future balance convenience
            AABB box = player.getBoundingBox().inflate(radius); // player bounding box inflated
            List<Creeper> creepers = world.getEntitiesOfClass( // all creepers in area
                    Creeper.class,
                    box
            );
            for (Creeper creeper : creepers) {
                creeper.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, level - 1));
            }
        }
    }

    @Override
    public @NotNull MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
