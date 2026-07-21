package com.pshchwy.enex.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

/**
 * Smite EX effect. Gives the victim Weakness, which scales to Weakness V when the victim is either sensitive to Smite or is a Warden.
 * @param amount
 */
public record SmiteEXEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<SmiteEXEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("amount").forGetter(SmiteEXEffect::amount)
            ).apply(instance, SmiteEXEffect::new)
    );

    @Override
    public void apply(ServerLevel world, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {
        // apply Weakness V to all undead mobs + warden, apply Weakness I to all other living mobs
        if (target instanceof LivingEntity victim) {
            if (!victim.canBeAffected(new MobEffectInstance(MobEffects.WEAKNESS, 1, 0))) {
                return;
            }
            boolean strong = victim.getType().is(EntityTypeTags.SENSITIVE_TO_SMITE) || victim.getType() == EntityType.WARDEN;
            int amplifier = strong ? 4 : 0;
            victim.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 3000, amplifier));
        }

    }

    @Override
    public @NotNull MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
