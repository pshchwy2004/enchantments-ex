package com.pshchwy.enex.datagen;

import com.pshchwy.enex.block.EXBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;

import java.util.concurrent.CompletableFuture;

public class EXBlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public EXBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }
    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        valueLookupBuilder(BlockTags.MINEABLE_WITH_AXE)
                .add(EXBlocks.STAMPING_TABLE);
    }
}
