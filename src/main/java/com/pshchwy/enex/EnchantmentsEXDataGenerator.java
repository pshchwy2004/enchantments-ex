package com.pshchwy.enex;

import com.pshchwy.enex.datagen.EXEnchantmentGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class EnchantmentsEXDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack(); // create pack
        pack.addProvider(EXEnchantmentGenerator::new);
    }
}
