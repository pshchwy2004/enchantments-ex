package com.pshchwy.enex.item;

import com.pshchwy.enex.EnchantmentsEX;
import com.pshchwy.enex.item.custom.MoltenInkItem;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public class EXItems {
    public static final Item NETHER_CRYSTAL_FRAGMENT = register(
            new Item(
                    new Item.Properties().fireResistant()
            ),
            "nether_crystal_fragment");

    public static final Item MOLTEN_INK = register(
            new MoltenInkItem(
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
        FabricBrewingRecipeRegistryBuilder.BUILD.register(
                builder -> {
                    builder.registerItemRecipe(
                            Items.POTION,
                            Ingredient.of(EXItems.NETHER_CRYSTAL_FRAGMENT),
                            EXItems.MOLTEN_INK
                    );
                }
        );
    }
}
