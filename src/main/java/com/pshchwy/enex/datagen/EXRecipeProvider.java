package com.pshchwy.enex.datagen;

import com.pshchwy.enex.EnchantmentsEX;
import com.pshchwy.enex.block.EXBlocks;
import com.pshchwy.enex.item.EXItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/// This datagen class builds the recipe JSONs for the new items.
public class EXRecipeProvider extends RecipeProvider {

    public EXRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
        super(provider, recipeOutput);
    }

    @Override
    public void buildRecipes() {

        shapeless(RecipeCategory.BREWING, EXItems.NETHER_CRYSTAL_FRAGMENT, 8) // You can also specify an int to produce more than one
                .requires(Items.NETHER_STAR)
                .requires(Items.NETHERITE_INGOT, 2)
                .unlockedBy(RecipeProvider.getHasName(EXItems.NETHER_CRYSTAL_FRAGMENT), has(EXItems.NETHER_CRYSTAL_FRAGMENT))
                .save(this.output, Identifier.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, "nether_crystal_fragment_from_ingots").getPath());

        shapeless(RecipeCategory.BREWING, EXItems.NETHER_CRYSTAL_FRAGMENT, 6) // You can also specify an int to produce more than one
                .requires(Items.NETHER_STAR)
                .requires(Items.NETHERITE_SCRAP, 8)
                .unlockedBy(RecipeProvider.getHasName(EXItems.NETHER_CRYSTAL_FRAGMENT), has(EXItems.NETHER_CRYSTAL_FRAGMENT))
                .save(this.output, Identifier.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, "nether_crystal_fragment_from_scraps").getPath());

        shaped(RecipeCategory.MISC, EXBlocks.STAMPING_TABLE.get(), 1)
                .pattern("pi")
                .pattern("ww")
                .pattern("ww")
                .define('w', ItemTags.PLANKS)
                .define('p', EXItemTagProvider.STAMPING_CLOTHS)
                .define('i', Items.IRON_INGOT)
                .unlockedBy(RecipeProvider.getHasName(EXBlocks.STAMPING_TABLE.get()), has(EXBlocks.STAMPING_TABLE.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, "stamping_table").getPath());


    }

    // The runner to add to the data generator
    public static class Runner extends RecipeProvider.Runner {
        // Get the parameters from the `GatherDataEvent`s.
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
            return new EXRecipeProvider(provider, output);
        }

        @Override
        public String getName() {
            return "";
        }
    }
}
