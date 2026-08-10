package com.pshchwy.enex.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ShulkerBullet.class)
public interface ShulkerBulletAccessor {
    @Accessor("finalTarget")
    void enex$setFinalTarget(EntityReference<Entity> target);

    @Accessor("finalTarget")
    EntityReference<Entity> enex$getFinalTarget();


}
