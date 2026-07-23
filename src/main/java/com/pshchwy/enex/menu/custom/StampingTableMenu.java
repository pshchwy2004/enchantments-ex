package com.pshchwy.enex.menu.custom;

import com.pshchwy.enex.block.EXBlocks;
import com.pshchwy.enex.enchantment.EXEnchantmentMap;
import com.pshchwy.enex.item.EXItems;
import com.pshchwy.enex.menu.EXMenus;
import com.pshchwy.enex.misc.EXSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
* This class is the server-side logic that applies to the stamping table. Complementary with StampingTableScreen, it handles all critical, backend logic.
 */
public class StampingTableMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private final Container stampSlots = new SimpleContainer(2) {
        @Override
        public void setChanged() {
            super.setChanged();
            StampingTableMenu.this.slotsChanged(this);
        }
    };
    private int selectedEnchantmentIndex;

    public StampingTableMenu(int id, Inventory inventory, ContainerLevelAccess access) {
        super(EXMenus.STAMPING_TABLE_MENU, id);
        this.access = access;
        selectedEnchantmentIndex = -1;
        this.addSlot(new Slot(this.stampSlots, 0, 15, 47) { // enchanted book placement
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(Items.ENCHANTED_BOOK);
            }
        });

        this.addSlot(new Slot(this.stampSlots, 1, 35, 47) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(EXItems.MOLTEN_INK);
            }
        });
        addPlayerHotbar(inventory);
        addPlayerInventory(inventory);
    }

    public StampingTableMenu(int id, Inventory inventory, BlockPos pos) {
        this(id, inventory, ContainerLevelAccess.create(inventory.player.level(), pos));
    }

    /**
     * Logic for shift clicking out of the inventory/container.
     */
    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);

        if (slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();

            // slot index ranges
            int customSlotsCount = 2;
            int totalSlotsCount = 38; // 2 block slots + 9 hotbar + 27 main inventory

            if (invSlot < customSlotsCount) {
                // stamping table slots
                // Move items into the player inventory/hotbar (slots 2 to 38)
                if (!this.moveItemStackTo(itemStack2, customSlotsCount, totalSlotsCount, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // player inventory slots
                if (itemStack2.is(Items.ENCHANTED_BOOK)) {
                    // Try moving into the Enchanted Book slot (Slot 0)
                    if (!this.moveItemStackTo(itemStack2, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (itemStack2.is(EXItems.MOLTEN_INK)) {
                    // Try moving into the Molten Ink slot (Slot 1)
                    if (!this.moveItemStackTo(itemStack2, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    // For any other item, shift-click splits between player inventory and hotbar
                    // Since hotbar is slots 2-10 and inventory is slots 11-37:
                    if (invSlot <= 10) { // From hotbar
                        if (!this.moveItemStackTo(itemStack2, 11, totalSlotsCount, false)) { // move to inventory
                            return ItemStack.EMPTY;
                        }
                    } else if (invSlot < totalSlotsCount) { // From inventory
                        if (!this.moveItemStackTo(itemStack2, 2, 11, false)) { // move to hotbar
                            return ItemStack.EMPTY;
                        }
                    }
                }
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

    /**
     * Boolean to check if the menu is still valid; i.e. if the block still exists within range of the player. Closes the menu when this is false.
     */
    @Override
    public boolean stillValid(Player player) { // canUse
        return stillValid(this.access, player, EXBlocks.STAMPING_TABLE)
            && this.stampSlots.stillValid(player);
    }

    /**
     * Helper function to add functional slots from the player inventory.
     */
    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    /**
     * Helper function to add the player's hotbar to the GUI.
     */
    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    /**
     * Parses the Enchanted Book's 1.21 Data Component map and extracts the list of holders.
     */
    public List<Holder<Enchantment>> getAvailableEnchantments() {
        ItemStack bookStack = this.stampSlots.getItem(0);
        if (bookStack.isEmpty() || !bookStack.is(Items.ENCHANTED_BOOK)) {
            return List.of();
        }

        // Retrieve the immutable 1.21 enchantment mapping component
        ItemEnchantments enchantsMap = bookStack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);

        List<Holder<Enchantment>> filteredList = new ArrayList<>();

        for (Holder<Enchantment> holder : enchantsMap.keySet()) {
            // get key -> add to new filtered list
            holder.unwrapKey().ifPresent(key -> {
                if (EXEnchantmentMap.isUpgradable(key)) {
                    filteredList.add(holder);
                }
            });
        }
        // sort list by string to ensure client-server continuity
        filteredList.sort((h1, h2) -> {
            String path1 = h1.unwrapKey().map(key -> key.identifier().toString()).orElse("");
            String path2 = h2.unwrapKey().map(key -> key.identifier().toString()).orElse("");
            return path1.compareTo(path2);
        });

        return filteredList;
    }

    /// Automatically updates whenever an item is inserted, extracted, or modified.
    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);

        // If the items changed, reset the player's screen selection safely
        if (container == this.stampSlots) {
            ItemStack bookStack = this.stampSlots.getItem(0);
            ItemStack inkStack = this.stampSlots.getItem(1);

            // If either slot is completely emptied, reset selection state
            if (bookStack.isEmpty() || inkStack.isEmpty()) {
                this.selectedEnchantmentIndex = -1;
            } else {
                // Bounds safety check: if a new book has fewer enchantments than the old one, drop selection
                int totalEnchants = getAvailableEnchantments().size();
                if (this.selectedEnchantmentIndex >= totalEnchants) {
                    this.selectedEnchantmentIndex = -1;
                }
            }
        }
    }

    /**
     * Allows the Screen to query which button should look active/selected.
     */
    public int getSelectedEnchantmentIndex() {
        return this.selectedEnchantmentIndex;
    }

    /**
     * Function that executes the function of clicking one of the available enchantments.
     */
    @Override
    public boolean clickMenuButton(Player player, int id) {
        List<Holder<Enchantment>> available = this.getAvailableEnchantments();

        // verify index exists
        if (id < 0 || id >= available.size()) {
            return false;
        }

        ItemStack bookStack = this.stampSlots.getItem(0);
        ItemStack inkStack = this.stampSlots.getItem(1);

        // secondary check for items on the serverside
        if (bookStack.isEmpty() || inkStack.isEmpty() || !inkStack.is(EXItems.MOLTEN_INK)) {
            return false;
        }

        Holder<Enchantment> targetEnchant = available.get(id); // gets enchantment

        // update the book's data components
        // create a copy of the book to safely mutate its components
        ItemStack upgradedBook = bookStack.copy();
        ItemEnchantments currentEnchants = upgradedBook.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
        ItemEnchantments.Mutable builder = new ItemEnchantments.Mutable(currentEnchants);

        int currentLevel = currentEnchants.getLevel(targetEnchant);

        // Resolve the registry key for the target enchantment, fetch its upgrade key, and lookup the new Holder
        targetEnchant.unwrapKey().ifPresent(originalKey -> {
            ResourceKey<Enchantment> exKey = com.pshchwy.enex.enchantment.EXEnchantmentMap.getUpgrade(originalKey);

            // get holder
            player.level().registryAccess().lookup(Registries.ENCHANTMENT).flatMap(registry -> registry.get(exKey)).ifPresent(exHolder -> {
                // remove the old enchantment from the book
                builder.set(targetEnchant, 0);
                // set new EX enchantment level
                builder.set(exHolder, currentLevel);
            });
        });

        // apply the mutated enchantment map back to the book copy
        upgradedBook.set(DataComponents.STORED_ENCHANTMENTS, builder.toImmutable());

        this.stampSlots.setItem(0, upgradedBook); // overwrite old book with upgraded version

        // consume Molten Ink and return an empty glass bottle
        inkStack.shrink(1); // even though Molten Ink can only have a max stack of 1, inventory edit shenanigans might cause unwanted behavior
        if (inkStack.isEmpty()) {
            // replace the slot with empty bottle
            this.stampSlots.setItem(1, new ItemStack(Items.GLASS_BOTTLE));
        } else {
            // If the player had stacked multiple Molten Inks, give them the bottle directly
            if (!player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE))) {
                player.drop(new ItemStack(Items.GLASS_BOTTLE), false);
            }
        }

        player.level().playSound(
                null,
                player.blockPosition(),
                EXSounds.STAMP,
                SoundSource.BLOCKS,
                1.0F,
                1.0F
        );

        // Reset selection state
        this.selectedEnchantmentIndex = -1;
        this.slotsChanged(this.stampSlots);
        return true;
    }

    /**
     * Void function that executes when the block is destroyed or the menu is otherwise exited by the player.
     */
    @Override
    public void removed(Player player) {
        super.removed(player);

        // Eject the contents of the stamping table back to the player when they exit
        this.access.execute((level, pos) -> {
            this.clearContainer(player, this.stampSlots);
        });
    }
}
