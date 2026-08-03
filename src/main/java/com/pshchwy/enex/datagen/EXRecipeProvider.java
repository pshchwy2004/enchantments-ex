package com.pshchwy.enex.datagen;

import com.pshchwy.enex.EnchantmentsEX;
import com.pshchwy.enex.block.EXBlocks;
import com.pshchwy.enex.item.EXItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/// This datagen class builds the recipe JSONs for the new items.
public class EXRecipeProvider extends RecipeProvider {

    public EXRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(@NotNull RecipeOutput exporter) {

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BREWING, EXItems.NETHER_CRYSTAL_FRAGMENT, 8) // You can also specify an int to produce more than one
                .requires(Items.NETHER_STAR)
                .requires(Items.NETHERITE_INGOT, 2)
                .unlockedBy(RecipeProvider.getHasName(EXItems.NETHER_CRYSTAL_FRAGMENT), RecipeProvider.has(EXItems.NETHER_CRYSTAL_FRAGMENT))
                .save(exporter, ResourceLocation.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, "nether_crystal_fragment_from_ingots"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BREWING, EXItems.NETHER_CRYSTAL_FRAGMENT, 6) // You can also specify an int to produce more than one
                .requires(Items.NETHER_STAR)
                .requires(Items.NETHERITE_SCRAP, 8)
                .unlockedBy(RecipeProvider.getHasName(EXItems.NETHER_CRYSTAL_FRAGMENT), RecipeProvider.has(EXItems.NETHER_CRYSTAL_FRAGMENT))
                .save(exporter, ResourceLocation.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, "nether_crystal_fragment_from_scraps"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EXBlocks.STAMPING_TABLE.get(), 1)
                .pattern("pi")
                .pattern("ww")
                .pattern("ww")
                .define('w', ItemTags.PLANKS)
                .define('p', EXItemTagProvider.STAMPING_CLOTHS)
                .define('i', Items.IRON_INGOT)
                .unlockedBy(RecipeProvider.getHasName(EXBlocks.STAMPING_TABLE.get()), RecipeProvider.has(EXBlocks.STAMPING_TABLE.get()))
                .save(exporter, ResourceLocation.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, "stamping_table"));


    }
}
