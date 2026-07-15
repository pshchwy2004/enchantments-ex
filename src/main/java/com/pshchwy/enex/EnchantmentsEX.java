package com.pshchwy.enex;

import com.pshchwy.enex.block.EXBlocks;
import com.pshchwy.enex.block.entity.EXBlockEntities;
import com.pshchwy.enex.enchantment.EXEnchantmentEffects;
import com.pshchwy.enex.item.EXItems;
import com.pshchwy.enex.menu.EXMenus;
import com.pshchwy.enex.util.EXLootTableModifiers;
import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.ResourceLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnchantmentsEX implements ModInitializer {
	// mod id, very important
	public static final String MOD_ID = "enchantments-ex";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// Initializes the mod.
		LOGGER.info("Initializing " + MOD_ID); // This goes first.
		EXItems.initialize();
		EXBlockEntities.initialize();
		EXBlocks.initialize();
		EXMenus.initialize();
		EXEnchantmentEffects.registerEnchantmentEffects();
		EXLootTableModifiers.modifyLootTables();
		LOGGER.info("Successfully initialized " + MOD_ID); // This goes last.
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
