package com.pshchwy.enex.item;

import com.pshchwy.enex.EnchantmentsEX;
import com.pshchwy.enex.block.EXBlocks;
import com.pshchwy.enex.item.custom.MoltenInkItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/// Initializes all items for NeoForge.
public class EXItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(EnchantmentsEX.MOD_ID);

    public static final DeferredItem<Item> NETHER_CRYSTAL_FRAGMENT = ITEMS.register(
            "nether_crystal_fragment", registryName -> new Item(
                    new Item.Properties()
                            .fireResistant()
                            .setId(ResourceKey.create(Registries.ITEM, registryName))
            )
    );

    public static final DeferredItem<MoltenInkItem> MOLTEN_INK = ITEMS.register(
            "molten_ink", registryName -> new MoltenInkItem(
                    new Item.Properties()
                            .fireResistant()
                            .stacksTo(1)
                            .setId(ResourceKey.create(Registries.ITEM, registryName))
                            .craftRemainder(Items.GLASS_BOTTLE)
                            .component(DataComponents.CONSUMABLE,
                                    Consumable.builder()
                                            .consumeSeconds(2.0F) // 40 ticks = 2.0 seconds
                                            .animation(ItemUseAnimation.DRINK)
                                            .sound(SoundEvents.HONEY_DRINK)
                                            .hasConsumeParticles(false)
                                            .build())
            )
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

    public static ResourceKey<Item> getRK(Item item) {
        return BuiltInRegistries.ITEM.getResourceKey(item).get();
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