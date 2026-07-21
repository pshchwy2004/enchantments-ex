package com.pshchwy.enex.datagen;

import com.pshchwy.enex.EnchantmentsEX;
import com.pshchwy.enex.block.EXBlocks;
import com.pshchwy.enex.item.EXItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

/// This datagen class builds the recipe JSONs for the new items.
public class EXRecipeProvider extends FabricRecipeProvider {

    public EXRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BREWING, EXItems.NETHER_CRYSTAL_FRAGMENT, 8) // You can also specify an int to produce more than one
                .requires(Items.NETHER_STAR)
                .requires(Items.NETHERITE_INGOT, 2)
                .unlockedBy(FabricRecipeProvider.getHasName(EXItems.NETHER_CRYSTAL_FRAGMENT), FabricRecipeProvider.has(EXItems.NETHER_CRYSTAL_FRAGMENT))
                .save(exporter, ResourceLocation.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, "nether_crystal_fragment_from_ingots"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BREWING, EXItems.NETHER_CRYSTAL_FRAGMENT, 6) // You can also specify an int to produce more than one
                .requires(Items.NETHER_STAR)
                .requires(Items.NETHERITE_SCRAP, 8)
                .unlockedBy(FabricRecipeProvider.getHasName(EXItems.NETHER_CRYSTAL_FRAGMENT), FabricRecipeProvider.has(EXItems.NETHER_CRYSTAL_FRAGMENT))
                .save(exporter, ResourceLocation.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, "nether_crystal_fragment_from_scraps"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EXBlocks.STAMPING_TABLE.asItem())
                .pattern("pi")
                .pattern("ww")
                .pattern("ww")
                .define('w', ItemTags.PLANKS)
                .define('p', EXItemTagProvider.STAMPING_CLOTHS)
                .define('i', Items.IRON_INGOT)
                .unlockedBy(FabricRecipeProvider.getHasName(EXBlocks.STAMPING_TABLE.asItem()), FabricRecipeProvider.has(EXBlocks.STAMPING_TABLE.asItem()))
                .save(exporter);
    }
}
