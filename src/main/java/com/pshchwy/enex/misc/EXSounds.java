package com.pshchwy.enex.misc;

import com.pshchwy.enex.EnchantmentsEX;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.pshchwy.enex.EnchantmentsEX.MOD_ID;

public class EXSounds {
    // Assuming that your mod id is examplemod
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, MOD_ID);

    // All vanilla sounds use variable range events.
    public static final DeferredHolder<SoundEvent, SoundEvent> STAMP = SOUND_EVENTS.register(
            "stamp", // must match the resource location on the next line
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "stamp"))
    );
    public static void initialize(IEventBus bus) {
        EnchantmentsEX.LOGGER.info("Registering sounds for " + EnchantmentsEX.MOD_ID);
        SOUND_EVENTS.register(bus);
    }
}
