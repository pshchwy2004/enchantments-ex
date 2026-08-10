package com.pshchwy.enex.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public record SparkEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<SparkEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("amount").forGetter(SparkEffect::amount)
            ).apply(instance, SparkEffect::new)
    );

    @Override
    public void apply(@NonNull ServerLevel world, int level, @NonNull EnchantedItemInUse context, @NonNull Entity target, @NonNull Vec3 pos) {
        // executes after hitting
        // spawns a lot of lava particles
        // Evaluate particle count based on the enchantment level
        int particleCount = (int) this.amount.calculate(level);

        // Center particle burst slightly above the victim's base position (chest/center level)
        double x = target.getX();
        double y = target.getY(0.5); // 50% height of target entity box
        double z = target.getZ();

        // Bounding box spread offset (X, Y, Z spread)
        double deltaX = 0.35;
        double deltaY = 0.5;
        double deltaZ = 0.35;

        // Particle speed/velocity scale
        double speed = 0.15;

        // sendParticles(particleType, x, y, z, count, deltaX, deltaY, deltaZ, speed)
        world.sendParticles(
                ParticleTypes.LAVA,
                x, y, z,
                particleCount,
                deltaX, deltaY, deltaZ,
                speed
        );

        world.playSound(null, BlockPos.containing(pos), SoundEvents.WOLF_ARMOR_CRACK,  SoundSource.HOSTILE, 1.0F, 1.0F);



    }

    @Override
    public @NotNull MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
