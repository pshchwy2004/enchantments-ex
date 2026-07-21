package com.pshchwy.enex;

import com.pshchwy.enex.menu.EXMenus;
import com.pshchwy.enex.menu.custom.StampingTableScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

/// Client mod initializer class. Used to initialize client-side classes, like frontend GUIs.
public class EnchantmentsEXClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MenuScreens.register(EXMenus.STAMPING_TABLE_MENU, StampingTableScreen::new);
    }
}
