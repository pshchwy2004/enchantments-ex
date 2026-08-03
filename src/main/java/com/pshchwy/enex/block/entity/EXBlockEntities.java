package com.pshchwy.enex.block.entity;

import com.pshchwy.enex.EnchantmentsEX;
import com.pshchwy.enex.block.EXBlocks;
import com.pshchwy.enex.block.entity.custom.StampingTableBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/// This class declares all block entities.
public class EXBlockEntities {
    // 1. Create the DeferredRegister for Block Entity Types
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, EnchantmentsEX.MOD_ID);

    // 2. Register Stamping Table Block Entity Type
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StampingTableBlockEntity>> STAMPING_TABLE_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register("stamping_table_entity", () ->
                    BlockEntityType.Builder.of(
                            StampingTableBlockEntity::new,
                            EXBlocks.STAMPING_TABLE.get()
                    ).build(null)
            );

    // 3. Attach the DeferredRegister to the mod event bus
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}
