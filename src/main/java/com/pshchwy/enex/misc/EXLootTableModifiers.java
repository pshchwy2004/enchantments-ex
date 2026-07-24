package com.pshchwy.enex.misc;

import com.pshchwy.enex.enchantment.EXEnchantmentEffects;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.advancements.predicates.DataComponentMatchers;
import net.minecraft.advancements.predicates.EnchantmentPredicate;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.predicates.DataComponentPredicates;
import net.minecraft.core.component.predicates.EnchantmentsPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;

public class EXLootTableModifiers {
    private static final Identifier DIAMOND_ORE_ID = Identifier.fromNamespaceAndPath("minecraft", "blocks/diamond_ore");
    private static final Identifier DEEPSLATE_DIAMOND_ORE_ID = Identifier.fromNamespaceAndPath("minecraft", "blocks/deepslate_diamond_ore");
    private static final Identifier COAL_ORE_ID = Identifier.fromNamespaceAndPath("minecraft", "blocks/coal_ore");
    private static final Identifier DEEPSLATE_COAL_ORE_ID = Identifier.fromNamespaceAndPath("minecraft", "blocks/deepslate_coal_ore");
    private static final Identifier COPPER_ORE_ID = Identifier.fromNamespaceAndPath("minecraft", "blocks/copper_ore");
    private static final Identifier DEEPSLATE_COPPER_ORE_ID = Identifier.fromNamespaceAndPath("minecraft", "blocks/deepslate_copper_ore");
    private static final Identifier IRON_ORE_ID = Identifier.fromNamespaceAndPath("minecraft", "blocks/iron_ore");
    private static final Identifier DEEPSLATE_IRON_ORE_ID = Identifier.fromNamespaceAndPath("minecraft", "blocks/deepslate_iron_ore");
    private static final Identifier GOLD_ORE_ID = Identifier.fromNamespaceAndPath("minecraft", "blocks/gold_ore");
    private static final Identifier DEEPSLATE_GOLD_ORE_ID = Identifier.fromNamespaceAndPath("minecraft", "blocks/deepslate_gold_ore");
    private static final Identifier EMERALD_ORE_ID = Identifier.fromNamespaceAndPath("minecraft", "blocks/emerald_ore");
    private static final Identifier DEEPSLATE_EMERALD_ORE_ID = Identifier.fromNamespaceAndPath("minecraft", "blocks/deepslate_emerald_ore");
    private static final Identifier LAPIS_ORE_ID = Identifier.fromNamespaceAndPath("minecraft", "blocks/lapis_ore");
    private static final Identifier DEEPSLATE_LAPIS_ORE_ID = Identifier.fromNamespaceAndPath("minecraft", "blocks/deepslate_lapis_ore");
    private static final Identifier REDSTONE_ORE_ID = Identifier.fromNamespaceAndPath("minecraft", "blocks/redstone_ore");
    private static final Identifier DEEPSLATE_REDSTONE_ORE_ID = Identifier.fromNamespaceAndPath("minecraft", "blocks/deepslate_redstone_ore");
    private static final Identifier NETHER_GOLD_ORE_ID = Identifier.fromNamespaceAndPath("minecraft", "blocks/nether_gold_ore");
    private static final Identifier ANCIENT_DEBRIS_ID = Identifier.fromNamespaceAndPath("minecraft", "blocks/ancient_debris");
    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            HolderLookup.RegistryLookup<Enchantment> registryLookup = registries.lookupOrThrow(Registries.ENCHANTMENT);

            // simplified builder
            var fortuneEXCondition = MatchTool.toolMatches(ItemPredicate.Builder.item().withComponents(DataComponentMatchers.Builder.components().partial(DataComponentPredicates.ENCHANTMENTS, EnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EXEnchantmentEffects.FORTUNE_EX), MinMaxBounds.Ints.atLeast(1))))).build()));
            var luckOfTheSeaEXCondition = MatchTool.toolMatches(ItemPredicate.Builder.item().withComponents(DataComponentMatchers.Builder.components().partial(DataComponentPredicates.ENCHANTMENTS, EnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EXEnchantmentEffects.LUCK_OF_THE_SEA_EX), MinMaxBounds.Ints.atLeast(1))))).build()));
            // Let's only modify built-in loot tables and leave data pack loot tables untouched by checking the source.
            // We also check that the loot table ID is equal to the ID we want.
            if (source.isBuiltin() && (key.identifier().equals(DIAMOND_ORE_ID) || key.identifier().equals(DEEPSLATE_DIAMOND_ORE_ID))) {
                // We make the pool and add an item
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.DIAMOND_BLOCK))
                        .when(fortuneEXCondition)
                        .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
                tableBuilder.withPool(poolBuilder);
            }
            else if (source.isBuiltin() && (key.identifier().equals(COAL_ORE_ID) || key.identifier().equals(DEEPSLATE_COAL_ORE_ID))) {
                // We make the pool and add an item
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.COAL_BLOCK))
                        .when(fortuneEXCondition)
                        .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
                tableBuilder.withPool(poolBuilder);
            }
            else if (source.isBuiltin() && (key.identifier().equals(COPPER_ORE_ID) || key.identifier().equals(DEEPSLATE_COPPER_ORE_ID))) {
                // We make the pool and add an item
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.RAW_COPPER_BLOCK))
                        .when(fortuneEXCondition)
                        .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
                tableBuilder.withPool(poolBuilder);
            }
            else if (source.isBuiltin() && (key.identifier().equals(IRON_ORE_ID) || key.identifier().equals(DEEPSLATE_IRON_ORE_ID))) {
                // We make the pool and add an item
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.RAW_IRON_BLOCK))
                        .when(fortuneEXCondition)
                        .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
                tableBuilder.withPool(poolBuilder);
            }
            else if (source.isBuiltin() && (key.identifier().equals(GOLD_ORE_ID) || key.identifier().equals(DEEPSLATE_GOLD_ORE_ID))) {
                // We make the pool and add an item
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.RAW_GOLD_BLOCK))
                        .when(fortuneEXCondition)
                        .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
                tableBuilder.withPool(poolBuilder);
            }
            else if (source.isBuiltin() && (key.identifier().equals(EMERALD_ORE_ID) || key.identifier().equals(DEEPSLATE_EMERALD_ORE_ID))) {
                // We make the pool and add an item
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.EMERALD_BLOCK))
                        .when(fortuneEXCondition)
                        .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
                tableBuilder.withPool(poolBuilder);
            }
            else if (source.isBuiltin() && (key.identifier().equals(LAPIS_ORE_ID) || key.identifier().equals(DEEPSLATE_LAPIS_ORE_ID))) {
                // We make the pool and add an item
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.LAPIS_BLOCK))
                        .when(fortuneEXCondition)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F)))
                        .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
                tableBuilder.withPool(poolBuilder);
            }
            else if (source.isBuiltin() && (key.identifier().equals(REDSTONE_ORE_ID) || key.identifier().equals(DEEPSLATE_REDSTONE_ORE_ID))) {
                // We make the pool and add an item
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.REDSTONE_BLOCK))
                        .when(fortuneEXCondition)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 5.0F)))
                        .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
                tableBuilder.withPool(poolBuilder);
            }
            else if (source.isBuiltin() && (key.identifier().equals(NETHER_GOLD_ORE_ID))) {
                // We make the pool and add an item
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.GOLD_INGOT))
                        .when(fortuneEXCondition)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F)))
                        .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
                tableBuilder.withPool(poolBuilder);
            }
            else if (source.isBuiltin() && (key.identifier().equals(ANCIENT_DEBRIS_ID))) {
                // We make the pool and add an item
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.NETHERITE_INGOT))
                        .when(fortuneEXCondition)
                        .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
                tableBuilder.withPool(poolBuilder);
            }

            // luck of the sea fishing shenanigans
            if (source.isBuiltin() && key.identifier().equals(Identifier.fromNamespaceAndPath("minecraft", "gameplay/fishing/treasure"))) {
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(20))
                        .add(LootItem.lootTableItem(Items.EMERALD).setWeight(20))
                        .add(LootItem.lootTableItem(Items.ELYTRA).setWeight(1))
                        .add(LootItem.lootTableItem(Items.NETHERITE_SCRAP).setWeight(1))
                        .add(LootItem.lootTableItem(Items.SHULKER_SHELL).setWeight(1))
                        .add(LootItem.lootTableItem(Items.HEART_OF_THE_SEA).setWeight(1))
                        .add(LootItem.lootTableItem(Items.DRAGON_HEAD).setWeight(1))
                        .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(10))
                        .add(LootItem.lootTableItem(Items.CONDUIT).setWeight(10))
                        .add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(5))
                        .add(LootItem.lootTableItem(Items.SPONGE).setWeight(20))
                        .add(LootItem.lootTableItem(Items.ECHO_SHARD).setWeight(10))
                        .when(luckOfTheSeaEXCondition);
                tableBuilder.withPool(poolBuilder);
            }
        });
    }
}
