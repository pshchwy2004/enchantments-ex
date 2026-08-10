package com.pshchwy.enex.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.UUID;

@Mixin(ShulkerBullet.class)
public interface ShulkerBulletAccessor {
    @Accessor("finalTarget")
    void enex$setFinalTarget(Entity target);

    @Accessor("finalTarget")
    Entity enex$getFinalTarget();



    @Accessor("targetId")
    void enex$setTargetId(UUID id);
}
