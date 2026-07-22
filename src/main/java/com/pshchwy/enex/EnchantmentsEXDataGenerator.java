package com.pshchwy.enex;

import com.pshchwy.enex.datagen.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

/// Data Generator entrypoint. When Data Generation is executed in Gradle, executes all providers in the pack.
public class EnchantmentsEXDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack(); // create pack
        pack.addProvider(EXEnchantmentGenerator::new);
        pack.addProvider(EXMobTagProvider::new);
        pack.addProvider(EXItemTagProvider::new);
        pack.addProvider(EXRecipeProvider::new);
        pack.addProvider(EXLootTableGenerator::new);
        pack.addProvider(EXBlockTagProvider::new);
        pack.addProvider(EXEnchantmentTagProvider::new);
    }
}
