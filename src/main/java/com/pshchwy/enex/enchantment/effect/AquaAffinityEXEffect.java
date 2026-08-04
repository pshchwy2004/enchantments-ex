package com.pshchwy.enex.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Aqua Affinity EX effect. This effect causes the enchanted item's holder to be immune to Mining Fatigue, though does not dissuade Elder Guardians from attempting to inflict Mining Fatigue on the player.
 * @param amount
 */
public record AquaAffinityEXEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<AquaAffinityEXEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("amount").forGetter(AquaAffinityEXEffect::amount)
            ).apply(instance, AquaAffinityEXEffect::new)
    );

    @Override
    public void apply(ServerLevel world, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {
        // executes per tick
        // If the player has Mining Fatigue (any level), remove it
        // All Elder Guardians within a 30 block radius get Glowing
        if (target instanceof Player player) {
            if (player.hasEffect(MobEffects.SLOWNESS)) {
                player.removeEffect(MobEffects.SLOWNESS);
            }
            AABB box = player.getBoundingBox().inflate(30);
            List<ElderGuardian> elderGuardians = world.getEntitiesOfClass(ElderGuardian.class, box);
            for (ElderGuardian elderGuardian : elderGuardians) {
                elderGuardian.addEffect(new MobEffectInstance(MobEffects.GLOWING, 300, 0));
            }
        }
    }

    @Override
    public @NotNull MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
