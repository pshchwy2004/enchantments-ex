package com.pshchwy.enex.item;

import com.pshchwy.enex.EnchantmentsEX;
import com.pshchwy.enex.block.EXBlocks;
import com.pshchwy.enex.item.custom.MoltenInkItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/// Initializes all items for NeoForge.
public class EXItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(EnchantmentsEX.MOD_ID);

    public static final DeferredItem<Item> NETHER_CRYSTAL_FRAGMENT = ITEMS.registerItem(
            "nether_crystal_fragment",
            Item::new,
            new Item.Properties().fireResistant()
    );

    public static final DeferredItem<MoltenInkItem> MOLTEN_INK = ITEMS.registerItem(
            "molten_ink",
            MoltenInkItem::new,
            new Item.Properties().fireResistant().stacksTo(1)
    );
    @SuppressWarnings("unused")
    public static final DeferredItem<BlockItem> STAMPING_TABLE = ITEMS.registerSimpleBlockItem("stamping_table", EXBlocks.STAMPING_TABLE);

    public static void initialize(IEventBus modEventBus) {
        EnchantmentsEX.LOGGER.info("Registering items for " + EnchantmentsEX.MOD_ID);
        // Register the DeferredRegister to the mod event bus
        ITEMS.register(modEventBus);
        // Register creative tab additions
        modEventBus.addListener(EXItems::addCreative);
    }

    private static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(NETHER_CRYSTAL_FRAGMENT);
            event.accept(MOLTEN_INK);
        }
    }

    /// Event handler for brewing recipes.
    public static void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
        event.getBuilder().addRecipe(
                Ingredient.of(Items.POTION),
                Ingredient.of(NETHER_CRYSTAL_FRAGMENT.get()),
                MOLTEN_INK.get().getDefaultInstance()
        );
    }
}