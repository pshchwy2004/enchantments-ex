package com.pshchwy.enex.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pshchwy.enex.mixin.ZombieVillagerAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.zombie.ZombieVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public record CureZombieVillagerEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<CureZombieVillagerEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("amount").forGetter(CureZombieVillagerEffect::amount)
            ).apply(instance, CureZombieVillagerEffect::new)
    );

    @Override
    public void apply(@NonNull ServerLevel world, int level, @NonNull EnchantedItemInUse context, @NonNull Entity target, @NonNull Vec3 pos) {
        // cure zombie villager
        if ((target instanceof ZombieVillager zombieVillager) && (context.owner() instanceof Player player)) {
            ((ZombieVillagerAccessor) zombieVillager).enex$setConversionStarter(player.getUUID());
            ((ZombieVillagerAccessor) zombieVillager).enex$convertVillager(world);
        }

    }

    @Override
    public @NotNull MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
