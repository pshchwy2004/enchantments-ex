package com.pshchwy.enex.datagen;

import com.pshchwy.enex.EnchantmentsEX;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/// This class declares item tags for NeoForge.
public class EXItemTagProvider extends ItemTagsProvider {

    public static final TagKey<Item> STAMPING_CLOTHS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, "stamping_cloths")
    );

    public EXItemTagProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture
    ) {
        super(output, registriesFuture, EnchantmentsEX.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(STAMPING_CLOTHS)
                .addOptionalTag(ItemTags.WOOL_CARPETS)
                .add(Items.PAPER)
                .add(Items.LEATHER)
                .add(Items.RABBIT_HIDE)
                .replace(true);
    }
}