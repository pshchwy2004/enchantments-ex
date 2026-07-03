package com.pshchwy.enex;

import com.pshchwy.enex.datagen.EXEnchantmentGenerator;
import com.pshchwy.enex.datagen.EXMobTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class EnchantmentsEXDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack(); // create pack
        pack.addProvider(EXEnchantmentGenerator::new);
        pack.addProvider(EXMobTagProvider::new);
    }
}
