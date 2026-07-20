package com.pshchwy.enex.enchantment;

import com.mojang.serialization.MapCodec;
import com.pshchwy.enex.EnchantmentsEX;
import com.pshchwy.enex.enchantment.effect.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;

public class EXEnchantmentEffects {

    public static final ResourceKey<Enchantment> AQUA_AFFINITY_EX = of("aqua_affinity_ex");
    @SuppressWarnings("unused")
    public static MapCodec<AquaAffinityEXEffect> AQUA_AFFINITY_EX_EFFECT = register("aqua_affinity_ex_effect", AquaAffinityEXEffect.CODEC);

    public static final ResourceKey<Enchantment> BANE_OF_ARTHROPODS_EX = of("bane_of_arthropods_ex");
    @SuppressWarnings("unused")
    public static MapCodec<BaneOfArthropodsEXEffect> BANE_OF_ARTHROPODS_EX_EFFECT = register("bane_of_arthropods_ex_effect", BaneOfArthropodsEXEffect.CODEC);

    public static final ResourceKey<Enchantment> BLAST_PROTECTION_EX = of("blast_protection_ex");
    @SuppressWarnings("unused")
    public static MapCodec<BlastProtectionEXEffect> BLAST_PROTECTION_EX_EFFECT = register("blast_protection_ex_effect", BlastProtectionEXEffect.CODEC);

    public static final ResourceKey<Enchantment> BREACH_EX = of("breach_ex");

    public static final ResourceKey<Enchantment> CHANNELING_EX = of("channeling_ex");

    public static final ResourceKey<Enchantment> DENSITY_EX = of("density_ex");
    @SuppressWarnings("unused")
    public static MapCodec<DensityEXEffect> DENSITY_EX_EFFECT = register("density_ex_effect", DensityEXEffect.CODEC);

    public static final ResourceKey<Enchantment> DEPTH_STRIDER_EX = of("depth_strider_ex");
    @SuppressWarnings("unused")
    public static MapCodec<DepthStriderEXEffect> DEPTH_STRIDER_EX_EFFECT = register("depth_strider_ex_effect", DepthStriderEXEffect.CODEC);

    public static final ResourceKey<Enchantment> EFFICIENCY_EX = of("efficiency_ex");

    public static final ResourceKey<Enchantment> FEATHER_FALLING_EX = of("feather_falling_ex");

    public static final ResourceKey<Enchantment> FIRE_ASPECT_EX = of("fire_aspect_ex");

    public static final ResourceKey<Enchantment> FIRE_PROTECTION_EX = of("fire_protection_ex");

    public static final ResourceKey<Enchantment> FLAME_EX = of("flame_ex");

    public static final ResourceKey<Enchantment> FORTUNE_EX = of("fortune_ex");

    public static final ResourceKey<Enchantment> FROST_WALKER_EX = of("frost_walker_ex");

    public static final ResourceKey<Enchantment> IMPALING_EX = of("impaling_ex");
    @SuppressWarnings("unused")
    public static MapCodec<BreathStealEffect> BREATH_STEAL_EFFECT = register("breath_steal_effect", BreathStealEffect.CODEC);

    public static final ResourceKey<Enchantment> INFINITY_EX = of("infinity_ex");

    public static final ResourceKey<Enchantment> KNOCKBACK_EX = of("knockback_ex");
    @SuppressWarnings("unused")
    public static MapCodec<KnockbackEXEffect> KNOCKBACK_EX_EFFECT = register("knockback_ex_effect", KnockbackEXEffect.CODEC);

    public static final ResourceKey<Enchantment> LOOTING_EX = of("looting_ex");

    public static final ResourceKey<Enchantment> LOYALTY_EX = of("loyalty_ex");
    @SuppressWarnings("unused")
    public static MapCodec<TridentItemRetrievalEffect> ITEM_RETRIEVAL_EFFECT = register("trident_item_retrieval_effect", TridentItemRetrievalEffect.CODEC);

    public static final ResourceKey<Enchantment> LUCK_OF_THE_SEA_EX = of("luck_of_the_sea_ex");

    @SuppressWarnings("unused")
    public static final ResourceKey<Enchantment> LUNGE_EX = of("lunge_ex");

    public static final ResourceKey<Enchantment> LURE_EX = of("lure_ex");
    @SuppressWarnings("unused")
    public static MapCodec<LureEXEffect> LURE_EFFECT = register("lure_effect", LureEXEffect.CODEC);

    public static final ResourceKey<Enchantment> MENDING_EX = of("mending_ex");

    public static final ResourceKey<Enchantment> MULTISHOT_EX = of("multishot_ex");

    public static final ResourceKey<Enchantment> PIERCING_EX = of("piercing_ex");

    public static final ResourceKey<Enchantment> POWER_EX = of("power_ex");

    public static final ResourceKey<Enchantment> PROJECTILE_PROTECTION_EX = of("projectile_protection_ex");

    public static final ResourceKey<Enchantment> PROTECTION_EX = of("protection_ex");

    public static final ResourceKey<Enchantment> PUNCH_EX = of("punch_ex");

    public static final ResourceKey<Enchantment> QUICK_CHARGE_EX = of("quick_charge_ex");

    public static final ResourceKey<Enchantment> RESPIRATION_EX = of("respiration_ex");
    @SuppressWarnings("unused")
    public static MapCodec<RespirationEXEffect> RESPIRATION_EX_EFFECT = register("respiration_ex_effect", RespirationEXEffect.CODEC);

    public static final ResourceKey<Enchantment> RIPTIDE_EX = of("riptide_ex");

    public static final ResourceKey<Enchantment> SHARPNESS_EX = of("sharpness_ex");

    @SuppressWarnings("unused")
    public static final ResourceKey<Enchantment> SILK_TOUCH_EX = of("silk_touch_ex");

    public static final ResourceKey<Enchantment> SMITE_EX = of("smite_ex");
    @SuppressWarnings("unused")
    public static MapCodec<SmiteEXEffect> SMITE_EX_EFFECT = register("smite_ex_effect", SmiteEXEffect.CODEC);

    public static final ResourceKey<Enchantment> SOUL_SPEED_EX = of("soul_speed_ex");

    public static final ResourceKey<Enchantment> SWEEPING_EDGE_EX = of("sweeping_edge_ex");

    public static final ResourceKey<Enchantment> SWIFT_SNEAK_EX = of("swift_sneak_ex");

    public static final ResourceKey<Enchantment> THORNS_EX = of("thorns_ex");

    public static final ResourceKey<Enchantment> UNBREAKING_EX = of("unbreaking_ex");

    public static final ResourceKey<Enchantment> WIND_BURST_EX = of("wind_burst_ex");

    private static ResourceKey<Enchantment> of(String path) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, path);
        return ResourceKey.create(Registries.ENCHANTMENT, id);
    }

    private static <T extends EnchantmentEntityEffect> MapCodec<T> register(String id, MapCodec<T> codec) {
        return Registry.register(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE, ResourceLocation.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, id), codec);
    }

    public static void registerEnchantmentEffects() {
        EnchantmentsEX.LOGGER.info("Registering enchantment effects for " + EnchantmentsEX.MOD_ID);
    }
}
