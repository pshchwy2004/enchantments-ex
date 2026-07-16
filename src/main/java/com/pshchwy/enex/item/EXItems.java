package com.pshchwy.enex.item;

import com.pshchwy.enex.EnchantmentsEX;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class EXItems {
    public static final Item NETHER_CRYSTAL_FRAGMENT = register(
            new Item(
                    new Item.Properties().fireResistant()
            ),
            "nether_crystal_fragment");

    public static final Item MOLTEN_INK = register(
            new Item(
                    new Item.Properties().fireResistant().stacksTo(1)
            ),
            "molten_ink");

    public static Item register(Item item, String id) {
        // Create the identifier for the item.
        ResourceLocation itemID = ResourceLocation.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, id);

        // Return registered item
        return Registry.register(BuiltInRegistries.ITEM, itemID, item);
    }

    public static void initialize() {
        EnchantmentsEX.LOGGER.info("Registering items for " + EnchantmentsEX.MOD_ID);
    }
}
