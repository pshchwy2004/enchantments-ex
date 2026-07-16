package com.pshchwy.enex.menu.custom;

import com.pshchwy.enex.block.EXBlocks;
import com.pshchwy.enex.menu.EXMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class StampingTableMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private final Container stampSlots = new SimpleContainer(2) {
        @Override
        public void setChanged() {
            super.setChanged();
            StampingTableMenu.this.slotsChanged(this);
        }
    };

    public StampingTableMenu(int id, Inventory inventory, ContainerLevelAccess access) {
        super(EXMenus.STAMPING_TABLE_MENU, id);
        this.access = access;
        this.addSlot(new Slot(this.stampSlots, 0, 15, 47) { // enchanted book placement
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(Items.ENCHANTED_BOOK);
            }
        });
        this.addSlot(new Slot(this.stampSlots, 1, 35, 47));
        addPlayerHotbar(inventory);
        addPlayerInventory(inventory);
    }

    public StampingTableMenu(int id, Inventory inventory, BlockPos pos) {
        this(id, inventory, ContainerLevelAccess.create(inventory.player.level(), pos));
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (invSlot == 0) {
                if (!this.moveItemStackTo(itemStack2, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (invSlot == 1) {
                if (!this.moveItemStackTo(itemStack2, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (itemStack2.is(Items.LAPIS_LAZULI)) { // change to stamping ink
                if (!this.moveItemStackTo(itemStack2, 1, 2, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (this.slots.getFirst().hasItem() || !this.slots.getFirst().mayPlace(itemStack2)) {
                    return ItemStack.EMPTY;
                }

                ItemStack itemStack3 = itemStack2.copyWithCount(1);
                itemStack2.shrink(1);
                this.slots.getFirst().setByPlayer(itemStack3);
            }

            if (itemStack2.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemStack2);
        }

        return itemStack;
    }

    @Override
    public boolean stillValid(Player player) { // canUse
        return stillValid(this.access, player, EXBlocks.STAMPING_TABLE)
            && this.stampSlots.stillValid(player);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
