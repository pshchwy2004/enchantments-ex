package com.pshchwy.enex.block.entity;

import com.pshchwy.enex.EnchantmentsEX;
import com.pshchwy.enex.block.EXBlocks;
import com.pshchwy.enex.block.entity.custom.StampingTableBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

/// This class declares all block entities.
public class EXBlockEntities {
    public static final BlockEntityType<StampingTableBlockEntity> STAMPING_TABLE_BLOCK_ENTITY =
            register("stamping_table_entity", StampingTableBlockEntity::new, EXBlocks.STAMPING_TABLE);

    private static <T extends BlockEntity> BlockEntityType<T> register(String name,
                                                                       BlockEntityType.BlockEntitySupplier<? extends T> entityFactory,
                                                                       Block... blocks) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, name);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, BlockEntityType.Builder.<T>of(entityFactory, blocks).build());
    }
    public static void initialize() {
        EnchantmentsEX.LOGGER.info("Registering block entities for " + EnchantmentsEX.MOD_ID);
    }
}
