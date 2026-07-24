package com.pshchwy.enex.block;

import com.pshchwy.enex.EnchantmentsEX;
import com.pshchwy.enex.block.custom.StampingTableBlock;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

/// Block declaration class.
public class EXBlocks {

    public static final Block STAMPING_TABLE = register(
            "stamping_table",
            key -> new StampingTableBlock(
                    BlockBehaviour.Properties.of()
                            .setId(key)
                            .sound(SoundType.WOOD)
                            .strength(2.0F)
                            .ignitedByLava()
                            .mapColor(Blocks.BIRCH_PLANKS.defaultMapColor())
            ),
            true
    );

    public static Block register(String name, Function<ResourceKey<Block>, Block> blockFactory, boolean shouldRegisterItem) {
        Identifier id = Identifier.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, name);
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, id);

        // Instantiate block with its bound ResourceKey
        Block block = blockFactory.apply(blockKey);

        // Register block item if needed
        if (shouldRegisterItem) {
            ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);
            BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey));
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    public static ResourceKey<Block> getRK(Block block) {
        return BuiltInRegistries.BLOCK.getResourceKey(block).get();
    }

    public static void initialize() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register((itemGroup) -> {
            itemGroup.accept(EXBlocks.STAMPING_TABLE.asItem());
        });
        EnchantmentsEX.LOGGER.info("Registering blocks for " + EnchantmentsEX.MOD_ID);
    }
}