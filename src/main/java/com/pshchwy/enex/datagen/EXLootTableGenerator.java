package com.pshchwy.enex.datagen;

import com.pshchwy.enex.block.EXBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public class EXLootTableGenerator extends BlockLootSubProvider {

    public EXLootTableGenerator(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        this.dropSelf(EXBlocks.STAMPING_TABLE.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return Collections.singleton(EXBlocks.STAMPING_TABLE.get());
    }
}