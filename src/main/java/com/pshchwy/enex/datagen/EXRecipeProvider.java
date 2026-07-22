package com.pshchwy.enex.datagen;

import com.pshchwy.enex.EnchantmentsEX;
import com.pshchwy.enex.block.EXBlocks;
import com.pshchwy.enex.item.EXItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/// This datagen class builds the recipe JSONs for the new items.
public class EXRecipeProvider extends FabricRecipeProvider {

    public EXRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
                shapeless(RecipeCategory.BREWING, EXItems.NETHER_CRYSTAL_FRAGMENT, 8) // You can also specify an int to produce more than one
                        .requires(Items.NETHER_STAR)
                        .requires(Items.NETHERITE_INGOT, 2)
                        .unlockedBy(getHasName(EXItems.NETHER_CRYSTAL_FRAGMENT), has(EXItems.NETHER_CRYSTAL_FRAGMENT))
                        .save(
                                exporter,
                                ResourceKey.create(
                                        Registries.RECIPE,
                                        ResourceLocation.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, "nether_crystal_fragment_from_ingots")
                                )
                        );

                shapeless(RecipeCategory.BREWING, EXItems.NETHER_CRYSTAL_FRAGMENT, 6) // You can also specify an int to produce more than one
                        .requires(Items.NETHER_STAR)
                        .requires(Items.NETHERITE_SCRAP, 8)
                        .unlockedBy(getHasName(EXItems.NETHER_CRYSTAL_FRAGMENT), has(EXItems.NETHER_CRYSTAL_FRAGMENT))
                        .save(
                                exporter,
                                ResourceKey.create(
                                        Registries.RECIPE,
                                        ResourceLocation.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, "nether_crystal_fragment_from_scraps")
                                )
                        );

                shaped(RecipeCategory.MISC, EXBlocks.STAMPING_TABLE.asItem())
                        .pattern("pi")
                        .pattern("ww")
                        .pattern("ww")
                        .define('w', ItemTags.PLANKS)
                        .define('p', EXItemTagProvider.STAMPING_CLOTHS)
                        .define('i', Items.IRON_INGOT)
                        .unlockedBy(getHasName(EXBlocks.STAMPING_TABLE.asItem()), has(EXBlocks.STAMPING_TABLE.asItem()))
                        .save(exporter);
            }
        };
    }



    @Override
    public @NotNull String getName() {
        return "EX Recipe Provider";
    }
}
