package com.pshchwy.enex;

import com.pshchwy.enex.block.EXBlocks;
import com.pshchwy.enex.block.entity.EXBlockEntities;
import com.pshchwy.enex.enchantment.EXEnchantmentEffects;
import com.pshchwy.enex.item.EXItems;
import com.pshchwy.enex.menu.EXMenus;
import com.pshchwy.enex.misc.EXLootTableModifiers;
import com.pshchwy.enex.misc.EXSounds;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/// Mod initializer class. Every registry must first initialize in this one.
@Mod(EnchantmentsEX.MOD_ID)
public class EnchantmentsEX {
	// mod id, very important
	public static final String MOD_ID = "enchantmentsex";
	// Create a Deferred Register to hold Blocks which will all be registered under the "enex" namespace
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
	// Create a Deferred Register to hold Items which will all be registered under the "enex" namespace
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);


	public EnchantmentsEX(IEventBus modEventBus, ModContainer modContainer) {
		// Register the commonSetup method for modloading
		// modEventBus.addListener(this::commonSetup);

		// Register the Deferred Register to the mod event bus so blocks get registered
		BLOCKS.register(modEventBus);
		// Register the Deferred Register to the mod event bus so items get registered
		ITEMS.register(modEventBus);
		onInitialize(modEventBus);

	}

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	/**
	 * Initializer class. Logger prints all initializing info.
	 */
	public void onInitialize(IEventBus modEventBus) {
		// Initializes the mod.
		LOGGER.info("Initializing " + MOD_ID); // This goes first.
		EXItems.initialize(modEventBus);
		EXBlocks.register(modEventBus);
		EXBlockEntities.register(modEventBus);
		EXSounds.initialize(modEventBus);
		EXMenus.initialize(modEventBus);
		EXEnchantmentEffects.register(modEventBus);
		NeoForge.EVENT_BUS.register(EXLootTableModifiers.class);
		NeoForge.EVENT_BUS.addListener(EXItems::registerBrewingRecipes);
		LOGGER.info("Successfully initialized " + MOD_ID); // This goes last.
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
