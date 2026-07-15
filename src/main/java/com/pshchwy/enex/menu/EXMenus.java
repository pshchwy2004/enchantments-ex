package com.pshchwy.enex.menu;

import com.pshchwy.enex.EnchantmentsEX;
import com.pshchwy.enex.menu.custom.StampingTableMenu;
import com.pshchwy.enex.menu.custom.StampingTableScreen;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class EXMenus {
    public static final MenuType<StampingTableMenu> STAMPING_TABLE_MENU =
            register("stamping_table_menu", StampingTableMenu::new);

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
