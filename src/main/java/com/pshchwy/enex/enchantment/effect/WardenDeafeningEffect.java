package com.pshchwy.enex.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record WardenDeafeningEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<WardenDeafeningEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("amount").forGetter(WardenDeafeningEffect::amount)
            ).apply(instance, WardenDeafeningEffect::new)
    );

    @Override
    public void apply(ServerLevel world, int level, @NonNull EnchantedItemInUse context, @NonNull Entity target, @NotNull Vec3 pos) {
        // executes per tick
        // every second, nearby warden's anger towards wearer decreases by level if the wearer is sneaking
        if (world.getGameTime() % 20L == 0L && context.owner() instanceof Player player) {
            if (player.isCrouching()) {
                AABB box = player.getBoundingBox().inflate(amount.calculate(level));
                List<Warden> wardens = world.getEntitiesOfClass(Warden.class, box);
                if (!wardens.isEmpty()) {
                    for (Warden warden : wardens) {
                        if (warden.getAngerManagement().getActiveAnger(player) > 0) {
                            warden.getAngerManagement().increaseAnger(player, level * -1);
                        }
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
