package com.pshchwy.enex.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pshchwy.enex.mixin.ProjectileAccessor;
import com.pshchwy.enex.mixin.ShulkerBulletAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ShulkerRedirectorEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<ShulkerRedirectorEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("amount").forGetter(ShulkerRedirectorEffect::amount)
            ).apply(instance, ShulkerRedirectorEffect::new)
    );

    @Override
    public void apply(ServerLevel world, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {
        // per tick
        AABB box = target.getBoundingBox();
        box = box.inflate(level * 5);
        List<ShulkerBullet> bullets = world.getEntitiesOfClass(ShulkerBullet.class, box);

        for (ShulkerBullet bullet : bullets) {
            if (bullet.getOwner() != null && bullet.getOwner() instanceof Shulker) {
                // set target to the owner if not already
                if (((ShulkerBulletAccessor) bullet).enex$getFinalTarget() != bullet.getOwner() && ((ProjectileAccessor) bullet).enex$checkLeftOwner()) {
                    ((ShulkerBulletAccessor) bullet).enex$setFinalTarget(bullet.getOwner());
                    ((ShulkerBulletAccessor) bullet).enex$setTargetId(bullet.getOwner().getUUID());
                }
            } else if (bullet.getOwner() == null) {
                // if the owner is dead, cause the bullet to die
                ((ShulkerBulletAccessor) bullet).enex$setFinalTarget(null);
                ((ShulkerBulletAccessor) bullet).enex$setTargetId(null);
            }
        }

    }

    @Override
    public @NotNull MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}