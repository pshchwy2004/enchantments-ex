package com.pshchwy.enex.block.entity.custom;

import com.pshchwy.enex.block.entity.EXBlockEntities;
import com.pshchwy.enex.menu.custom.StampingTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/// This class declares the entity for the Stamping Table block. This is necessary due to the need for a GUI and container system.
public class StampingTableBlockEntity extends BlockEntity implements MenuProvider {
    public StampingTableBlockEntity(BlockPos pos, BlockState state) {
        super(EXBlockEntities.STAMPING_TABLE_BLOCK_ENTITY.get(), pos, state);
    }


    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.enchantmentsex.stamping_table_function");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        return new StampingTableMenu(i, inventory, this.worldPosition);
    }
}
