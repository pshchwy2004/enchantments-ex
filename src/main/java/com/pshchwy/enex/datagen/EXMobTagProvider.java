package com.pshchwy.enex.datagen;

import com.pshchwy.enex.EnchantmentsEX;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;

import java.util.concurrent.CompletableFuture;

/// This class declares mob tags.
public class EXMobTagProvider extends FabricTagsProvider.EntityTypeTagsProvider {

    public static final TagKey<EntityType<?>> BREACH_EX_VULNERABLE = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, "boss_mobs"));
    public static final TagKey<EntityType<?>> FIRE_IMMUNE = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, "immune_to_fire"));
    public static final TagKey<EntityType<?>> FLYING_MOBS = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, "flying_mobs"));

    /**
     * Constructs a new {@link FabricTagsProvider} with the default computed path.
     *
     * <p>Common implementations of this class are provided.
     *
     * @param output           the {@link FabricPackOutput} instance
     * @param registriesFuture the backing registry for the tag type
     */
    public EXMobTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        tag(BREACH_EX_VULNERABLE)
                .add(getRK(EntityTypes.ENDER_DRAGON))
                .add(getRK(EntityTypes.IRON_GOLEM))
                .add(getRK(EntityTypes.WARDEN))
                .add(getRK(EntityTypes.RAVAGER))
                .add(getRK(EntityTypes.WITHER))
                .setReplace(true);

        tag(FIRE_IMMUNE)
                .add(getRK(EntityTypes.ENDER_DRAGON))
                .add(getRK(EntityTypes.ZOGLIN))
                .add(getRK(EntityTypes.VEX))
                .add(getRK(EntityTypes.WARDEN))
                .add(getRK(EntityTypes.ENDER_DRAGON))
                .add(getRK(EntityTypes.WITHER))
                .add(getRK(EntityTypes.BLAZE))
                .add(getRK(EntityTypes.WITHER_SKELETON))
                .add(getRK(EntityTypes.GHAST))
                .add(getRK(EntityTypes.MAGMA_CUBE))
                .add(getRK(EntityTypes.ZOMBIFIED_PIGLIN))
                .add(getRK(EntityTypes.STRIDER))
                .setReplace(true);

        tag(FLYING_MOBS)
                .add(getRK(EntityTypes.ENDER_DRAGON))
                .add(getRK(EntityTypes.GHAST))
                .add(getRK(EntityTypes.VEX))
                .add(getRK(EntityTypes.ALLAY))
                .add(getRK(EntityTypes.BLAZE))
                .add(getRK(EntityTypes.WITHER))
                .add(getRK(EntityTypes.BAT))
                .add(getRK(EntityTypes.PARROT))
                .add(getRK(EntityTypes.BEE))
                .add(getRK(EntityTypes.PHANTOM))
                .add(getRK(EntityTypes.HAPPY_GHAST))
                .setReplace(true);
    }

    private static ResourceKey<EntityType<?>> getRK (EntityType<?> entityType) {
        return BuiltInRegistries.ENTITY_TYPE.getResourceKey(entityType).get();
    }
}
