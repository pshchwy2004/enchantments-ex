package com.pshchwy.enex.datagen;

import com.pshchwy.enex.block.EXBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class EXBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {

    public EXBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }
    @Override
    protected void addTags(HolderLookup.@NonNull Provider wrapperLookup) {
        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(EXBlocks.getRK(EXBlocks.STAMPING_TABLE));
    }
}
