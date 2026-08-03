package com.pshchwy.enex.datagen;

import com.pshchwy.enex.EnchantmentsEX;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/// This class declares item tags for NeoForge.
public class EXItemTagProvider extends ItemTagsProvider {

    public static final TagKey<Item> STAMPING_CLOTHS = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, "stamping_cloths")
    );

    public EXItemTagProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture,
            @Nullable ExistingFileHelper existingFileHelper
    ) {
        super(output, registriesFuture, CompletableFuture.completedFuture(TagsProvider.TagLookup.empty()), EnchantmentsEX.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(STAMPING_CLOTHS)
                .addOptionalTag(ItemTags.WOOL_CARPETS)
                .add(Items.PAPER.builtInRegistryHolder().key())
                .add(Items.LEATHER.builtInRegistryHolder().key())
                .add(Items.RABBIT_HIDE.builtInRegistryHolder().key())
                .replace(true);
    }
}