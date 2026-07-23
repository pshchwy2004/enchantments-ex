package com.pshchwy.enex.block.entity.custom;

import com.pshchwy.enex.block.entity.EXBlockEntities;
import com.pshchwy.enex.menu.custom.StampingTableMenu;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

/// This class declares the entity for the Stamping Table block. This is necessary due to the need for a GUI and container system.
public class StampingTableBlockEntity extends BlockEntity implements ExtendedMenuProvider<BlockPos> {
    public StampingTableBlockEntity(BlockPos pos, BlockState state) {
        super(EXBlockEntities.STAMPING_TABLE_BLOCK_ENTITY, pos, state);
    }

    @Override
    public @NonNull BlockPos getScreenOpeningData(@NonNull ServerPlayer player) {
        return this.worldPosition;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.enchantments-ex.stamping_table_function");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new StampingTableMenu(i, inventory, this.worldPosition);
    }
}
