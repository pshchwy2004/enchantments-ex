package com.pshchwy.enex.datagen;

import com.pshchwy.enex.EnchantmentsEX;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

/// This class declares item tags.
public class EXItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public static final TagKey<Item> STAMPING_CLOTHS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, "stamping_cloths"));

    /**
     * Constructs a new {@link FabricTagProvider} with the default computed path.
     *
     * <p>Common implementations of this class are provided.
     *
     * @param output           the {@link FabricDataOutput} instance
     * @param registriesFuture the backing registry for the tag type
     */
    public EXItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        valueLookupBuilder(STAMPING_CLOTHS)
                .addOptionalTag(ItemTags.WOOL_CARPETS)
                .add(Items.PAPER)
                .add(Items.LEATHER)
                .add(Items.RABBIT_HIDE)
                .setReplace(true);
    }
}
