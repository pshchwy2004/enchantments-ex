package com.pshchwy.enex.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.ZombieVillager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.UUID;

@Mixin(ZombieVillager.class)
public interface ZombieVillagerAccessor {
    @Invoker("finishConversion")
    void enex$convertVillager(final ServerLevel level);

    @Accessor("conversionStarter")
    void enex$setConversionStarter(UUID conversionStarter);
}
