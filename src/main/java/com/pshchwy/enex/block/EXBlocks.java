package com.pshchwy.enex.block;

import com.pshchwy.enex.EnchantmentsEX;
import com.pshchwy.enex.block.custom.StampingTableBlock;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

/// Block declaration class.
public class EXBlocks {
    public static final Block STAMPING_TABLE = register(
            new StampingTableBlock(
                    BlockBehaviour.Properties.of()
                            .sound(SoundType.WOOD)
                            .strength(2.0F)
                            .ignitedByLava()
                            .mapColor(Blocks.BIRCH_PLANKS.defaultMapColor())
            ),
            "stamping_table",
            true);


    public static Block register(Block block, String name, boolean shouldRegisterItem) {
        // Register the block and its item.
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, name);

        // for non-item blocks
        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Properties());
            Registry.register(BuiltInRegistries.ITEM, id, blockItem);
        }

        return Registry.register(BuiltInRegistries.BLOCK, id, block);
    }
    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register((itemGroup) -> {
            itemGroup.accept(EXBlocks.STAMPING_TABLE.asItem());
        });
        EnchantmentsEX.LOGGER.info("Registering blocks for " + EnchantmentsEX.MOD_ID);
    }
}
