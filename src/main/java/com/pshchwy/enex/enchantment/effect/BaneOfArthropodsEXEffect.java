package com.pshchwy.enex.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

/**
 * Bane of Arthropods EX effect. Renders the weapon's holder immune to poison, and causes all cobwebs the holder touches to break as if with a sword.
 * @param amount
 */
public record BaneOfArthropodsEXEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<BaneOfArthropodsEXEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("amount").forGetter(BaneOfArthropodsEXEffect::amount)
            ).apply(instance, BaneOfArthropodsEXEffect::new)
    );

    @Override
    public void apply(ServerLevel world, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {
        // executes per tick
        // If the player has Poison (any level), remove it
        if (target instanceof Player player) {
            if (player.hasEffect(MobEffects.POISON)) {
                player.removeEffect(MobEffects.POISON);
            }
            // if the player is touching a Cobweb block, break it
            if (!world.isClientSide) {
                AABB box = player.getBoundingBox();
                // iterate through all blocks touched by bounding box
                for (BlockPos blockPos : BlockPos.betweenClosed(
                        Mth.floor(box.minX),
                        Mth.floor(box.minY),
                        Mth.floor(box.minZ),

                        Mth.floor(box.maxX),
                        Mth.floor(box.maxY),
                        Mth.floor(box.maxZ)
                )) {
                    if (world.getBlockState(blockPos).is(Blocks.COBWEB)) {
                        world.destroyBlock(blockPos, true, player);
                    }
                }
            }
        }



    }

    @Override
    public @NotNull MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
