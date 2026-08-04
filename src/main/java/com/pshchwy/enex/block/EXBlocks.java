package com.pshchwy.enex.block;

import com.pshchwy.enex.EnchantmentsEX;
import com.pshchwy.enex.block.custom.StampingTableBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/// Block declaration class.
@EventBusSubscriber(modid = EnchantmentsEX.MOD_ID)
public class EXBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(EnchantmentsEX.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(EnchantmentsEX.MOD_ID);
    public static final DeferredBlock<Block> STAMPING_TABLE = BLOCKS.register(
            "stamping_table", registryName -> new StampingTableBlock(
                    BlockBehaviour.Properties.of()
                            .setId(ResourceKey.create(Registries.BLOCK, registryName))
                            .sound(SoundType.WOOD)
                            .strength(2.0F)
                            .ignitedByLava()
                            .mapColor(Blocks.BIRCH_PLANKS.defaultMapColor())
            )
    );


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }
    @SubscribeEvent
    public static void buildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(STAMPING_TABLE);
        }
    }
}
