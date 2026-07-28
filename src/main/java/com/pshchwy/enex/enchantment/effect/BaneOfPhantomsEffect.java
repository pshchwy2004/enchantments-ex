package com.pshchwy.enex.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.List;

public record BaneOfPhantomsEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<BaneOfPhantomsEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("amount").forGetter(BaneOfPhantomsEffect::amount)
            ).apply(instance, BaneOfPhantomsEffect::new)
    );

    @Override
    public void apply(@NonNull ServerLevel world, int level, @NonNull EnchantedItemInUse context, @NonNull Entity target, @NonNull Vec3 pos) {
        // executes per tick
        if (target instanceof Player player) { // players only: phantoms
            double radius = 15.0; // var declared for future balance convenience
            AABB box = player.getBoundingBox().inflate(radius).inflate(0, 50, 0); // player bounding box inflated to extend towards the sky

            List<Phantom> phantoms = world.getEntitiesOfClass( // all creepers in area
                    Phantom.class,
                    box
            );
            for (Phantom phantom : phantoms) {
                phantom.addEffect(new MobEffectInstance(MobEffects.WITHER, 10, level - 1), player);
            }
        }
    }

    @Override
    public @NotNull MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
