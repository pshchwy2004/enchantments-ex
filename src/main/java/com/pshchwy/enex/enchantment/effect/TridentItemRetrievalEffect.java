package com.pshchwy.enex.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record TridentItemRetrievalEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<TridentItemRetrievalEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("amount").forGetter(TridentItemRetrievalEffect::amount)
            ).apply(instance, TridentItemRetrievalEffect::new)
    );

    @Override
    public void apply(ServerLevel world, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {
        // executes when the trident hits a block
        // any item within 3 blocks is instantly teleported to the player
        if (context.owner() instanceof Player player) {
            AABB box = AABB.ofSize(pos, 3, 3, 3);
            List<ItemEntity> items = world.getEntitiesOfClass(
                    ItemEntity.class,
                    box
            );
            for (ItemEntity item : items) { // teleport
                item.setPos(player.getX(), player.getY(), player.getZ());
            }
        }


    }

    @Override
    public @NotNull MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
