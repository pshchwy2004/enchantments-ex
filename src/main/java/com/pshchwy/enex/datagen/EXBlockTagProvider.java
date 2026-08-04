package com.pshchwy.enex.datagen;

import com.pshchwy.enex.EnchantmentsEX;
import com.pshchwy.enex.block.EXBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class EXBlockTagProvider extends BlockTagsProvider {

    public EXBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, EnchantmentsEX.MOD_ID);
    }
    @Override
    protected void addTags(HolderLookup.@NotNull Provider wrapperLookup) {
        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(EXBlocks.STAMPING_TABLE.get());
    }
}
