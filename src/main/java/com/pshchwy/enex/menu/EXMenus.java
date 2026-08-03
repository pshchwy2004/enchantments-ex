package com.pshchwy.enex.menu;

import com.pshchwy.enex.EnchantmentsEX;
import com.pshchwy.enex.menu.custom.StampingTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/// Initializes all menus.
public class EXMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, EnchantmentsEX.MOD_ID);
    public static final DeferredHolder<MenuType<?>, MenuType<StampingTableMenu>> STAMPING_TABLE_MENU =
            MENUS.register("stamping_table_menu", () ->
                    IMenuTypeExtension.create((windowId, inv, data) -> {
                        // Decodes the BlockPos sent from the server upon opening the menu
                        BlockPos pos = BlockPos.STREAM_CODEC.decode(data);
                        return new StampingTableMenu(windowId, inv, pos);
                    })
            );
    public static void initialize(IEventBus eventBus) {
        EnchantmentsEX.LOGGER.info("Registering menus for " + EnchantmentsEX.MOD_ID);
        MENUS.register(eventBus);
    }
}
