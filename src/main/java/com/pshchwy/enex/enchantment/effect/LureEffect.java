package com.pshchwy.enex.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pshchwy.enex.mixin.FishingHookAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record LureEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<LureEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("amount").forGetter(LureEffect::amount)
            ).apply(instance, LureEffect::new)
    );

    @Override
    public void apply(ServerLevel world, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {
        // executes per tick
        // if the player is fishing and the fishing hook is out, automatically reel in when the reel is ready

        if (target instanceof Player player && player.fishing != null && ((FishingHookAccessor) player.fishing).enex$getNibble() > 0) {
            FishingHook fishingHook = player.fishing;
            fishingHook.retrieve(context.itemStack());
        }

        // any item within 2 blocks of the fishing hook is automatically teleported to the player

        if (target instanceof Player player && player.fishing != null) {
            FishingHook fishingHook = player.fishing;
            Vec3 fishingPos = fishingHook.position();
            AABB box = AABB.ofSize(fishingPos, 2, 2, 2);
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
