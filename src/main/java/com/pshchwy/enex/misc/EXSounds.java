package com.pshchwy.enex.misc;

import com.pshchwy.enex.EnchantmentsEX;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import static com.pshchwy.enex.EnchantmentsEX.MOD_ID;

public class EXSounds {
    public static final SoundEvent STAMP = Registry.register(BuiltInRegistries.SOUND_EVENT, ResourceLocation.fromNamespaceAndPath(MOD_ID, "stamp"),
				SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "stamp")));

    public static void initialize() {
        EnchantmentsEX.LOGGER.info("Registering sounds for " + EnchantmentsEX.MOD_ID);
    }
}
