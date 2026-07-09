package com.pshchwy.enex.util;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.advancements.critereon.EnchantedItemTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.intellij.lang.annotations.Identifier;

public class EXLootTableModifiers {
    private static final ResourceLocation DIAMOND_ORE_ID = ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/diamond_ore");
    public static void modifyLootTables() {

    }
}
