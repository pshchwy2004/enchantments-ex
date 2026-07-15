package com.pshchwy.enex.block.entity.custom;

import com.pshchwy.enex.block.entity.EXBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class StampingTableBlockEntity extends BlockEntity {
    public StampingTableBlockEntity(BlockPos pos, BlockState state) {
        super(EXBlockEntities.STAMPING_TABLE_BLOCK_ENTITY, pos, state);
    }
}
