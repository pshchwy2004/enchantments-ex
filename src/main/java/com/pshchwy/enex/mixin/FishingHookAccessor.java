package com.pshchwy.enex.mixin;

import net.minecraft.world.entity.projectile.FishingHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/// FishingHook accessor meant to acquire the value of the "nibble" field, normally private in the FishingHook class.
@Mixin(FishingHook.class)
public interface FishingHookAccessor {
    @Accessor("nibble")
    int enex$getNibble();
}
