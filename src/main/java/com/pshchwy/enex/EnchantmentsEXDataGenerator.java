package com.pshchwy.enex;

import com.pshchwy.enex.datagen.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

/// Data Generator entrypoint. When Data Generation is executed in Gradle, executes all providers in the pack.
@EventBusSubscriber(modid = EnchantmentsEX.MOD_ID)
public class EnchantmentsEXDataGenerator extends GatherDataEvent {

    public EnchantmentsEXDataGenerator(ModContainer mc, DataGenerator dataGenerator, DataGeneratorConfig dataGeneratorConfig) {
        super(mc, dataGenerator, dataGeneratorConfig);
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {

        // provider registrations
        event.createProvider(EXBlockTagProvider::new);
        event.createProvider(EXItemTagProvider::new);
        event.createProvider(EXEnchantmentTagProvider::new);
        event.createProvider(EXMobTagProvider::new);
        event.createProvider(EXEnchantmentGenerator::new);
        event.createProvider(EXLootTableProvider::new);
        event.createProvider(EXRecipeProvider.Runner::new);

    }
}
