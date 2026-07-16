package com.pshchwy.enex;

import com.pshchwy.enex.menu.EXMenus;
import com.pshchwy.enex.menu.custom.StampingTableScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class EnchantmentsEXClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MenuScreens.register(EXMenus.STAMPING_TABLE_MENU, StampingTableScreen::new);
    }
}
