package com.pshchwy.enex.util;

import com.pshchwy.enex.enchantment.EXEnchantmentEffects;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.intellij.lang.annotations.Identifier;

import java.util.List;

public class EXLootTableModifiers {
    private static final ResourceLocation DIAMOND_ORE_ID = ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/diamond_ore");
    private static final ResourceLocation DEEPSLATE_DIAMOND_ORE_ID = ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/deepslate_diamond_ore");
    private static final ResourceLocation COAL_ORE_ID = ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/coal_ore");
    private static final ResourceLocation DEEPSLATE_COAL_ORE_ID = ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/deepslate_coal_ore");
    private static final ResourceLocation COPPER_ORE_ID = ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/copper_ore");
    private static final ResourceLocation DEEPSLATE_COPPER_ORE_ID = ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/deepslate_copper_ore");
    private static final ResourceLocation IRON_ORE_ID = ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/iron_ore");
    private static final ResourceLocation DEEPSLATE_IRON_ORE_ID = ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/deepslate_iron_ore");
    private static final ResourceLocation GOLD_ORE_ID = ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/gold_ore");
    private static final ResourceLocation DEEPSLATE_GOLD_ORE_ID = ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/deepslate_gold_ore");
    private static final ResourceLocation EMERALD_ORE_ID = ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/emerald_ore");
    private static final ResourceLocation DEEPSLATE_EMERALD_ORE_ID = ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/deepslate_emerald_ore");
    private static final ResourceLocation LAPIS_ORE_ID = ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/lapis_ore");
    private static final ResourceLocation DEEPSLATE_LAPIS_ORE_ID = ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/deepslate_lapis_ore");
    private static final ResourceLocation REDSTONE_ORE_ID = ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/redstone_ore");
    private static final ResourceLocation DEEPSLATE_REDSTONE_ORE_ID = ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/deepslate_redstone_ore");
    private static final ResourceLocation NETHER_GOLD_ORE_ID = ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/nether_gold_ore");
    private static final ResourceLocation ANCIENT_DEBRIS_ID = ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/ancient_debris");
    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            HolderLookup.RegistryLookup<Enchantment> registryLookup = registries.lookupOrThrow(Registries.ENCHANTMENT);
            // Let's only modify built-in loot tables and leave data pack loot tables untouched by checking the source.
            // We also check that the loot table ID is equal to the ID we want.
            if (source.isBuiltin() && (key.location().equals(DIAMOND_ORE_ID) || key.location().equals(DEEPSLATE_DIAMOND_ORE_ID))) {
                // We make the pool and add an item
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.DIAMOND_BLOCK))
                        .when(
                                MatchTool.toolMatches(
                                        ItemPredicate.Builder.item()
                                                .withSubPredicate(
                                                        ItemSubPredicates.ENCHANTMENTS,
                                                        ItemEnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX), MinMaxBounds.Ints.atLeast(1))))
                                                )
                                )
                        )
                        .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
                tableBuilder.withPool(poolBuilder);
            }
            else if (source.isBuiltin() && (key.location().equals(COAL_ORE_ID) || key.location().equals(DEEPSLATE_COAL_ORE_ID))) {
                // We make the pool and add an item
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.COAL_BLOCK))
                        .when(
                                MatchTool.toolMatches(
                                        ItemPredicate.Builder.item()
                                                .withSubPredicate(
                                                        ItemSubPredicates.ENCHANTMENTS,
                                                        ItemEnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX), MinMaxBounds.Ints.atLeast(1))))
                                                )
                                )
                        )
                        .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
                tableBuilder.withPool(poolBuilder);
            }
            else if (source.isBuiltin() && (key.location().equals(COPPER_ORE_ID) || key.location().equals(DEEPSLATE_COPPER_ORE_ID))) {
                // We make the pool and add an item
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.RAW_COPPER_BLOCK))
                        .when(
                                MatchTool.toolMatches(
                                        ItemPredicate.Builder.item()
                                                .withSubPredicate(
                                                        ItemSubPredicates.ENCHANTMENTS,
                                                        ItemEnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX), MinMaxBounds.Ints.atLeast(1))))
                                                )
                                )
                        )
                        .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
                tableBuilder.withPool(poolBuilder);
            }
            else if (source.isBuiltin() && (key.location().equals(IRON_ORE_ID) || key.location().equals(DEEPSLATE_IRON_ORE_ID))) {
                // We make the pool and add an item
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.RAW_IRON_BLOCK))
                        .when(
                                MatchTool.toolMatches(
                                        ItemPredicate.Builder.item()
                                                .withSubPredicate(
                                                        ItemSubPredicates.ENCHANTMENTS,
                                                        ItemEnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX), MinMaxBounds.Ints.atLeast(1))))
                                                )
                                )
                        )
                        .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
                tableBuilder.withPool(poolBuilder);
            }
            else if (source.isBuiltin() && (key.location().equals(GOLD_ORE_ID) || key.location().equals(DEEPSLATE_GOLD_ORE_ID))) {
                // We make the pool and add an item
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.RAW_GOLD_BLOCK))
                        .when(
                                MatchTool.toolMatches(
                                        ItemPredicate.Builder.item()
                                                .withSubPredicate(
                                                        ItemSubPredicates.ENCHANTMENTS,
                                                        ItemEnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX), MinMaxBounds.Ints.atLeast(1))))
                                                )
                                )
                        )
                        .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
                tableBuilder.withPool(poolBuilder);
            }
            else if (source.isBuiltin() && (key.location().equals(EMERALD_ORE_ID) || key.location().equals(DEEPSLATE_EMERALD_ORE_ID))) {
                // We make the pool and add an item
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.EMERALD_BLOCK))
                        .when(
                                MatchTool.toolMatches(
                                        ItemPredicate.Builder.item()
                                                .withSubPredicate(
                                                        ItemSubPredicates.ENCHANTMENTS,
                                                        ItemEnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX), MinMaxBounds.Ints.atLeast(1))))
                                                )
                                )
                        )
                        .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
                tableBuilder.withPool(poolBuilder);
            }
            else if (source.isBuiltin() && (key.location().equals(LAPIS_ORE_ID) || key.location().equals(DEEPSLATE_LAPIS_ORE_ID))) {
                // We make the pool and add an item
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.LAPIS_BLOCK))
                        .when(
                                MatchTool.toolMatches(
                                        ItemPredicate.Builder.item()
                                                .withSubPredicate(
                                                        ItemSubPredicates.ENCHANTMENTS,
                                                        ItemEnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX), MinMaxBounds.Ints.atLeast(1))))
                                                )
                                )
                        )
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F)))
                        .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
                tableBuilder.withPool(poolBuilder);
            }
            else if (source.isBuiltin() && (key.location().equals(REDSTONE_ORE_ID) || key.location().equals(DEEPSLATE_REDSTONE_ORE_ID))) {
                // We make the pool and add an item
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.REDSTONE_BLOCK))
                        .when(
                                MatchTool.toolMatches(
                                        ItemPredicate.Builder.item()
                                                .withSubPredicate(
                                                        ItemSubPredicates.ENCHANTMENTS,
                                                        ItemEnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX), MinMaxBounds.Ints.atLeast(1))))
                                                )
                                )
                        )
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 5.0F)))
                        .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
                tableBuilder.withPool(poolBuilder);
            }
            else if (source.isBuiltin() && (key.location().equals(NETHER_GOLD_ORE_ID))) {
                // We make the pool and add an item
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.GOLD_INGOT))
                        .when(
                                MatchTool.toolMatches(
                                        ItemPredicate.Builder.item()
                                                .withSubPredicate(
                                                        ItemSubPredicates.ENCHANTMENTS,
                                                        ItemEnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX), MinMaxBounds.Ints.atLeast(1))))
                                                )
                                )
                        )
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F)))
                        .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
                tableBuilder.withPool(poolBuilder);
            }
            else if (source.isBuiltin() && (key.location().equals(ANCIENT_DEBRIS_ID))) {
                // We make the pool and add an item
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.NETHERITE_INGOT))
                        .when(
                                MatchTool.toolMatches(
                                        ItemPredicate.Builder.item()
                                                .withSubPredicate(
                                                        ItemSubPredicates.ENCHANTMENTS,
                                                        ItemEnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX), MinMaxBounds.Ints.atLeast(1))))
                                                )
                                )
                        )
                        .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
                tableBuilder.withPool(poolBuilder);
            }
        });
    }
}
