package com.pshchwy.enex.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ResultContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemCombinerMenu.class)
public interface ItemCombinerMenuAccessor extends AbstractContainerMenuAccessor {
    @Accessor("inputSlots")
    Container enex$getInputSlots();

    @Accessor("resultSlots")
    ResultContainer enex$getResultSlots();

    @Accessor("player")
    Player enex$getPlayer();
}
