package com.pshchwy.enex.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pshchwy.enex.enchantment.EXEnchantmentEffects;
import com.pshchwy.enex.misc.EXEnchantmentHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record BaneOfPhantomsEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<BaneOfPhantomsEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("amount").forGetter(BaneOfPhantomsEffect::amount)
            ).apply(instance, BaneOfPhantomsEffect::new)
    );

    @Override
    public void apply(ServerLevel world, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {
        // executes per tick
        if (target instanceof Player player) { // players only: phantoms
            double radius = 15.0; // var declared for future balance convenience
            AABB box = player.getBoundingBox().inflate(radius).inflate(0, 50, 0); // player bounding box inflated to extend towards the sky

            List<Phantom> phantoms = world.getEntitiesOfClass( // all phantoms in area
                    Phantom.class,
                    box
            );
            boolean ignite = EXEnchantmentHelper.hasEnchantment(context.itemStack(), EXEnchantmentEffects.FIRE_ASPECT_EX) || EXEnchantmentHelper.hasEnchantment(context.itemStack(), Enchantments.FIRE_ASPECT);
            for (Phantom phantom : phantoms) {
                phantom.setLastHurtByPlayer(player, 100);
                phantom.addEffect(new MobEffectInstance(MobEffects.WITHER, 10, level - 1), player);
                if (ignite && !phantom.isOnFire()) {
                    phantom.igniteForSeconds(100);
                }
            }
            List<Vex> vexes = world.getEntitiesOfClass( // all phantoms in area
                    Vex.class,
                    box
            );
            for (Vex vex : vexes) {
                vex.setLastHurtByPlayer(player, 100);
                vex.addEffect(new MobEffectInstance(MobEffects.WITHER, 10, level - 1), player);
                if (ignite && !vex.isOnFire()) {
                    vex.igniteForSeconds(100);
                }
            }
        }
    }

    @Override
    public @NotNull MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
