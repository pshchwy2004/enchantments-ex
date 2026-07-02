package com.pshchwy.enex.datagen;

import com.pshchwy.enex.EnchantmentsEX;
import com.pshchwy.enex.enchantment.EXEnchantmentEffects;
import com.pshchwy.enex.enchantment.effect.KnockbackEXEffect;
import com.pshchwy.enex.enchantment.effect.SharpnessEXEffect;
import com.pshchwy.enex.enchantment.effect.SmiteEXEffect;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class EXEnchantmentGenerator extends FabricDynamicRegistryProvider {
    public EXEnchantmentGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
        System.out.println("registering enchant generator...");
    }
    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        // register Knockback EX
        register(entries, EXEnchantmentEffects.KNOCKBACK_EX, Enchantment.enchantment(
                Enchantment.definition(
                        registries.lookupOrThrow(Registries.ITEM).getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                        // weight of showing up in enchantment table
                        1,
                        // enchantment max level
                        2,
                        // base cost for level 1 of the enchantment, and min levels required for something higher
                        Enchantment.dynamicCost(5, 20),
                        // same fields as above but for max cost
                        Enchantment.dynamicCost(55, 20),
                        // anvil cost
                        5,
                        // valid slots
                        EquipmentSlotGroup.MAINHAND
                )
            ).withEffect(
                EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.ATTACKER,
                EnchantmentTarget.VICTIM,
                new KnockbackEXEffect(LevelBasedValue.perLevel(0.4f, 0.2f))
                ).withEffect(
                        EnchantmentEffectComponents.KNOCKBACK,
                        new AddValue(LevelBasedValue.perLevel(1.0f, 1.0f))
                )
        );
        // register Sharpness EX
        register(entries, EXEnchantmentEffects.SHARPNESS_EX, Enchantment.enchantment(
                        Enchantment.definition(
                                registries.lookupOrThrow(Registries.ITEM).getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                                // weight of showing up in enchantment table
                                1,
                                // enchantment max level
                                5,
                                // base cost for level 1 of the enchantment, and min levels required for something higher
                                Enchantment.dynamicCost(1, 11),
                                // same fields as above but for max cost
                                Enchantment.dynamicCost(21, 11),
                                // anvil cost
                                5,
                                // valid slots
                                EquipmentSlotGroup.MAINHAND
                        )
                ).withEffect(
                        EnchantmentEffectComponents.POST_ATTACK,
                        EnchantmentTarget.ATTACKER,
                        EnchantmentTarget.VICTIM,
                        new SharpnessEXEffect(LevelBasedValue.perLevel(0.4f, 0.2f))
                ).withEffect(
                        EnchantmentEffectComponents.DAMAGE,
                        new AddValue(LevelBasedValue.perLevel(1.0f, 0.5f))
                )
        );
        // register Smite EX
        register(entries, EXEnchantmentEffects.SMITE_EX, Enchantment.enchantment(
                        Enchantment.definition(
                                // which items can be enchanted
                                registries.lookupOrThrow(Registries.ITEM).getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                                // weight of showing up in enchantment table
                                1,
                                // enchantment max level
                                5,
                                // base cost for level 1 of the enchantment, and min levels required for something higher
                                Enchantment.dynamicCost(5, 8),
                                // same fields as above but for max cost
                                Enchantment.dynamicCost(25, 8),
                                // anvil cost
                                5,
                                // valid slots
                                EquipmentSlotGroup.MAINHAND
                        )
                ).withEffect( // lightning effect
                        EnchantmentEffectComponents.POST_ATTACK,
                        EnchantmentTarget.ATTACKER,
                        EnchantmentTarget.VICTIM,
                        new SmiteEXEffect(LevelBasedValue.perLevel(0.4f, 0.2f))
                ).withEffect(
                        EnchantmentEffectComponents.DAMAGE,
                        new AddValue(LevelBasedValue.perLevel(2.5f, 2.5f)),
                LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.THIS,
                        EntityPredicate.Builder.entity()
                                .entityType(
                                        EntityTypePredicate.of(
                                                registries.lookupOrThrow(Registries.ENTITY_TYPE)
                                                        .getOrThrow(EntityTypeTags.SENSITIVE_TO_SMITE).key()
                                        )
                                )
                )
                )
        );
    }

    private void register(Entries entries, ResourceKey<Enchantment> key, Enchantment.Builder builder, ResourceCondition... resourceConditions) {
        entries.add(key, builder.build(key.location()), resourceConditions);
    }

    @Override
    public @NotNull String getName() {
        return "EXEnchantmentGenerator";
    }
}
