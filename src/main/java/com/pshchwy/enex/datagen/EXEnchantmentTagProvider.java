package com.pshchwy.enex.datagen;

import com.pshchwy.enex.EnchantmentsEX;
import com.pshchwy.enex.enchantment.EXEnchantmentEffects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.concurrent.CompletableFuture;

/// EX enchantment Tag provider in order to prevent EX enchantments and original enchantments from coexisting in a weapon (although it could hypothetically still be possible with commands)
public class EXEnchantmentTagProvider extends FabricTagProvider<Enchantment> {
    /**
     * Constructs a new {@link FabricTagProvider} with the default computed path.
     *
     */
    public static final TagKey<Enchantment> AQUA_AFFINITY_EXCLUSIVE = create("aqua_affinity_exclusive");
    public static final TagKey<Enchantment> BANE_OF_ARTHROPODS_EXCLUSIVE = create("bane_of_arthropods_exclusive");
    public static final TagKey<Enchantment> BLAST_PROTECTION_EXCLUSIVE = create("blast_protection_exclusive");
    public static final TagKey<Enchantment> BREACH_EXCLUSIVE = create("breach_exclusive");
    public static final TagKey<Enchantment> CHANNELING_EXCLUSIVE = create("channeling_exclusive");
    public static final TagKey<Enchantment> DENSITY_EXCLUSIVE = create("density_exclusive");
    public static final TagKey<Enchantment> DEPTH_STRIDER_EXCLUSIVE = create("depth_strider_exclusive");
    public static final TagKey<Enchantment> EFFICIENCY_EXCLUSIVE = create("efficiency_exclusive");
    public static final TagKey<Enchantment> FEATHER_FALLING_EXCLUSIVE = create("feather_falling_exclusive");
    public static final TagKey<Enchantment> FIRE_ASPECT_EXCLUSIVE = create("fire_aspect_exclusive");
    public static final TagKey<Enchantment> FIRE_PROTECTION_EXCLUSIVE = create("fire_protection_exclusive");
    public static final TagKey<Enchantment> FLAME_EXCLUSIVE = create("flame_exclusive");
    public static final TagKey<Enchantment> FORTUNE_EXCLUSIVE = create("fortune_exclusive");
    public static final TagKey<Enchantment> FROST_WALKER_EXCLUSIVE = create("frost_walker_exclusive");
    public static final TagKey<Enchantment> IMPALING_EXCLUSIVE = create("impaling_exclusive");
    public static final TagKey<Enchantment> INFINITY_EXCLUSIVE = create("infinity_exclusive");
    public static final TagKey<Enchantment> KNOCKBACK_EXCLUSIVE = create("knockback_exclusive");
    public static final TagKey<Enchantment> LOOTING_EXCLUSIVE = create("looting_exclusive");
    public static final TagKey<Enchantment> LOYALTY_EXCLUSIVE = create("loyalty_exclusive");
    public static final TagKey<Enchantment> LUCK_OF_THE_SEA_EXCLUSIVE = create("luck_of_the_sea_exclusive");
    // public static final TagKey<Enchantment> LUNGE_EXCLUSIVE = create("lunge_exclusive");
    public static final TagKey<Enchantment> LURE_EXCLUSIVE = create("lure_exclusive");
    public static final TagKey<Enchantment> MENDING_EXCLUSIVE = create("mending_exclusive");
    public static final TagKey<Enchantment> MULTISHOT_EXCLUSIVE = create("multishot_exclusive");
    public static final TagKey<Enchantment> PIERCING_EXCLUSIVE = create("piercing_exclusive");
    public static final TagKey<Enchantment> POWER_EXCLUSIVE = create("power_exclusive");
    public static final TagKey<Enchantment> PROJECTILE_PROTECTION_EXCLUSIVE = create("projectile_protection_exclusive");
    public static final TagKey<Enchantment> PROTECTION_EXCLUSIVE = create("protection_exclusive");
    public static final TagKey<Enchantment> PUNCH_EXCLUSIVE = create("punch_exclusive");
    public static final TagKey<Enchantment> QUICK_CHARGE_EXCLUSIVE = create("quick_charge_exclusive");
    public static final TagKey<Enchantment> RESPIRATION_EXCLUSIVE = create("respiration_exclusive");
    public static final TagKey<Enchantment> RIPTIDE_EXCLUSIVES = create("riptide_exclusives");
    public static final TagKey<Enchantment> SHARPNESS_EXCLUSIVE = create("sharpness_exclusive");
    public static final TagKey<Enchantment> SMITE_EXCLUSIVE = create("smite_exclusive");
    public static final TagKey<Enchantment> SOUL_SPEED_EXCLUSIVE = create("soul_speed_exclusive");
    public static final TagKey<Enchantment> SWEEPING_EDGE_EXCLUSIVE = create("sweeping_edge_exclusive");
    public static final TagKey<Enchantment> SWIFT_SNEAK_EXCLUSIVE = create("swift_sneak_exclusive");
    public static final TagKey<Enchantment> THORNS_EXCLUSIVE = create("thorns_exclusive");
    public static final TagKey<Enchantment> UNBREAKING_EXCLUSIVE = create("unbreaking_exclusive");
    public static final TagKey<Enchantment> WIND_BURST_EXCLUSIVE = create("wind_burst_exclusive");
    public EXEnchantmentTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.ENCHANTMENT, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        builder(AQUA_AFFINITY_EXCLUSIVE)
                .add(Enchantments.AQUA_AFFINITY)
                .addOptional(EXEnchantmentEffects.AQUA_AFFINITY_EX)
                .setReplace(true);
        builder(BANE_OF_ARTHROPODS_EXCLUSIVE)
                .add(Enchantments.BANE_OF_ARTHROPODS)
                .addOptional(EXEnchantmentEffects.BANE_OF_ARTHROPODS_EX)
                .addOptionalTag(EnchantmentTags.DAMAGE_EXCLUSIVE)
                .setReplace(true);
        builder(BLAST_PROTECTION_EXCLUSIVE)
                .add(Enchantments.BLAST_PROTECTION)
                .addOptional(EXEnchantmentEffects.BLAST_PROTECTION_EX)
                .addOptionalTag(EnchantmentTags.ARMOR_EXCLUSIVE);

        builder(BREACH_EXCLUSIVE)
                .add(Enchantments.BREACH)
                .addOptional(EXEnchantmentEffects.BREACH_EX)
                .addOptionalTag(EnchantmentTags.DAMAGE_EXCLUSIVE);

        builder(CHANNELING_EXCLUSIVE)
                .add(Enchantments.CHANNELING)
                .addOptional(EXEnchantmentEffects.CHANNELING_EX);

        builder(DENSITY_EXCLUSIVE)
                .add(Enchantments.DENSITY)
                .addOptional(EXEnchantmentEffects.DENSITY_EX)
                .addOptionalTag(EnchantmentTags.DAMAGE_EXCLUSIVE);

        builder(DEPTH_STRIDER_EXCLUSIVE)
                .add(Enchantments.DEPTH_STRIDER)
                .addOptional(EXEnchantmentEffects.DEPTH_STRIDER_EX)
                .addOptionalTag(EnchantmentTags.BOOTS_EXCLUSIVE);

        builder(EFFICIENCY_EXCLUSIVE)
                .add(Enchantments.EFFICIENCY)
                .addOptional(EXEnchantmentEffects.EFFICIENCY_EX);

        builder(FEATHER_FALLING_EXCLUSIVE)
                .add(Enchantments.FEATHER_FALLING)
                .addOptional(EXEnchantmentEffects.FEATHER_FALLING_EX);

        builder(FIRE_ASPECT_EXCLUSIVE)
                .add(Enchantments.FIRE_ASPECT)
                .addOptional(EXEnchantmentEffects.FIRE_ASPECT_EX);

        builder(FIRE_PROTECTION_EXCLUSIVE)
                .add(Enchantments.FIRE_PROTECTION)
                .addOptional(EXEnchantmentEffects.FIRE_PROTECTION_EX)
                .addOptionalTag(EnchantmentTags.ARMOR_EXCLUSIVE);

        builder(FLAME_EXCLUSIVE)
                .add(Enchantments.FLAME)
                .addOptional(EXEnchantmentEffects.FLAME_EX);

        builder(FORTUNE_EXCLUSIVE)
                .add(Enchantments.FORTUNE)
                .addOptional(EXEnchantmentEffects.FORTUNE_EX)
                .addOptionalTag(EnchantmentTags.MINING_EXCLUSIVE);

        builder(FROST_WALKER_EXCLUSIVE)
                .add(Enchantments.FROST_WALKER)
                .addOptional(EXEnchantmentEffects.FROST_WALKER_EX)
                .addOptionalTag(EnchantmentTags.BOOTS_EXCLUSIVE);

        builder(IMPALING_EXCLUSIVE)
                .add(Enchantments.IMPALING)
                .addOptional(EXEnchantmentEffects.IMPALING_EX)
                .addOptionalTag(EnchantmentTags.DAMAGE_EXCLUSIVE);

        builder(INFINITY_EXCLUSIVE)
                .add(Enchantments.INFINITY)
                .addOptional(EXEnchantmentEffects.INFINITY_EX)
                .addOptionalTag(EnchantmentTags.BOW_EXCLUSIVE);

        builder(KNOCKBACK_EXCLUSIVE)
                .add(Enchantments.KNOCKBACK)
                .addOptional(EXEnchantmentEffects.KNOCKBACK_EX);

        builder(LOOTING_EXCLUSIVE)
                .add(Enchantments.LOOTING)
                .addOptional(EXEnchantmentEffects.LOOTING_EX);

        builder(LOYALTY_EXCLUSIVE)
                .add(Enchantments.LOYALTY)
                .addOptional(EXEnchantmentEffects.LOYALTY_EX);

        builder(LUCK_OF_THE_SEA_EXCLUSIVE)
                .add(Enchantments.LUCK_OF_THE_SEA)
                .addOptional(EXEnchantmentEffects.LUCK_OF_THE_SEA_EX);

        builder(LURE_EXCLUSIVE)
                .add(Enchantments.LURE)
                .addOptional(EXEnchantmentEffects.LURE_EX);

        builder(MENDING_EXCLUSIVE)
                .add(Enchantments.MENDING)
                .addOptional(EXEnchantmentEffects.MENDING_EX);

        builder(MULTISHOT_EXCLUSIVE)
                .add(Enchantments.MULTISHOT)
                .addOptional(EXEnchantmentEffects.MULTISHOT_EX);

        builder(PIERCING_EXCLUSIVE)
                .add(Enchantments.PIERCING)
                .addOptional(EXEnchantmentEffects.PIERCING_EX);

        builder(POWER_EXCLUSIVE)
                .add(Enchantments.POWER)
                .addOptional(EXEnchantmentEffects.POWER_EX);

        builder(PROJECTILE_PROTECTION_EXCLUSIVE)
                .add(Enchantments.PROJECTILE_PROTECTION)
                .addOptional(EXEnchantmentEffects.PROJECTILE_PROTECTION_EX)
                .addOptionalTag(EnchantmentTags.ARMOR_EXCLUSIVE);

        builder(PROTECTION_EXCLUSIVE)
                .add(Enchantments.PROTECTION)
                .addOptional(EXEnchantmentEffects.PROTECTION_EX)
                .addOptionalTag(EnchantmentTags.ARMOR_EXCLUSIVE);

        builder(PUNCH_EXCLUSIVE)
                .add(Enchantments.PUNCH)
                .addOptional(EXEnchantmentEffects.PUNCH_EX);

        builder(QUICK_CHARGE_EXCLUSIVE)
                .add(Enchantments.QUICK_CHARGE)
                .addOptional(EXEnchantmentEffects.QUICK_CHARGE_EX);

        builder(RESPIRATION_EXCLUSIVE)
                .add(Enchantments.RESPIRATION)
                .addOptional(EXEnchantmentEffects.RESPIRATION_EX);

        builder(RIPTIDE_EXCLUSIVES)
                .add(Enchantments.RIPTIDE)
                .addOptional(EXEnchantmentEffects.RIPTIDE_EX)
                .addOptionalTag(EnchantmentTags.RIPTIDE_EXCLUSIVE);

        builder(SHARPNESS_EXCLUSIVE)
                .add(Enchantments.SHARPNESS)
                .addOptional(EXEnchantmentEffects.SHARPNESS_EX)
                .addOptionalTag(EnchantmentTags.DAMAGE_EXCLUSIVE);

        builder(SMITE_EXCLUSIVE)
                .add(Enchantments.SMITE)
                .addOptional(EXEnchantmentEffects.SMITE_EX)
                .addOptionalTag(EnchantmentTags.DAMAGE_EXCLUSIVE);

        builder(SOUL_SPEED_EXCLUSIVE)
                .add(Enchantments.SOUL_SPEED)
                .addOptional(EXEnchantmentEffects.SOUL_SPEED_EX);

        builder(SWEEPING_EDGE_EXCLUSIVE)
                .add(Enchantments.SWEEPING_EDGE)
                .addOptional(EXEnchantmentEffects.SWEEPING_EDGE_EX);

        builder(SWIFT_SNEAK_EXCLUSIVE)
                .add(Enchantments.SWIFT_SNEAK)
                .addOptional(EXEnchantmentEffects.SWIFT_SNEAK_EX);

        builder(THORNS_EXCLUSIVE)
                .add(Enchantments.THORNS)
                .addOptional(EXEnchantmentEffects.THORNS_EX);

        builder(UNBREAKING_EXCLUSIVE)
                .add(Enchantments.UNBREAKING)
                .addOptional(EXEnchantmentEffects.UNBREAKING_EX);

        builder(WIND_BURST_EXCLUSIVE)
                .add(Enchantments.WIND_BURST)
                .addOptional(EXEnchantmentEffects.WIND_BURST_EX);
    }

    private static TagKey<Enchantment> create(String string) {
        return TagKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, "exclusive_set/" + string));
    }
}
