package com.pshchwy.enex;

import com.pshchwy.enex.datagen.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

/// Data Generator entrypoint. When Data Generation is executed in Gradle, executes all providers in the pack.
@EventBusSubscriber(modid = EnchantmentsEX.MOD_ID)
public class EnchantmentsEXDataGenerator extends GatherDataEvent {

    public EnchantmentsEXDataGenerator(ModContainer mc, DataGenerator dataGenerator, DataGeneratorConfig dataGeneratorConfig, ExistingFileHelper existingFileHelper) {
        super(mc, dataGenerator, dataGeneratorConfig, existingFileHelper);
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        var existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(
                event.includeServer(),
                new EXBlockTagProvider(packOutput, lookupProvider, existingFileHelper)
        );
        generator.addProvider(
                event.includeServer(),
                new EXItemTagProvider(packOutput, lookupProvider, existingFileHelper)
        );
        generator.addProvider(
                event.includeServer(),
                new EXEnchantmentTagProvider(packOutput, lookupProvider, existingFileHelper)
        );
        generator.addProvider(
                event.includeServer(),
                new EXMobTagProvider(packOutput, lookupProvider, existingFileHelper)
        );
        generator.addProvider(
                event.includeServer(),
                new EXEnchantmentGenerator(packOutput, lookupProvider)
        );
        generator.addProvider(
                event.includeServer(),
                new EXLootTableProvider(packOutput, lookupProvider)
        );
        generator.addProvider(
                event.includeServer(),
                new EXRecipeProvider(packOutput, lookupProvider)
        );
    }
}
