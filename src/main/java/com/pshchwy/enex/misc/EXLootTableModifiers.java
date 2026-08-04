package com.pshchwy.enex.misc;

import com.pshchwy.enex.enchantment.EXEnchantmentEffects;
import net.minecraft.advancements.criterion.DataComponentMatchers;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.predicates.DataComponentPredicates;
import net.minecraft.core.component.predicates.EnchantmentsPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;

import java.util.List;

public class EXLootTableModifiers {

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {

        ResourceKey<LootTable> key = event.getKey();
        HolderLookup.Provider registries = event.getRegistries();
        HolderLookup.RegistryLookup<Enchantment> registryLookup = registries.lookupOrThrow(Registries.ENCHANTMENT);

        var fortuneEXCondition = MatchTool.toolMatches(ItemPredicate.Builder.item().withComponents(DataComponentMatchers.Builder.components().partial(DataComponentPredicates.ENCHANTMENTS, EnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EXEnchantmentEffects.FORTUNE_EX), MinMaxBounds.Ints.atLeast(1))))).build()));
        var luckOfTheSeaEXCondition = MatchTool.toolMatches(ItemPredicate.Builder.item().withComponents(DataComponentMatchers.Builder.components().partial(DataComponentPredicates.ENCHANTMENTS, EnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EXEnchantmentEffects.LUCK_OF_THE_SEA_EX), MinMaxBounds.Ints.atLeast(1))))).build()));
        // Diamond Ores
        if (key.equals(Blocks.DIAMOND_ORE.getLootTable().orElseThrow()) || key.equals(Blocks.DEEPSLATE_DIAMOND_ORE.getLootTable().orElseThrow())) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .add(LootItem.lootTableItem(Items.DIAMOND_BLOCK))
                    .when(fortuneEXCondition)
                    .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
            event.getTable().addPool(poolBuilder.build());
        }
        // Coal Ores
        else if (key.equals(Blocks.COAL_ORE.getLootTable().orElseThrow()) || key.equals(Blocks.DEEPSLATE_COAL_ORE.getLootTable().orElseThrow())) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .add(LootItem.lootTableItem(Items.COAL_BLOCK))
                    .when(fortuneEXCondition)
                    .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
            event.getTable().addPool(poolBuilder.build());
        }
        // Copper Ores
        else if (key.equals(Blocks.COPPER_ORE.getLootTable().orElseThrow()) || key.equals(Blocks.DEEPSLATE_COPPER_ORE.getLootTable().orElseThrow())) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .add(LootItem.lootTableItem(Items.RAW_COPPER_BLOCK))
                    .when(fortuneEXCondition)
                    .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
            event.getTable().addPool(poolBuilder.build());
        }
        // Iron Ores
        else if (key.equals(Blocks.IRON_ORE.getLootTable().orElseThrow()) || key.equals(Blocks.DEEPSLATE_IRON_ORE.getLootTable().orElseThrow())) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .add(LootItem.lootTableItem(Items.RAW_IRON_BLOCK))
                    .when(fortuneEXCondition)
                    .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
            event.getTable().addPool(poolBuilder.build());
        }
        // Gold Ores
        else if (key.equals(Blocks.GOLD_ORE.getLootTable().orElseThrow()) || key.equals(Blocks.DEEPSLATE_GOLD_ORE.getLootTable().orElseThrow())) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .add(LootItem.lootTableItem(Items.RAW_GOLD_BLOCK))
                    .when(fortuneEXCondition)
                    .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
            event.getTable().addPool(poolBuilder.build());
        }
        // Emerald Ores
        else if (key.equals(Blocks.EMERALD_ORE.getLootTable().orElseThrow()) || key.equals(Blocks.DEEPSLATE_EMERALD_ORE.getLootTable().orElseThrow())) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .add(LootItem.lootTableItem(Items.EMERALD_BLOCK))
                    .when(fortuneEXCondition)
                    .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
            event.getTable().addPool(poolBuilder.build());
        }
        // Lapis Ores
        else if (key.equals(Blocks.LAPIS_ORE.getLootTable().orElseThrow()) || key.equals(Blocks.DEEPSLATE_LAPIS_ORE.getLootTable().orElseThrow())) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .add(LootItem.lootTableItem(Items.LAPIS_BLOCK))
                    .when(fortuneEXCondition)
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F)))
                    .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
            event.getTable().addPool(poolBuilder.build());
        }
        // Redstone Ores
        else if (key.equals(Blocks.REDSTONE_ORE.getLootTable().orElseThrow()) || key.equals(Blocks.DEEPSLATE_REDSTONE_ORE.getLootTable().orElseThrow())) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .add(LootItem.lootTableItem(Items.REDSTONE_BLOCK))
                    .when(fortuneEXCondition)
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 5.0F)))
                    .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
            event.getTable().addPool(poolBuilder.build());
        }
        // Nether Gold Ore
        else if (key.equals(Blocks.NETHER_GOLD_ORE.getLootTable().orElseThrow())) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .add(LootItem.lootTableItem(Items.GOLD_INGOT))
                    .when(fortuneEXCondition)
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F)))
                    .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
            event.getTable().addPool(poolBuilder.build());
        }
        // Ancient Debris
        else if (key.equals(Blocks.ANCIENT_DEBRIS.getLootTable().orElseThrow())) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .add(LootItem.lootTableItem(Items.NETHERITE_INGOT))
                    .when(fortuneEXCondition)
                    .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(EXEnchantmentEffects.FORTUNE_EX)));
            event.getTable().addPool(poolBuilder.build());
        }
        // Fishing Treasure
        else if (key.equals(BuiltInLootTables.FISHING_TREASURE)) {
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
            event.getTable().addPool(poolBuilder.build());
        }
    }
}