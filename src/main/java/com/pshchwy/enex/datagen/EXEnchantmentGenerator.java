package com.pshchwy.enex.datagen;

import com.pshchwy.enex.EnchantmentsEX;
import com.pshchwy.enex.enchantment.EXEnchantmentEffects;
import com.pshchwy.enex.enchantment.effect.*;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.EnchantmentLevelProvider;
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
                        new ApplyMobEffect(
                                HolderSet.<MobEffect>direct(MobEffects.WITHER),
                                LevelBasedValue.constant(1.5F),
                                LevelBasedValue.perLevel(1.5F, 0.5F),
                                LevelBasedValue.constant(1.0F),
                                LevelBasedValue.constant(1.0F)
                        ),
                        LootItemRandomChanceCondition.randomChance(EnchantmentLevelProvider.forEnchantmentLevel(LevelBasedValue.perLevel(0.15F)))
                ).withEffect(
                        EnchantmentEffectComponents.DAMAGE,
                        new AddValue(LevelBasedValue.perLevel(1.0f, 0.5f))
                ).exclusiveWith(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
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
                ).exclusiveWith(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
        );

        // register Aqua Affinity EX
        register(entries, EXEnchantmentEffects.AQUA_AFFINITY_EX, Enchantment.enchantment(
                        Enchantment.definition(
                                // which items can be enchanted
                                registries.lookupOrThrow(Registries.ITEM).getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE),
                                // weight of showing up in enchantment table
                                1,
                                // enchantment max level
                                1,
                                // base cost for level 1 of the enchantment, and min levels required for something higher
                                Enchantment.dynamicCost(5, 8),
                                // same fields as above but for max cost
                                Enchantment.dynamicCost(25, 8),
                                // anvil cost
                                5,
                                // valid slots
                                EquipmentSlotGroup.HEAD
                        )
                ).withEffect( // Vanilla Aqua Affinity EX
                        EnchantmentEffectComponents.ATTRIBUTES,
                        new EnchantmentAttributeEffect(
                                // id
                                ResourceLocation.withDefaultNamespace("enchantment.aqua_affinity_ex"),
                                // attribute
                                Attributes.SUBMERGED_MINING_SPEED,
                                // Multiplier
                                LevelBasedValue.perLevel(4.0f),
                                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                        )
                ).withEffect(
                        EnchantmentEffectComponents.TICK,
                        new AquaAffinityEXEffect(LevelBasedValue.constant(0.0f))
                )
        );

        // register Bane of Arthropods EX
        register(entries, EXEnchantmentEffects.BANE_OF_ARTHROPODS_EX, Enchantment.enchantment(
                        Enchantment.definition(
                                // which items can be enchanted
                                registries.lookupOrThrow(Registries.ITEM).getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
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
                ).withEffect(
                        EnchantmentEffectComponents.DAMAGE,
                        new AddValue(LevelBasedValue.perLevel(2.5f, 2.5f)),
                        LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity()
                                        .entityType(
                                                EntityTypePredicate.of(
                                                        registries.lookupOrThrow(Registries.ENTITY_TYPE)
                                                                .getOrThrow(EntityTypeTags.SENSITIVE_TO_BANE_OF_ARTHROPODS).key()
                                                )
                                        )
                        )
                ).withEffect(
                        EnchantmentEffectComponents.TICK,
                        new BaneOfArthropodsEXEffect(LevelBasedValue.constant(0.0f))
                ).withEffect(
                        EnchantmentEffectComponents.POST_ATTACK,
                        EnchantmentTarget.ATTACKER,
                        EnchantmentTarget.VICTIM,
                        new ApplyMobEffect(
                                HolderSet.<MobEffect>direct(MobEffects.MOVEMENT_SLOWDOWN),
                                LevelBasedValue.constant(1.5F),
                                LevelBasedValue.perLevel(1.5F, 0.5F),
                                LevelBasedValue.constant(3.0F),
                                LevelBasedValue.constant(3.0F)
                        ),
                        LootItemEntityPropertyCondition.hasProperties(
                                        LootContext.EntityTarget.THIS,
                                        net.minecraft.advancements.critereon.EntityPredicate.Builder.entity()
                                                .entityType(EntityTypePredicate.of(EntityTypeTags.SENSITIVE_TO_BANE_OF_ARTHROPODS))
                                )
                                .and(DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().isDirect(true)))
                ).exclusiveWith(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
        );

        // register Blast Protection EX
        register(entries, EXEnchantmentEffects.BLAST_PROTECTION_EX, Enchantment.enchantment(
                        Enchantment.definition(
                                // which items can be enchanted
                                registries.lookupOrThrow(Registries.ITEM).getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                                // weight of showing up in enchantment table
                                1,
                                // enchantment max level
                                4,
                                // base cost for level 1 of the enchantment, and min levels required for something higher
                                Enchantment.dynamicCost(5, 8),
                                // same fields as above but for max cost
                                Enchantment.dynamicCost(13, 8),
                                // anvil cost
                                5,
                                // valid slots
                                EquipmentSlotGroup.ARMOR
                        )
                ).withEffect(
                        EnchantmentEffectComponents.TICK,
                        new BlastProtectionEXEffect(LevelBasedValue.perLevel(1.0f))
                ).exclusiveWith(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EnchantmentTags.ARMOR_EXCLUSIVE))
        );

        // register Breach EX
        register(entries, EXEnchantmentEffects.BREACH_EX, Enchantment.enchantment(
                        Enchantment.definition(
                                // which items can be enchanted
                                registries.lookupOrThrow(Registries.ITEM).getOrThrow(ItemTags.MACE_ENCHANTABLE),
                                // weight of showing up in enchantment table
                                1,
                                // enchantment max level
                                4,
                                // base cost for level 1 of the enchantment, and min levels required for something higher
                                Enchantment.dynamicCost(15, 9),
                                // same fields as above but for max cost
                                Enchantment.dynamicCost(65, 9),
                                // anvil cost
                                5,
                                // valid slots
                                EquipmentSlotGroup.MAINHAND
                        )
                ).withEffect(EnchantmentEffectComponents.ARMOR_EFFECTIVENESS, new AddValue(LevelBasedValue.perLevel(-0.15F))
                ).exclusiveWith(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE)
                ).withEffect( // BIG damage to mobs with large health
                        EnchantmentEffectComponents.DAMAGE,
                        new AddValue(LevelBasedValue.perLevel(10.0f, 5.0f)),
                        LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity()
                                        .entityType(
                                                EntityTypePredicate.of(
                                                        registries.lookupOrThrow(Registries.ENTITY_TYPE)
                                                                .getOrThrow(EXMobTagProvider.BREACH_EX_VULNERABLE).key()
                                                )
                                        )
                        )
                )
        );

        // register Channeling EX
        register(entries, EXEnchantmentEffects.CHANNELING_EX, Enchantment.enchantment(
                        Enchantment.definition(
                                // which items can be enchanted
                                registries.lookupOrThrow(Registries.ITEM).getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                                // weight of showing up in enchantment table
                                1,
                                // enchantment max level
                                1,
                                // base cost for level 1 of the enchantment, and min levels required for something higher
                                Enchantment.constantCost(25),
                                // same fields as above but for max cost
                                Enchantment.constantCost(50),
                                // anvil cost
                                5,
                                // valid slots
                                EquipmentSlotGroup.MAINHAND
                        )
                )
                .withEffect(
                        EnchantmentEffectComponents.POST_ATTACK,
                        EnchantmentTarget.ATTACKER,
                        EnchantmentTarget.VICTIM,
                        AllOf.entityEffects(
                                new SummonEntityEffect(HolderSet.<EntityType<?>>direct(EntityType.LIGHTNING_BOLT.builtInRegistryHolder()), false),
                                new PlaySoundEffect(SoundEvents.TRIDENT_THUNDER, ConstantFloat.of(5.0F), ConstantFloat.of(1.0F))
                        ),
                        AllOfCondition.allOf(
                                LootItemEntityPropertyCondition.hasProperties(
                                        LootContext.EntityTarget.THIS,
                                        net.minecraft.advancements.critereon.EntityPredicate.Builder.entity()
                                                .located(net.minecraft.advancements.critereon.LocationPredicate.Builder.location().setCanSeeSky(true))
                                ),
                                LootItemEntityPropertyCondition.hasProperties(
                                        LootContext.EntityTarget.DIRECT_ATTACKER, net.minecraft.advancements.critereon.EntityPredicate.Builder.entity().of(EntityType.TRIDENT)
                                )
                        )
                )
                .withEffect(
                        EnchantmentEffectComponents.HIT_BLOCK,
                        AllOf.entityEffects(
                                new SummonEntityEffect(HolderSet.<EntityType<?>>direct(EntityType.LIGHTNING_BOLT.builtInRegistryHolder()), false),
                                new PlaySoundEffect(SoundEvents.TRIDENT_THUNDER, ConstantFloat.of(5.0F), ConstantFloat.of(1.0F))
                        ),
                        AllOfCondition.allOf(
                                LootItemEntityPropertyCondition.hasProperties(
                                        LootContext.EntityTarget.THIS, net.minecraft.advancements.critereon.EntityPredicate.Builder.entity().of(EntityType.TRIDENT)
                                ),
                                LocationCheck.checkLocation(net.minecraft.advancements.critereon.LocationPredicate.Builder.location().setCanSeeSky(true))
                        )
            )
        );

        // register Density EX
        register(entries, EXEnchantmentEffects.DENSITY_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        registries.lookupOrThrow(Registries.ITEM).getOrThrow(ItemTags.MACE_ENCHANTABLE),
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
                ).exclusiveWith(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
                .withEffect(EnchantmentEffectComponents.SMASH_DAMAGE_PER_FALLEN_BLOCK, new AddValue(LevelBasedValue.perLevel(0.5F)))
                .withEffect(
                        EnchantmentEffectComponents.POST_ATTACK,
                        EnchantmentTarget.ATTACKER,
                        EnchantmentTarget.VICTIM,
                        new DensityEXEffect(LevelBasedValue.perLevel(0.4f, 0.2f))
                )
        );

        // register Depth Strider EX
        register(entries, EXEnchantmentEffects.DEPTH_STRIDER_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        registries.lookupOrThrow(Registries.ITEM).getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        3,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(10, 10),
                                        // same fields as above but for max cost
                                        Enchantment.dynamicCost(25, 10),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.FEET
                                )
                        )
                .exclusiveWith(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EnchantmentTags.BOOTS_EXCLUSIVE))
                .withEffect(
                        EnchantmentEffectComponents.ATTRIBUTES,
                        new EnchantmentAttributeEffect(
                                ResourceLocation.withDefaultNamespace("enchantment.depth_strider_ex"),
                                Attributes.WATER_MOVEMENT_EFFICIENCY,
                                LevelBasedValue.perLevel(0.33333334F),
                                AttributeModifier.Operation.ADD_VALUE
                        )
                )
                .withEffect(
                        EnchantmentEffectComponents.LOCATION_CHANGED,
                        new EnchantmentAttributeEffect(
                                ResourceLocation.withDefaultNamespace("enchantment.depth_strider_ex"),
                                Attributes.ATTACK_SPEED,
                                LevelBasedValue.perLevel(0.25F, 0.25F),
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        AnyOfCondition.anyOf(
                                LootItemEntityPropertyCondition.hasProperties(
                                        LootContext.EntityTarget.THIS,
                                        net.minecraft.advancements.critereon.EntityPredicate.Builder.entity()
                                                .movementAffectedBy(
                                                        net.minecraft.advancements.critereon.LocationPredicate.Builder.location()
                                                                .setFluid(net.minecraft.advancements.critereon.FluidPredicate.Builder.fluid().of(Fluids.WATER))
                                                )
                                ),
                                LootItemEntityPropertyCondition.hasProperties(
                                        LootContext.EntityTarget.THIS,
                                        net.minecraft.advancements.critereon.EntityPredicate.Builder.entity()
                                                .movementAffectedBy(
                                                        net.minecraft.advancements.critereon.LocationPredicate.Builder.location()
                                                                .setFluid(net.minecraft.advancements.critereon.FluidPredicate.Builder.fluid().of(Fluids.FLOWING_WATER))
                                                )
                                )
                        )

                )
        );

        // register Efficiency EX
        register(entries, EXEnchantmentEffects.EFFICIENCY_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        registries.lookupOrThrow(Registries.ITEM).getOrThrow(ItemTags.MINING_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        5,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(1, 10),
                                        // same fields as above but for max cost
                                        Enchantment.dynamicCost(51, 10),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.MAINHAND
                                )
                        )
                .withEffect(
                        EnchantmentEffectComponents.ATTRIBUTES,
                        new EnchantmentAttributeEffect(
                                ResourceLocation.withDefaultNamespace("enchantment.efficiency_ex"), Attributes.MINING_EFFICIENCY, new LevelBasedValue.LevelsSquared(1.0F), AttributeModifier.Operation.ADD_VALUE
                        )
                )
                .withEffect( // extra damage to shulkers
                        EnchantmentEffectComponents.DAMAGE,
                        new AddValue(LevelBasedValue.perLevel(12.5f, 10.0f)),

                        LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity()
                                        .entityType(
                                                EntityTypePredicate.of(
                                                        EntityType.SHULKER
                                                )
                                        )
                        )
                )
                .withEffect( // zero armor for shulkers
                        EnchantmentEffectComponents.ARMOR_EFFECTIVENESS,
                        new SetValue(LevelBasedValue.constant(0.0F)),
                        LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity()
                                        .entityType(
                                                EntityTypePredicate.of(
                                                        EntityType.SHULKER
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
