package com.pshchwy.enex.datagen;

import com.pshchwy.enex.EnchantmentsEX;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/// This class declares mob tags.
public class EXMobTagProvider extends EntityTypeTagsProvider {

    public static final TagKey<EntityType<?>> BREACH_EX_VULNERABLE = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, "boss_mobs"));
    public static final TagKey<EntityType<?>> FIRE_IMMUNE = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, "immune_to_fire"));
    public static final TagKey<EntityType<?>> FLYING_MOBS = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, "flying_mobs"));

    /**
     * Constructs a new {@link EntityTypeTagsProvider} with the default computed path.
     *
     * <p>Common implementations of this class are provided.
     *
     * @param output           the {@link PackOutput} instance
     * @param registriesFuture the backing registry for the tag type
     */
    public EXMobTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture, EnchantmentsEX.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider wrapperLookup) {
        this.tag(BREACH_EX_VULNERABLE)
                .add(getRK(EntityTypes.ENDER_DRAGON))
                .add(getRK(EntityTypes.IRON_GOLEM))
                .add(getRK(EntityTypes.WARDEN))
                .add(getRK(EntityTypes.RAVAGER))
                .add(getRK(EntityTypes.WITHER))
                .replace(true);

        this.tag(FIRE_IMMUNE)
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
                .replace(true);

        this.tag(FLYING_MOBS)
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
                .replace(true);
    }

    private static ResourceKey<EntityType<?>> getRK (EntityType<?> entityType) {
        return BuiltInRegistries.ENTITY_TYPE.getResourceKey(entityType).get();
    }
}
