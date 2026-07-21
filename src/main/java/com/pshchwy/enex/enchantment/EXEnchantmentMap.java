package com.pshchwy.enex.enchantment;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.HashMap;
import java.util.Map;

/// This class' function is to provide a map mapping the enchantments to their EX variants.
public class EXEnchantmentMap {
    private static final Map<ResourceKey<Enchantment>, ResourceKey<Enchantment>> UPGRADE_MAP = new HashMap<>();

    static {
        // add the enchants here
        registerUpgrade(
                Enchantments.AQUA_AFFINITY,
                EXEnchantmentEffects.AQUA_AFFINITY_EX
        );
        registerUpgrade(
                Enchantments.BANE_OF_ARTHROPODS,
                EXEnchantmentEffects.BANE_OF_ARTHROPODS_EX
        );
        registerUpgrade(
                Enchantments.BREACH,
                EXEnchantmentEffects.BREACH_EX
        );
        registerUpgrade(
                Enchantments.BLAST_PROTECTION,
                EXEnchantmentEffects.BLAST_PROTECTION_EX
        );
        registerUpgrade(
                Enchantments.CHANNELING,
                EXEnchantmentEffects.CHANNELING_EX
        );
        registerUpgrade(
                Enchantments.DENSITY,
                EXEnchantmentEffects.DENSITY_EX
        );
        registerUpgrade(
                Enchantments.DEPTH_STRIDER,
                EXEnchantmentEffects.DEPTH_STRIDER_EX
        );
        registerUpgrade(
                Enchantments.EFFICIENCY,
                EXEnchantmentEffects.EFFICIENCY_EX
        );
        registerUpgrade(
                Enchantments.FEATHER_FALLING,
                EXEnchantmentEffects.FEATHER_FALLING_EX
        );
        registerUpgrade(
                Enchantments.FIRE_ASPECT,
                EXEnchantmentEffects.FIRE_ASPECT_EX
        );
        registerUpgrade(
                Enchantments.FIRE_PROTECTION,
                EXEnchantmentEffects.FIRE_PROTECTION_EX
        );
        registerUpgrade(
                Enchantments.FLAME,
                EXEnchantmentEffects.FLAME_EX
        );
        registerUpgrade(
                Enchantments.FORTUNE,
                EXEnchantmentEffects.FORTUNE_EX
        );
        registerUpgrade(
                Enchantments.FROST_WALKER,
                EXEnchantmentEffects.FROST_WALKER_EX
        );
        registerUpgrade(
                Enchantments.IMPALING,
                EXEnchantmentEffects.IMPALING_EX
        );
        registerUpgrade(
                Enchantments.INFINITY,
                EXEnchantmentEffects.INFINITY_EX
        );
        registerUpgrade(
                Enchantments.KNOCKBACK,
                EXEnchantmentEffects.KNOCKBACK_EX
        );
        registerUpgrade(
                Enchantments.LOOTING,
                EXEnchantmentEffects.LOOTING_EX
        );
        registerUpgrade(
                Enchantments.LOYALTY,
                EXEnchantmentEffects.LOYALTY_EX
        );
        registerUpgrade(
                Enchantments.LUCK_OF_THE_SEA,
                EXEnchantmentEffects.LUCK_OF_THE_SEA_EX
        );
        // Lunge goes here when implemented in further MC versions
        registerUpgrade(
                Enchantments.LURE,
                EXEnchantmentEffects.LURE_EX
        );
        registerUpgrade(
                Enchantments.MENDING,
                EXEnchantmentEffects.MENDING_EX
        );
        registerUpgrade(
                Enchantments.MULTISHOT,
                EXEnchantmentEffects.MULTISHOT_EX
        );
        registerUpgrade(
                Enchantments.PIERCING,
                EXEnchantmentEffects.PIERCING_EX
        );
        registerUpgrade(
                Enchantments.POWER,
                EXEnchantmentEffects.POWER_EX
        );
        registerUpgrade(
                Enchantments.PUNCH,
                EXEnchantmentEffects.PUNCH_EX
        );
        registerUpgrade(
                Enchantments.PROJECTILE_PROTECTION,
                EXEnchantmentEffects.PROJECTILE_PROTECTION_EX
        );
        registerUpgrade(
                Enchantments.PROTECTION,
                EXEnchantmentEffects.PROTECTION_EX
        );
        registerUpgrade(
                Enchantments.QUICK_CHARGE,
                EXEnchantmentEffects.QUICK_CHARGE_EX
        );
        registerUpgrade(
                Enchantments.RESPIRATION,
                EXEnchantmentEffects.RESPIRATION_EX
        );
        registerUpgrade(
                Enchantments.RIPTIDE,
                EXEnchantmentEffects.RIPTIDE_EX
        );
        registerUpgrade(
                Enchantments.SHARPNESS,
                EXEnchantmentEffects.SHARPNESS_EX
        );
        registerUpgrade(
                Enchantments.SMITE,
                EXEnchantmentEffects.SMITE_EX
        );
        registerUpgrade(
                Enchantments.SOUL_SPEED,
                EXEnchantmentEffects.SOUL_SPEED_EX
        );
        registerUpgrade(
                Enchantments.SWEEPING_EDGE,
                EXEnchantmentEffects.SWEEPING_EDGE_EX
        );
        registerUpgrade(
                Enchantments.SWIFT_SNEAK,
                EXEnchantmentEffects.SWIFT_SNEAK_EX
        );
        registerUpgrade(
                Enchantments.THORNS,
                EXEnchantmentEffects.THORNS_EX
        );
        registerUpgrade(
                Enchantments.UNBREAKING,
                EXEnchantmentEffects.UNBREAKING_EX
        );
        registerUpgrade(
                Enchantments.WIND_BURST,
                EXEnchantmentEffects.WIND_BURST_EX
        );
    }

    private static void registerUpgrade(ResourceKey<Enchantment> original, ResourceKey<Enchantment> upgrade) {
        UPGRADE_MAP.put(original, upgrade);
    }

    public static boolean isUpgradable(ResourceKey<Enchantment> key) {
        return UPGRADE_MAP.containsKey(key);
    }

    public static ResourceKey<Enchantment> getUpgrade(ResourceKey<Enchantment> key) {
        return UPGRADE_MAP.get(key);
    }
}
