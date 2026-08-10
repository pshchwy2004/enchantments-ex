package com.pshchwy.enex.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public record CleanseEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<CleanseEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("amount").forGetter(CleanseEffect::amount)
            ).apply(instance, CleanseEffect::new)
    );

    @Override
    public void apply(ServerLevel world, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {
        // executes when being attacked
        // random chance (decided in generator) of removing 1 random debuff effect
        if (context.owner() instanceof Player player) {
            Map<Holder<MobEffect>, MobEffectInstance> effectSet = player.getActiveEffectsMap();
            List<Holder<MobEffect>> keyList = new ArrayList<>();
            effectSet.forEach((holder, effect) -> {
                if (holder.value().getCategory() == MobEffectCategory.BENEFICIAL) {
                    keyList.add(holder);
                }
            });
            Holder<MobEffect> effect = keyList.get(Random.from(new Random()).nextInt(keyList.size()));
            player.removeEffect(effect);
        }

    }

    @Override
    public @NotNull MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}