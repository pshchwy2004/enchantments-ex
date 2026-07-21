package com.pshchwy.enex.menu;

import com.pshchwy.enex.EnchantmentsEX;
import com.pshchwy.enex.menu.custom.StampingTableMenu;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

/// Initializes all menus.
public class EXMenus {
    public static final MenuType<StampingTableMenu> STAMPING_TABLE_MENU =
            Registry.register(
                    BuiltInRegistries.MENU,
                    ResourceLocation.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, "stamping_table_menu"),
                    new ExtendedScreenHandlerType<>(StampingTableMenu::new, BlockPos.STREAM_CODEC)
            );
    @SuppressWarnings("unused")
    public static <T extends AbstractContainerMenu> MenuType<T> register(
            String name,
            MenuType.MenuSupplier<T> constructor
    ) {
        return Registry.register(BuiltInRegistries.MENU, name, new MenuType<>(constructor, FeatureFlagSet.of()));
    }
    public static void initialize() {
        EnchantmentsEX.LOGGER.info("Registering menus for " + EnchantmentsEX.MOD_ID);
    }
}
