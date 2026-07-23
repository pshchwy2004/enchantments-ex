package com.pshchwy.enex.datagen;

import com.pshchwy.enex.EnchantmentsEX;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

/// This class declares mob tags.
public class EXMobTagProvider extends FabricTagProvider.EntityTypeTagProvider {

    public static final TagKey<EntityType<?>> BREACH_EX_VULNERABLE = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, "boss_mobs"));
    public static final TagKey<EntityType<?>> FIRE_IMMUNE = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, "immune_to_fire"));
    public static final TagKey<EntityType<?>> FLYING_MOBS = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, "flying_mobs"));

    /**
     * Constructs a new {@link FabricTagProvider} with the default computed path.
     *
     * <p>Common implementations of this class are provided.
     *
     * @param output           the {@link FabricDataOutput} instance
     * @param registriesFuture the backing registry for the tag type
     */
    public EXMobTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        valueLookupBuilder(BREACH_EX_VULNERABLE)
                .add(EntityType.ENDER_DRAGON)
                .add(EntityType.IRON_GOLEM)
                .add(EntityType.WARDEN)
                .add(EntityType.RAVAGER)
                .add(EntityType.WITHER)
                .setReplace(true);

        valueLookupBuilder(FIRE_IMMUNE)
                .add(EntityType.ENDER_DRAGON)
                .add(EntityType.ZOGLIN)
                .add(EntityType.VEX)
                .add(EntityType.WARDEN)
                .add(EntityType.ENDER_DRAGON)
                .add(EntityType.WITHER)
                .add(EntityType.BLAZE)
                .add(EntityType.WITHER_SKELETON)
                .add(EntityType.GHAST)
                .add(EntityType.MAGMA_CUBE)
                .add(EntityType.ZOMBIFIED_PIGLIN)
                .add(EntityType.STRIDER)
                .setReplace(true);

        valueLookupBuilder(FLYING_MOBS)
                .add(EntityType.ENDER_DRAGON)
                .add(EntityType.GHAST)
                .add(EntityType.VEX)
                .add(EntityType.ALLAY)
                .add(EntityType.BLAZE)
                .add(EntityType.WITHER)
                .add(EntityType.BAT)
                .add(EntityType.PARROT)
                .add(EntityType.BEE)
                .add(EntityType.PHANTOM)
                .add(EntityType.HAPPY_GHAST)
                .setReplace(true);
    }
}
