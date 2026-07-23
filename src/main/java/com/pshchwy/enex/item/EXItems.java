package com.pshchwy.enex.item;

import com.pshchwy.enex.EnchantmentsEX;
import com.pshchwy.enex.item.custom.MoltenInkItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.crafting.Ingredient;

/// Initializes all items.
public class EXItems {

    public static final Item NETHER_CRYSTAL_FRAGMENT = register(
            "nether_crystal_fragment",
            key -> new Item(new Item.Properties().setId(key).fireResistant())
    );

    public static final Item MOLTEN_INK = register(
            "molten_ink",
            key -> new MoltenInkItem(
                    new Item.Properties()
                            .setId(key)
                            .fireResistant()
                            .stacksTo(1)
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

    private static Item register(String id, java.util.function.Function<ResourceKey<Item>, Item> factory) {
        Identifier location = Identifier.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, id);
        ResourceKey<Item> key = ResourceKey.create(net.minecraft.core.registries.Registries.ITEM, location);
        Item item = factory.apply(key);
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    public static void initialize() {
        EnchantmentsEX.LOGGER.info("Registering items for " + EnchantmentsEX.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS)
                .register((creativeTab) -> {
                    creativeTab.accept(EXItems.NETHER_CRYSTAL_FRAGMENT);
                    creativeTab.accept(EXItems.MOLTEN_INK);
                });

        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerItemRecipe(
                    Items.POTION,
                    Ingredient.of(EXItems.NETHER_CRYSTAL_FRAGMENT),
                    EXItems.MOLTEN_INK
            );
        });
    }
}