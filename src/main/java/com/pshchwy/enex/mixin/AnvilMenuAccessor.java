package com.pshchwy.enex.mixin;

import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AnvilMenu.class)
public interface AnvilMenuAccessor extends ItemCombinerMenuAccessor {
    @Accessor("cost")
    DataSlot enex$getCost();

    @Accessor("itemName")
    String enex$getItemName();

    @Accessor("onlyRenaming")
    void enex$setOnlyRenaming(boolean value);



    @Accessor("repairItemCountCost")
    void enex$setRepairItemCountCost(int cost);
}
