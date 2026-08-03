package com.pshchwy.enex;

import com.pshchwy.enex.menu.EXMenus;
import com.pshchwy.enex.menu.custom.StampingTableScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@Mod(value = EnchantmentsEX.MOD_ID)
/// Client mod initializer class. Used to initialize client-side classes, like frontend GUIs.
@EventBusSubscriber(modid = EnchantmentsEX.MOD_ID, value = Dist.CLIENT)
public class EnchantmentsEXClient {
	@SubscribeEvent
	public static void registerScreens(RegisterMenuScreensEvent event) {
		// EXMenus.STAMPING_TABLE_MENU should be a Supplier<MenuType<StampingTableMenu>> or DeferredHolder
		event.register(EXMenus.STAMPING_TABLE_MENU.get(), StampingTableScreen::new);
	}
}
