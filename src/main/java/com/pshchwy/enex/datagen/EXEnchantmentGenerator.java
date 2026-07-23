package com.pshchwy.enex.datagen;

import com.pshchwy.enex.enchantment.EXEnchantmentEffects;
import com.pshchwy.enex.enchantment.effect.*;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.minecraft.advancements.criterion.*;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.EnchantmentLevelProvider;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/// This class is a Provider that handles the registration of all new EX enchantments. Mapping their original variants happens in EXEnchantmentMap.java.
public class EXEnchantmentGenerator extends FabricDynamicRegistryProvider {
    public EXEnchantmentGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
        System.out.println("registering enchant generator...");
    }
    /**
     * Registers all EX enchantments.
     */
    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        // easy variables for access
        HolderGetter<DamageType> damageTypes = registries.lookupOrThrow(Registries.DAMAGE_TYPE);
        HolderGetter<Enchantment> enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT);
        HolderGetter<Item> items = registries.lookupOrThrow(Registries.ITEM);
        @SuppressWarnings("unused")
        HolderGetter<Block> blocks = registries.lookupOrThrow(Registries.BLOCK);
        HolderGetter<EntityType<?>>  entityTypes = registries.lookupOrThrow(Registries.ENTITY_TYPE);
        // register Knockback EX
        register(entries, EXEnchantmentEffects.KNOCKBACK_EX, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(ItemTags.MELEE_WEAPON_ENCHANTABLE),
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
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.KNOCKBACK_EXCLUSIVE))
        );
        // register Sharpness EX
        register(entries, EXEnchantmentEffects.SHARPNESS_EX, Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(ItemTags.MELEE_WEAPON_ENCHANTABLE),
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
                                HolderSet.direct(MobEffects.WITHER),
                                LevelBasedValue.constant(15.0F),
                                LevelBasedValue.perLevel(15.0F, 2.5F),
                                LevelBasedValue.constant(1.0F),
                                LevelBasedValue.constant(1.0F)
                        ),
                        LootItemRandomChanceCondition.randomChance(EnchantmentLevelProvider.forEnchantmentLevel(LevelBasedValue.perLevel(0.15F)))
                ).withEffect(
                        EnchantmentEffectComponents.DAMAGE,
                        new AddValue(LevelBasedValue.perLevel(1.0f, 0.5f))
                )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.SHARPNESS_EXCLUSIVE))
        );
        // register Smite EX
        register(entries, EXEnchantmentEffects.SMITE_EX, Enchantment.enchantment(
                        Enchantment.definition(
                                // which items can be enchanted
                                items.getOrThrow(ItemTags.MELEE_WEAPON_ENCHANTABLE),
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
                                                entityTypes,
                                                EntityTypeTags.SENSITIVE_TO_SMITE
                                        )
                                )
                )
                )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.SMITE_EXCLUSIVE))
        );

        // register Aqua Affinity EX
        register(entries, EXEnchantmentEffects.AQUA_AFFINITY_EX, Enchantment.enchantment(
                        Enchantment.definition(
                                // which items can be enchanted
                                items.getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE),
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
                                Identifier.withDefaultNamespace("enchantment.aqua_affinity_ex"),
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
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.AQUA_AFFINITY_EXCLUSIVE))
        );

        // register Bane of Arthropods EX
        register(entries, EXEnchantmentEffects.BANE_OF_ARTHROPODS_EX, Enchantment.enchantment(
                        Enchantment.definition(
                                // which items can be enchanted
                                items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                                items.getOrThrow(ItemTags.MELEE_WEAPON_ENCHANTABLE),
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
                                                        entityTypes,
                                                        EntityTypeTags.SENSITIVE_TO_BANE_OF_ARTHROPODS
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
                                HolderSet.direct(MobEffects.SLOWNESS),
                                LevelBasedValue.constant(1.5F),
                                LevelBasedValue.perLevel(1.5F, 0.5F),
                                LevelBasedValue.constant(3.0F),
                                LevelBasedValue.constant(3.0F)
                        ),
                        LootItemEntityPropertyCondition.hasProperties(
                                        LootContext.EntityTarget.THIS,
                                        EntityPredicate.Builder.entity()
                                                .entityType(
                                                        EntityTypePredicate.of(
                                                                entityTypes,
                                                                EntityTypeTags.SENSITIVE_TO_BANE_OF_ARTHROPODS
                                                        )
                                                )
                                )
                                .and(DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().isDirect(true)))
                )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.BANE_OF_ARTHROPODS_EXCLUSIVE))
        );

        // register Blast Protection EX
        register(entries, EXEnchantmentEffects.BLAST_PROTECTION_EX, Enchantment.enchantment(
                        Enchantment.definition(
                                // which items can be enchanted
                                items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
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
                )
                .withEffect(
                        EnchantmentEffectComponents.DAMAGE_PROTECTION,
                        new AddValue(LevelBasedValue.perLevel(2.0F)),
                        DamageSourceCondition.hasDamageSource(
                                DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_EXPLOSION)).tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))
                        )
                )
                .withEffect(
                        EnchantmentEffectComponents.ATTRIBUTES,
                        new EnchantmentAttributeEffect(
                                Identifier.withDefaultNamespace("enchantment.blast_protection_ex"),
                                Attributes.EXPLOSION_KNOCKBACK_RESISTANCE,
                                LevelBasedValue.perLevel(0.15F),
                                AttributeModifier.Operation.ADD_VALUE
                        )
                )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.BLAST_PROTECTION_EXCLUSIVE))
        );

        // register Breach EX
        register(entries, EXEnchantmentEffects.BREACH_EX, Enchantment.enchantment(
                        Enchantment.definition(
                                // which items can be enchanted
                                items.getOrThrow(ItemTags.MACE_ENCHANTABLE),
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
                )
                .withEffect( // BIG damage to mobs with large health
                        EnchantmentEffectComponents.DAMAGE,
                        new AddValue(LevelBasedValue.perLevel(10.0f, 5.0f)),
                        LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity()
                                        .entityType(
                                                EntityTypePredicate.of(
                                                        entityTypes,
                                                        EXMobTagProvider.BREACH_EX_VULNERABLE
                                                )
                                        )
                        )
                )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.BREACH_EXCLUSIVE))
        );

        // register Channeling EX
        register(entries, EXEnchantmentEffects.CHANNELING_EX, Enchantment.enchantment(
                        Enchantment.definition(
                                // which items can be enchanted
                                items.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
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
                                new SummonEntityEffect(HolderSet.direct(EntityType.LIGHTNING_BOLT.builtInRegistryHolder()), false),
                                new PlaySoundEffect(List.of(SoundEvents.TRIDENT_THUNDER), ConstantFloat.of(5.0F), ConstantFloat.of(1.0F))
                        ),
                        AllOfCondition.allOf(
                                LootItemEntityPropertyCondition.hasProperties(
                                        LootContext.EntityTarget.THIS,
                                        net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                                                .located(net.minecraft.advancements.criterion.LocationPredicate.Builder.location().setCanSeeSky(true))
                                ),
                                LootItemEntityPropertyCondition.hasProperties(
                                        LootContext.EntityTarget.DIRECT_ATTACKER, net.minecraft.advancements.criterion.EntityPredicate.Builder.entity().of(entityTypes, EntityType.TRIDENT)
                                )
                        )
                )
                .withEffect(
                        EnchantmentEffectComponents.HIT_BLOCK,
                        AllOf.entityEffects(
                                new SummonEntityEffect(HolderSet.direct(EntityType.LIGHTNING_BOLT.builtInRegistryHolder()), false),
                                new PlaySoundEffect(List.of(SoundEvents.TRIDENT_THUNDER), ConstantFloat.of(5.0F), ConstantFloat.of(1.0F))
                        ),
                        AllOfCondition.allOf(
                                LootItemEntityPropertyCondition.hasProperties(
                                        LootContext.EntityTarget.THIS, net.minecraft.advancements.criterion.EntityPredicate.Builder.entity().of(entityTypes, EntityType.TRIDENT)
                                ),
                                LocationCheck.checkLocation(net.minecraft.advancements.criterion.LocationPredicate.Builder.location().setCanSeeSky(true))
                        )
            )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.CHANNELING_EXCLUSIVE))
        );

        // register Density EX
        register(entries, EXEnchantmentEffects.DENSITY_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.MACE_ENCHANTABLE),
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
                )
                .withEffect(EnchantmentEffectComponents.SMASH_DAMAGE_PER_FALLEN_BLOCK, new AddValue(LevelBasedValue.perLevel(0.5F)))
                .withEffect(
                        EnchantmentEffectComponents.POST_ATTACK,
                        EnchantmentTarget.ATTACKER,
                        EnchantmentTarget.VICTIM,
                        new DensityEXEffect(LevelBasedValue.perLevel(0.4f, 0.2f))
                )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.DENSITY_EXCLUSIVE))
        );

        // register Depth Strider EX
        register(entries, EXEnchantmentEffects.DEPTH_STRIDER_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
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
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.DEPTH_STRIDER_EXCLUSIVE))
                .withEffect(
                        EnchantmentEffectComponents.ATTRIBUTES,
                        new EnchantmentAttributeEffect(
                                Identifier.withDefaultNamespace("enchantment.depth_strider_ex"),
                                Attributes.WATER_MOVEMENT_EFFICIENCY,
                                LevelBasedValue.perLevel(0.33333334F),
                                AttributeModifier.Operation.ADD_VALUE
                        )
                )
                .withEffect(
                        EnchantmentEffectComponents.LOCATION_CHANGED,
                        new EnchantmentAttributeEffect(
                                Identifier.withDefaultNamespace("enchantment.depth_strider_ex"),
                                Attributes.ATTACK_SPEED,
                                LevelBasedValue.perLevel(0.25F, 0.25F),
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        AnyOfCondition.anyOf(
                                LootItemEntityPropertyCondition.hasProperties(
                                        LootContext.EntityTarget.THIS,
                                        net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                                                .movementAffectedBy(
                                                        net.minecraft.advancements.criterion.LocationPredicate.Builder.location()
                                                                .setFluid(net.minecraft.advancements.criterion.FluidPredicate.Builder.fluid().of(Fluids.WATER))
                                                )
                                ),
                                LootItemEntityPropertyCondition.hasProperties(
                                        LootContext.EntityTarget.THIS,
                                        net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                                                .movementAffectedBy(
                                                        net.minecraft.advancements.criterion.LocationPredicate.Builder.location()
                                                                .setFluid(net.minecraft.advancements.criterion.FluidPredicate.Builder.fluid().of(Fluids.FLOWING_WATER))
                                                )
                                )
                        )

                )
                .withEffect(
                        EnchantmentEffectComponents.TICK,
                        new DepthStriderEXEffect(LevelBasedValue.perLevel(1.0f))
                )
        );

        // register Efficiency EX
        register(entries, EXEnchantmentEffects.EFFICIENCY_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.MINING_ENCHANTABLE),
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
                .withEffect( // vanilla efficiency
                        EnchantmentEffectComponents.ATTRIBUTES,
                        new EnchantmentAttributeEffect(
                                Identifier.withDefaultNamespace("enchantment.efficiency_ex"), Attributes.MINING_EFFICIENCY, new LevelBasedValue.LevelsSquared(1.0F), AttributeModifier.Operation.ADD_VALUE
                        )
                )
                .withEffect( // more block interaction range
                        EnchantmentEffectComponents.ATTRIBUTES,
                        new EnchantmentAttributeEffect(
                                Identifier.withDefaultNamespace("enchantment.efficiency_ex"), Attributes.BLOCK_INTERACTION_RANGE, LevelBasedValue.constant(2.25F), AttributeModifier.Operation.ADD_VALUE
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
                                                        entityTypes, EntityType.SHULKER
                                                )
                                        )
                        )
                )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.EFFICIENCY_EXCLUSIVE))
                .withEffect( // zero armor for shulkers
                        EnchantmentEffectComponents.ARMOR_EFFECTIVENESS,
                        new SetValue(LevelBasedValue.constant(0.0F)),
                        LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity()
                                        .entityType(
                                                EntityTypePredicate.of(
                                                        entityTypes, EntityType.SHULKER
                                                )
                                        )
                        )
                )
        );

        // register Feather Falling EX
        register(entries, EXEnchantmentEffects.FEATHER_FALLING_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        5,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(5, 6),
                                        // same fields as above but for max cost
                                        Enchantment.dynamicCost(11, 6),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.FEET
                                )
                        )
                .withEffect(
                        EnchantmentEffectComponents.DAMAGE_PROTECTION,
                        new AddValue(LevelBasedValue.perLevel(3.0F)),
                        DamageSourceCondition.hasDamageSource(
                                DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_FALL)).tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))
                        )
                )
                .withEffect( // increase blocks needed to start taking FD
                        EnchantmentEffectComponents.ATTRIBUTES,
                        new EnchantmentAttributeEffect(
                                Identifier.withDefaultNamespace("enchantment.feather_falling_ex"), Attributes.SAFE_FALL_DISTANCE, LevelBasedValue.constant(7.0F), AttributeModifier.Operation.ADD_VALUE
                        )
                )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.FEATHER_FALLING_EXCLUSIVE))
        );

        // register Fire Aspect EX
        register(entries, EXEnchantmentEffects.FIRE_ASPECT_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.FIRE_ASPECT_ENCHANTABLE),
                                        items.getOrThrow(ItemTags.MELEE_WEAPON_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        2,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(10, 20),
                                        // same fields as above but for max cost
                                        Enchantment.dynamicCost(60, 20),
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
                        new Ignite(LevelBasedValue.perLevel(4.0F)),
                        DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().isDirect(true))
                )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.FIRE_ASPECT_EXCLUSIVE))
                .withEffect(
                        EnchantmentEffectComponents.DAMAGE,
                        new AddValue(LevelBasedValue.perLevel(5.0f, 5.0f)),
                        AnyOfCondition.anyOf(
                                LootItemEntityPropertyCondition.hasProperties(
                                        LootContext.EntityTarget.THIS,
                                        EntityPredicate.Builder.entity()
                                                .flags(new EntityFlagsPredicate.Builder().setOnFire(true))
                                ),
                                LootItemEntityPropertyCondition.hasProperties(
                                        LootContext.EntityTarget.THIS,
                                        net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                                                .entityType(EntityTypePredicate.of(entityTypes, EXMobTagProvider.FIRE_IMMUNE))
                                                .build()
                                )
                        )
                )
                .withEffect(
                        EnchantmentEffectComponents.POST_ATTACK,
                        EnchantmentTarget.ATTACKER,
                        EnchantmentTarget.VICTIM,
                        // Spawn particle burst at the target location
                        new SpawnParticlesEffect(
                                ParticleTypes.LAVA, // Particle type (e.g., LAVA, SMALL_FLAME, FLAME, LAVA, SWEEP_ATTACK, etc.)
                                SpawnParticlesEffect.inBoundingBox(), // Spawns at victim position
                                SpawnParticlesEffect.offsetFromEntityPosition(0.5F), // Box offset around victim
                                SpawnParticlesEffect.movementScaled(0.2F), // Particle spread speed
                                SpawnParticlesEffect.fixedVelocity(ConstantFloat.of(0.1F)),
                                ConstantFloat.of(15.0F) // Number of particles to spawn
                        ),
                        // Matching condition: Must be direct attack AND target is on fire OR immune to fire
                        AllOfCondition.allOf(
                                DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().isDirect(true)),
                                AnyOfCondition.anyOf(
                                        LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS,
                                                EntityPredicate.Builder.entity()
                                                        .flags(new EntityFlagsPredicate.Builder().setOnFire(true))
                                        ),
                                        LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS,
                                                EntityPredicate.Builder.entity()
                                                        .entityType(EntityTypePredicate.of(entityTypes, EXMobTagProvider.FIRE_IMMUNE))
                                                        .build()
                                        )
                                )
                        )
                )
        );

        // register Fire Protection EX
        register(entries, EXEnchantmentEffects.FIRE_PROTECTION_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        4,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(10, 8),
                                        // same fields as above but for max cost
                                        Enchantment.dynamicCost(18, 8),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.ARMOR
                                )
                        )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.FIRE_PROTECTION_EXCLUSIVE))
                .withEffect( // vanilla fire dmg reduction
                        EnchantmentEffectComponents.DAMAGE_PROTECTION,
                        new AddValue(LevelBasedValue.perLevel(2.0F)),
                        AllOfCondition.allOf(
                                DamageSourceCondition.hasDamageSource(
                                        DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_FIRE)).tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))
                                )
                        )
                )
                .withEffect( // vanilla burning time reduction
                        EnchantmentEffectComponents.ATTRIBUTES,
                        new EnchantmentAttributeEffect(
                                Identifier.withDefaultNamespace("enchantment.fire_protection_ex"),
                                Attributes.BURNING_TIME,
                                LevelBasedValue.perLevel(-0.15F),
                                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                        )
                )
                .withEffect( // damage protection per level when in the nether
                        EnchantmentEffectComponents.DAMAGE_PROTECTION,
                        new AddValue(LevelBasedValue.perLevel(1.0F)),
                        LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity()
                                        .located(
                                                LocationPredicate.Builder.inDimension(Level.NETHER)
                                        )
                        )
                )
        );

        // register Flame EX
        register(entries, EXEnchantmentEffects.FLAME_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.BOW_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        1,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.constantCost(50),
                                        // same fields as above but for max cost
                                        Enchantment.constantCost(50),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.MAINHAND
                                )
                        )
                .withEffect(EnchantmentEffectComponents.PROJECTILE_SPAWNED, new Ignite(LevelBasedValue.constant(100.0F))) // vanilla flame
                .withEffect( // increased damage against entities on fire and entities immune to fire
                        EnchantmentEffectComponents.DAMAGE,
                        new AddValue(LevelBasedValue.perLevel(5.0f, 5.0f)),
                        AllOfCondition.allOf(
                                AnyOfCondition.anyOf(
                                        LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS,
                                                EntityPredicate.Builder.entity()
                                                        .flags(new EntityFlagsPredicate.Builder().setOnFire(true))
                                        ),
                                        LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS,
                                                net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                                                        .entityType(EntityTypePredicate.of(entityTypes, EXMobTagProvider.FIRE_IMMUNE))
                                                        .build()
                                        )
                                ),
                                LootItemEntityPropertyCondition.hasProperties(
                                        LootContext.EntityTarget.DIRECT_ATTACKER, net.minecraft.advancements.criterion.EntityPredicate.Builder.entity().of(entityTypes, EntityTypeTags.ARROWS).build()
                                )
                        )

                )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.FLAME_EXCLUSIVE))

        );

        // register Fortune EX
        register(entries, EXEnchantmentEffects.FORTUNE_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.MINING_LOOT_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        3,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(15, 9),
                                        // same fields as above but for max cost
                                        Enchantment.dynamicCost(65, 9),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.MAINHAND
                                )
                        )
                        .withEffect(EnchantmentEffectComponents.BLOCK_EXPERIENCE, new MultiplyValue(LevelBasedValue.perLevel(1.5f, 0.5f)))
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.FORTUNE_EXCLUSIVE))
        );

        // register Frost Walker EX
        register(entries, EXEnchantmentEffects.FROST_WALKER_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        2,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(10, 10),
                                        // same fields as above but for max cost
                                        Enchantment.dynamicCost(10, 10),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.FEET
                                )
                        )
                .withEffect(
                        EnchantmentEffectComponents.DAMAGE_IMMUNITY,
                        DamageImmunity.INSTANCE,
                        DamageSourceCondition.hasDamageSource(
                                DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.BURN_FROM_STEPPING)).tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))
                        )
                )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.FROST_WALKER_EXCLUSIVE))
                .withEffect(
                        EnchantmentEffectComponents.LOCATION_CHANGED,
                        new ReplaceDisk(
                                new LevelBasedValue.Clamped(LevelBasedValue.perLevel(3.0F, 1.0F), 0.0F, 16.0F),
                                LevelBasedValue.constant(1.0F),
                                new Vec3i(0, -1, 0),
                                Optional.of(
                                        net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.allOf(
                                                net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.matchesTag(new Vec3i(0, 1, 0), BlockTags.AIR),
                                                net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.matchesBlocks(Blocks.WATER),
                                                net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.matchesFluids(Fluids.WATER),
                                                BlockPredicate.unobstructed()
                                        )
                                ),
                                BlockStateProvider.simple(Blocks.FROSTED_ICE),
                                Optional.of(GameEvent.BLOCK_PLACE)
                        ),
                        LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.THIS,
                                net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                                        .flags(net.minecraft.advancements.criterion.EntityFlagsPredicate.Builder.flags().setOnGround(true))
                        )
                )
                .withEffect(
                        EnchantmentEffectComponents.LOCATION_CHANGED,
                        new EnchantmentAttributeEffect(
                                Identifier.withDefaultNamespace("enchantment.frost_walker_ex"),
                                Attributes.MOVEMENT_SPEED,
                                LevelBasedValue.perLevel(0.0405F, 0.0105F),
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        AllOfCondition.allOf(
                                InvertedLootItemCondition.invert(
                                        LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS,
                                                net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                                                        .vehicle(net.minecraft.advancements.criterion.EntityPredicate.Builder.entity())
                                        )
                                ),
                                AnyOfCondition.anyOf(
                                        AllOfCondition.allOf(
                                                EnchantmentActiveCheck.enchantmentActiveCheck(),
                                                LootItemEntityPropertyCondition.hasProperties(
                                                        LootContext.EntityTarget.THIS,
                                                        net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                                                                .flags(net.minecraft.advancements.criterion.EntityFlagsPredicate.Builder.flags().setIsFlying(false))
                                                ),
                                                AnyOfCondition.anyOf(
                                                        LootItemEntityPropertyCondition.hasProperties(
                                                                LootContext.EntityTarget.THIS,
                                                                net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                                                                        .movementAffectedBy(
                                                                                net.minecraft.advancements.criterion.LocationPredicate.Builder.location()
                                                                                        .setBlock(net.minecraft.advancements.criterion.BlockPredicate.Builder.block().of(blocks, BlockTags.ICE))
                                                                        )
                                                        ),
                                                        LootItemEntityPropertyCondition.hasProperties(
                                                                LootContext.EntityTarget.THIS,
                                                                net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                                                                        .flags(net.minecraft.advancements.criterion.EntityFlagsPredicate.Builder.flags().setOnGround(false))
                                                                        .build()
                                                        )
                                                )
                                        ),
                                        AllOfCondition.allOf(
                                                EnchantmentActiveCheck.enchantmentInactiveCheck(),
                                                LootItemEntityPropertyCondition.hasProperties(
                                                        LootContext.EntityTarget.THIS,
                                                        net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                                                                .movementAffectedBy(
                                                                        net.minecraft.advancements.criterion.LocationPredicate.Builder.location()
                                                                                .setBlock(net.minecraft.advancements.criterion.BlockPredicate.Builder.block().of(blocks, BlockTags.ICE))
                                                                )
                                                                .flags(net.minecraft.advancements.criterion.EntityFlagsPredicate.Builder.flags().setIsFlying(false))
                                                )
                                        )
                                )
                        )
                )
                .withEffect(
                        EnchantmentEffectComponents.DAMAGE_PROTECTION,
                        new AddValue(LevelBasedValue.perLevel(5.0F)),
                        AllOfCondition.allOf(
                                DamageSourceCondition.hasDamageSource(
                                        DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_FREEZING)).tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))
                                )
                        )
                )
        );

        // register Impaling EX
        register(entries, EXEnchantmentEffects.IMPALING_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        5,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(1, 8),
                                        // same fields as above but for max cost
                                        Enchantment.dynamicCost(21, 8),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.MAINHAND
                                )
                        )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.IMPALING_EXCLUSIVE))
                .withEffect(
                        EnchantmentEffectComponents.DAMAGE,
                        new AddValue(LevelBasedValue.perLevel(2.5F)),
                        AnyOfCondition.anyOf(
                                LootItemEntityPropertyCondition.hasProperties(
                                        LootContext.EntityTarget.THIS,
                                        net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                                                .entityType(EntityTypePredicate.of(entityTypes, EntityTypeTags.SENSITIVE_TO_IMPALING))
                                                .build()
                                ),
                                LootItemEntityPropertyCondition.hasProperties(
                                        LootContext.EntityTarget.THIS,
                                        net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                                                .located(
                                                        LocationPredicate.Builder.location()
                                                                .setFluid(
                                                                    FluidPredicate.Builder.fluid().of(Fluids.WATER)
                                                                )
                                                )
                                ),
                                AllOfCondition.allOf(
                                        AnyOfCondition.anyOf(
                                                WeatherCheck.weather().setRaining(true),
                                                WeatherCheck.weather().setThundering(true)
                                        ),
                                        LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS,
                                                net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                                                        .located(
                                                                LocationPredicate.Builder.location()
                                                                        .setCanSeeSky(true)
                                                        )
                                        )
                                )

                        )

                )
                .withEffect(
                        EnchantmentEffectComponents.POST_ATTACK,
                        EnchantmentTarget.ATTACKER,
                        EnchantmentTarget.VICTIM,
                        new BreathStealEffect(LevelBasedValue.constant(1.0f))
                )
        );

        // register Infinity EX
        register(entries, EXEnchantmentEffects.INFINITY_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.BOW_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        1,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.constantCost(20),
                                        // same fields as above but for max cost
                                        Enchantment.constantCost(50),
                                        // anvil cost
                                        10,
                                        // valid slots
                                        EquipmentSlotGroup.MAINHAND
                                )
                        )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.INFINITY_EXCLUSIVE))
                .withEffect(
                        EnchantmentEffectComponents.AMMO_USE,
                        new SetValue(LevelBasedValue.constant(0.0F)),
                        AnyOfCondition.anyOf(
                                MatchTool.toolMatches(net.minecraft.advancements.criterion.ItemPredicate.Builder.item().of(items, Items.ARROW)),
                                MatchTool.toolMatches(net.minecraft.advancements.criterion.ItemPredicate.Builder.item().of(items, Items.SPECTRAL_ARROW)),
                                MatchTool.toolMatches(net.minecraft.advancements.criterion.ItemPredicate.Builder.item().of(items, Items.TIPPED_ARROW)),
                                MatchTool.toolMatches(net.minecraft.advancements.criterion.ItemPredicate.Builder.item().of(items, Items.FIREWORK_ROCKET))
                        )

                )
        );

        // register Looting EX
        register(entries, EXEnchantmentEffects.LOOTING_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.MELEE_WEAPON_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        3,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(15, 9),
                                        // same fields as above but for max cost
                                        Enchantment.dynamicCost(65, 9),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.MAINHAND
                                )
                        )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.LOOTING_EXCLUSIVE))
                .withEffect( // vanilla Looting
                        EnchantmentEffectComponents.EQUIPMENT_DROPS,
                        EnchantmentTarget.ATTACKER,
                        EnchantmentTarget.VICTIM,
                        new AddValue(LevelBasedValue.perLevel(0.01F)),
                        LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.ATTACKER,
                                net.minecraft.advancements.criterion.EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(entityTypes, EntityType.PLAYER))
                        )
                )
                .withEffect(EnchantmentEffectComponents.MOB_EXPERIENCE, new MultiplyValue(LevelBasedValue.perLevel(2.5f, 1.0f)))
        );

        // register Loyalty EX
        register(entries, EXEnchantmentEffects.LOYALTY_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        3,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(12, 7),
                                        // same fields as above but for max cost
                                        Enchantment.constantCost(50),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.MAINHAND
                                )
                        )
                .withEffect(EnchantmentEffectComponents.TRIDENT_RETURN_ACCELERATION, new AddValue(LevelBasedValue.perLevel(1.0F))) // vanilla effect
                .withEffect( // hit block? take all items o algo
                        EnchantmentEffectComponents.HIT_BLOCK,
                        new TridentItemRetrievalEffect(LevelBasedValue.constant(1.0f))
                )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.LOYALTY_EXCLUSIVE))

        );

        // register Luck of the Sea EX
        register(entries, EXEnchantmentEffects.LUCK_OF_THE_SEA_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.FISHING_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        3,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(15, 9),
                                        // same fields as above but for max cost
                                        Enchantment.dynamicCost(65, 9),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.MAINHAND
                                )
                        )
                .withEffect(EnchantmentEffectComponents.FISHING_LUCK_BONUS, new AddValue(LevelBasedValue.perLevel(1.0F))) // changes happen in the loot tables
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.LUCK_OF_THE_SEA_EXCLUSIVE))
        );

        // register Lunge EX
        register(entries, EXEnchantmentEffects.LUNGE_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.LUNGE_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        3,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(5, 8),
                                        // same fields as above but for max cost
                                        Enchantment.dynamicCost(25, 8),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.HAND
                                )
                        )
                .withEffect(
                        EnchantmentEffectComponents.POST_PIERCING_ATTACK,
                        AllOf.entityEffects(
                                new ChangeItemDamage(new LevelBasedValue.Constant(1.0F)),
                                new ApplyEntityImpulse(new Vec3(0.0, 0.0, 1.0), new Vec3(1.0, 0.0, 1.0), LevelBasedValue.perLevel(0.458F)),
                                new PlaySoundEffect(List.of(SoundEvents.LUNGE_1, SoundEvents.LUNGE_2, SoundEvents.LUNGE_3), ConstantFloat.of(1.0F), ConstantFloat.of(1.0F))
                        ),
                        AllOfCondition.allOf(
                                InvertedLootItemCondition.invert(
                                        LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS,
                                                net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                                                        .vehicle(net.minecraft.advancements.criterion.EntityPredicate.Builder.entity())
                                        )
                                ),
                                LootItemEntityPropertyCondition.hasProperties(
                                        LootContext.EntityTarget.THIS,
                                        net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                                                .flags(net.minecraft.advancements.criterion.EntityFlagsPredicate.Builder.flags().setIsFallFlying(false))
                                ),
                                LootItemEntityPropertyCondition.hasProperties(
                                        LootContext.EntityTarget.THIS,
                                        net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                                                .flags(net.minecraft.advancements.criterion.EntityFlagsPredicate.Builder.flags().setIsInWater(false))
                                )
                        )
                )
        );

        // register Lure EX
        register(entries, EXEnchantmentEffects.LURE_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.FISHING_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        3,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(15, 9),
                                        // same fields as above but for max cost
                                        Enchantment.dynamicCost(65, 9),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.MAINHAND
                                )
                        )
                .withEffect(EnchantmentEffectComponents.FISHING_TIME_REDUCTION, new AddValue(LevelBasedValue.perLevel(5.0F)))
                .withEffect(
                        EnchantmentEffectComponents.TICK,
                        new LureEXEffect(LevelBasedValue.constant(0.0f))
                ).exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.LURE_EXCLUSIVE))

        );

        // register Mending EX
        register(entries, EXEnchantmentEffects.MENDING_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.DURABILITY_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        1,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(25, 25),
                                        // same fields as above but for max cost
                                        Enchantment.dynamicCost(25, 25),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.ANY
                                )
                        )
                .withEffect(EnchantmentEffectComponents.REPAIR_WITH_XP, new MultiplyValue(LevelBasedValue.constant(4.0F)))
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.MENDING_EXCLUSIVE))
        );

        // register Multishot EX
        register(entries, EXEnchantmentEffects.MULTISHOT_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
                                        items.getOrThrow(ItemTags.BOW_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        5,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(10, 5),
                                        // same fields as above but for max cost
                                        Enchantment.dynamicCost(20, 5),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.MAINHAND
                                )
                        )
                // not exclusive with piercing
                .withEffect(EnchantmentEffectComponents.PROJECTILE_COUNT, new AddValue(LevelBasedValue.perLevel(4.0F)))
                .withEffect(EnchantmentEffectComponents.PROJECTILE_SPREAD, new AddValue(LevelBasedValue.perLevel(5.0F)))
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.MULTISHOT_EXCLUSIVE))
        );

        // register Piercing EX
        register(entries, EXEnchantmentEffects.PIERCING_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
                                        items.getOrThrow(ItemTags.BOW_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        4,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(1, 10),
                                        // same fields as above but for max cost
                                        Enchantment.constantCost(50),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.MAINHAND
                                )
                        )
                        // not exclusive with multishot
                .withEffect(EnchantmentEffectComponents.PROJECTILE_PIERCING, new AddValue(LevelBasedValue.perLevel(1.0F)))
                .withEffect(
                        EnchantmentEffectComponents.DAMAGE,
                        new AddValue(LevelBasedValue.perLevel(2.5f, 2.5f)),
                        AnyOfCondition.anyOf(
                                AllOfCondition.allOf(
                                        LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.DIRECT_ATTACKER, net.minecraft.advancements.criterion.EntityPredicate.Builder.entity().of(entityTypes, EntityTypeTags.ARROWS).build()
                                        ),
                                        LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS,
                                                EntityPredicate.Builder.entity()
                                                        .entityType(
                                                                EntityTypePredicate.of(
                                                                        entityTypes,
                                                                        EntityType.SKELETON
                                                                )
                                                        )
                                        )
                                ),
                                LootItemEntityPropertyCondition.hasProperties(
                                        LootContext.EntityTarget.DIRECT_ATTACKER, net.minecraft.advancements.criterion.EntityPredicate.Builder.entity().of(entityTypes, EntityType.FIREWORK_ROCKET).build()
                                )
                        )

                )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.PIERCING_EXCLUSIVE))
        );

        // register Power EX
        register(entries, EXEnchantmentEffects.POWER_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.BOW_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        5,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(1, 10),
                                        // same fields as above but for max cost
                                        Enchantment.dynamicCost(16, 10),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.MAINHAND
                                )
                        )
                .withEffect(
                        EnchantmentEffectComponents.DAMAGE,
                        new AddValue(LevelBasedValue.perLevel(0.5F)),
                        LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.DIRECT_ATTACKER, net.minecraft.advancements.criterion.EntityPredicate.Builder.entity().of(entityTypes, EntityTypeTags.ARROWS).build()
                        )
                )
                .withEffect(
                        EnchantmentEffectComponents.POST_ATTACK,
                        EnchantmentTarget.ATTACKER,
                        EnchantmentTarget.VICTIM,
                        new ApplyMobEffect(
                                HolderSet.direct(MobEffects.WITHER),
                                LevelBasedValue.constant(1.5F),
                                LevelBasedValue.perLevel(1.5F, 0.5F),
                                LevelBasedValue.constant(1.0F),
                                LevelBasedValue.constant(1.0F)
                        ),
                        AllOfCondition.allOf(
                                LootItemEntityPropertyCondition.hasProperties(
                                        LootContext.EntityTarget.DIRECT_ATTACKER, net.minecraft.advancements.criterion.EntityPredicate.Builder.entity().of(entityTypes, EntityTypeTags.ARROWS).build()
                                ),
                                LootItemRandomChanceCondition.randomChance(EnchantmentLevelProvider.forEnchantmentLevel(LevelBasedValue.perLevel(0.15F)))
                        )

                )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.POWER_EXCLUSIVE))
        );

        // register Projectile Protection EX
        register(entries, EXEnchantmentEffects.PROJECTILE_PROTECTION_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        4,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(3, 6),
                                        // same fields as above but for max cost
                                        Enchantment.dynamicCost(9, 6),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.ARMOR
                                )
                        )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.PROJECTILE_PROTECTION_EXCLUSIVE))
                .withEffect(
                        EnchantmentEffectComponents.DAMAGE_PROTECTION,
                        new AddValue(LevelBasedValue.perLevel(2.0F)),
                        AnyOfCondition.anyOf(
                                DamageSourceCondition.hasDamageSource(
                                        DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE)).tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))
                                ),
                                DamageSourceCondition.hasDamageSource( // sonic boom damage reduction
                                        DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.BYPASSES_ENCHANTMENTS)).tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))
                                )
                        )

                )
                .withEffect( // reduces generic knockback
                        EnchantmentEffectComponents.ATTRIBUTES,
                        new EnchantmentAttributeEffect(
                                Identifier.withDefaultNamespace("enchantment.projectile_protection_ex"),
                                Attributes.KNOCKBACK_RESISTANCE,
                                LevelBasedValue.perLevel(0.15F),
                                AttributeModifier.Operation.ADD_VALUE
                        )
                )
        );

        // register Protection EX
        register(entries, EXEnchantmentEffects.PROTECTION_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        4,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(1, 11),
                                        // same fields as above but for max cost
                                        Enchantment.dynamicCost(12, 21),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.ARMOR
                                )
                        )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.PROTECTION_EXCLUSIVE))
                .withEffect(
                        EnchantmentEffectComponents.DAMAGE_PROTECTION,
                        new AddValue(LevelBasedValue.perLevel(1.0F)),
                        DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY)))
                )
                .withEffect(
                        EnchantmentEffectComponents.POST_ATTACK,
                        EnchantmentTarget.VICTIM,
                        EnchantmentTarget.ATTACKER,
                        AllOf.entityEffects(
                                new ApplyMobEffect(
                                        HolderSet.direct(MobEffects.WEAKNESS),
                                        LevelBasedValue.constant(1.5F),
                                        LevelBasedValue.perLevel(2.5F, 0.5F),
                                        LevelBasedValue.perLevel(1.0F),
                                        LevelBasedValue.perLevel(1.0F)
                                )
                        ),
                        LootItemRandomChanceCondition.randomChance(EnchantmentLevelProvider.forEnchantmentLevel(LevelBasedValue.perLevel(0.15F)))
                )
        );

        // register Punch EX
        register(entries, EXEnchantmentEffects.PUNCH_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.BOW_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        2,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(12, 20),
                                        // same fields as above but for max cost
                                        Enchantment.dynamicCost(37, 20),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.MAINHAND
                                )
                        )
                .withEffect(
                        EnchantmentEffectComponents.KNOCKBACK,
                        new AddValue(LevelBasedValue.perLevel(1.0F)),
                        LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.DIRECT_ATTACKER, net.minecraft.advancements.criterion.EntityPredicate.Builder.entity().of(entityTypes, EntityTypeTags.ARROWS).build()
                        )
                )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.PUNCH_EXCLUSIVE))
                .withEffect(
                        EnchantmentEffectComponents.POST_ATTACK,
                        EnchantmentTarget.ATTACKER,
                        EnchantmentTarget.VICTIM,
                        new KnockbackEXEffect(LevelBasedValue.perLevel(0.4f, 0.2f)),
                        LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.DIRECT_ATTACKER, net.minecraft.advancements.criterion.EntityPredicate.Builder.entity().of(entityTypes, EntityTypeTags.ARROWS).build()
                        )
                )
        );

        // register Quick Charge EX
        register(entries, EXEnchantmentEffects.QUICK_CHARGE_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        5,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(12, 20),
                                        // same fields as above but for max cost
                                        Enchantment.constantCost(50),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.MAINHAND,
                                        EquipmentSlotGroup.OFFHAND
                                )
                        )
                .withSpecialEffect(EnchantmentEffectComponents.CROSSBOW_CHARGE_TIME, new AddValue(LevelBasedValue.perLevel(-0.25F)))
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.QUICK_CHARGE_EXCLUSIVE))
                .withSpecialEffect(
                        EnchantmentEffectComponents.CROSSBOW_CHARGING_SOUNDS,
                        List.of(
                                new CrossbowItem.ChargingSounds(Optional.of(SoundEvents.CROSSBOW_QUICK_CHARGE_1), Optional.empty(), Optional.of(SoundEvents.CROSSBOW_LOADING_END)),
                                new CrossbowItem.ChargingSounds(Optional.of(SoundEvents.CROSSBOW_QUICK_CHARGE_2), Optional.empty(), Optional.of(SoundEvents.CROSSBOW_LOADING_END)),
                                new CrossbowItem.ChargingSounds(Optional.of(SoundEvents.CROSSBOW_QUICK_CHARGE_3), Optional.empty(), Optional.of(SoundEvents.CROSSBOW_LOADING_END))
                        )
                )
                .withEffect(
                        EnchantmentEffectComponents.PROJECTILE_SPAWNED,
                        new Ignite(LevelBasedValue.constant(100.0F))
                )
        );

        // register Respiration EX
        register(entries, EXEnchantmentEffects.RESPIRATION_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        3,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(10, 10),
                                        // same fields as above but for max cost
                                        Enchantment.dynamicCost(40, 10),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.HEAD
                                )
                        )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.RESPIRATION_EXCLUSIVE))
                .withEffect(
                        EnchantmentEffectComponents.ATTRIBUTES,
                        new EnchantmentAttributeEffect(
                                Identifier.withDefaultNamespace("enchantment.respiration_ex"),
                                Attributes.OXYGEN_BONUS,
                                LevelBasedValue.perLevel(1.0F),
                                AttributeModifier.Operation.ADD_VALUE
                        )
                )
                .withEffect(
                        EnchantmentEffectComponents.TICK,
                        new RespirationEXEffect(LevelBasedValue.constant(1.0f))
                )
        );

        // register Riptide EX
        register(entries, EXEnchantmentEffects.RIPTIDE_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        3,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(17, 7),
                                        // same fields as above but for max cost
                                        Enchantment.constantCost(50),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.HAND
                                )
                        )
                        .withEffect(
                                EnchantmentEffectComponents.ATTRIBUTES,
                                new EnchantmentAttributeEffect(
                                        Identifier.withDefaultNamespace("enchantment.riptide_ex"),
                                        Attributes.OXYGEN_BONUS,
                                        LevelBasedValue.perLevel(1.0F),
                                        AttributeModifier.Operation.ADD_VALUE
                                )
                        )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.RIPTIDE_EXCLUSIVES))
                .withSpecialEffect(EnchantmentEffectComponents.TRIDENT_SPIN_ATTACK_STRENGTH, new AddValue(LevelBasedValue.perLevel(1.5F, 0.75F)))
                .withSpecialEffect(
                        EnchantmentEffectComponents.TRIDENT_SOUND, List.of(SoundEvents.TRIDENT_RIPTIDE_1, SoundEvents.TRIDENT_RIPTIDE_2, SoundEvents.TRIDENT_RIPTIDE_3)
                )
        );

        // register Soul Speed EX
        // builder def
        net.minecraft.advancements.criterion.EntityPredicate.Builder soulSpeedBuilder = net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                .periodicTick(5)
                .flags(net.minecraft.advancements.criterion.EntityFlagsPredicate.Builder.flags().setIsFlying(false).setOnGround(true))
                .moving(MovementPredicate.horizontalSpeed(MinMaxBounds.Doubles.atLeast(1.0E-5F)))
                .movementAffectedBy(
                        net.minecraft.advancements.criterion.LocationPredicate.Builder.location()
                                .setBlock(net.minecraft.advancements.criterion.BlockPredicate.Builder.block().of(blocks, BlockTags.SOUL_SPEED_BLOCKS))
                );
        register(entries, EXEnchantmentEffects.SOUL_SPEED_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
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
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.SOUL_SPEED_EXCLUSIVE))
                .withEffect(
                        EnchantmentEffectComponents.LOCATION_CHANGED,
                        new EnchantmentAttributeEffect(
                                Identifier.withDefaultNamespace("enchantment.soul_speed"),
                                Attributes.MOVEMENT_SPEED,
                                LevelBasedValue.perLevel(0.0405F, 0.0105F),
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        AllOfCondition.allOf(
                                InvertedLootItemCondition.invert(
                                        LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS,
                                                net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                                                        .vehicle(net.minecraft.advancements.criterion.EntityPredicate.Builder.entity())
                                        )
                                ),
                                AnyOfCondition.anyOf(
                                        AllOfCondition.allOf(
                                                EnchantmentActiveCheck.enchantmentActiveCheck(),
                                                LootItemEntityPropertyCondition.hasProperties(
                                                        LootContext.EntityTarget.THIS,
                                                        net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                                                                .flags(net.minecraft.advancements.criterion.EntityFlagsPredicate.Builder.flags().setIsFlying(false))
                                                ),
                                                AnyOfCondition.anyOf(
                                                        LootItemEntityPropertyCondition.hasProperties(
                                                                LootContext.EntityTarget.THIS,
                                                                net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                                                                        .movementAffectedBy(
                                                                                net.minecraft.advancements.criterion.LocationPredicate.Builder.location()
                                                                                        .setBlock(net.minecraft.advancements.criterion.BlockPredicate.Builder.block().of(blocks, BlockTags.SOUL_SPEED_BLOCKS))
                                                                        )
                                                        ),
                                                        LootItemEntityPropertyCondition.hasProperties(
                                                                LootContext.EntityTarget.THIS,
                                                                net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                                                                        .flags(net.minecraft.advancements.criterion.EntityFlagsPredicate.Builder.flags().setOnGround(false))
                                                                        .build()
                                                        )
                                                )
                                        ),
                                        AllOfCondition.allOf(
                                                EnchantmentActiveCheck.enchantmentInactiveCheck(),
                                                LootItemEntityPropertyCondition.hasProperties(
                                                        LootContext.EntityTarget.THIS,
                                                        net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                                                                .movementAffectedBy(
                                                                        net.minecraft.advancements.criterion.LocationPredicate.Builder.location()
                                                                                .setBlock(net.minecraft.advancements.criterion.BlockPredicate.Builder.block().of(blocks, BlockTags.SOUL_SPEED_BLOCKS))
                                                                )
                                                                .flags(net.minecraft.advancements.criterion.EntityFlagsPredicate.Builder.flags().setIsFlying(false))
                                                )
                                        )
                                )
                        )
                )
                .withEffect(
                        EnchantmentEffectComponents.LOCATION_CHANGED,
                        new EnchantmentAttributeEffect(
                                Identifier.withDefaultNamespace("enchantment.soul_speed"),
                                Attributes.MOVEMENT_EFFICIENCY,
                                LevelBasedValue.constant(1.0F),
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.THIS,
                                net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                                        .movementAffectedBy(
                                                net.minecraft.advancements.criterion.LocationPredicate.Builder.location()
                                                        .setBlock(net.minecraft.advancements.criterion.BlockPredicate.Builder.block().of(blocks, BlockTags.SOUL_SPEED_BLOCKS))
                                        )
                        )
                )
                .withEffect(
                        EnchantmentEffectComponents.TICK,
                        new SpawnParticlesEffect(
                                ParticleTypes.SOUL,
                                SpawnParticlesEffect.inBoundingBox(),
                                SpawnParticlesEffect.offsetFromEntityPosition(0.1F),
                                SpawnParticlesEffect.movementScaled(-0.2F),
                                SpawnParticlesEffect.fixedVelocity(ConstantFloat.of(0.1F)),
                                ConstantFloat.of(1.0F)
                        ),
                        LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, soulSpeedBuilder)
                )
                .withEffect(
                        EnchantmentEffectComponents.TICK,
                        new PlaySoundEffect(List.of(SoundEvents.SOUL_ESCAPE), ConstantFloat.of(0.6F), UniformFloat.of(0.6F, 1.0F)),
                        AllOfCondition.allOf(
                                LootItemRandomChanceCondition.randomChance(0.35F), LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, soulSpeedBuilder)
                        )
                )
        );

        // register Sweeping Edge EX
        register(entries, EXEnchantmentEffects.SWEEPING_EDGE_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.SWEEPING_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        3,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(5, 9),
                                        // same fields as above but for max cost
                                        Enchantment.dynamicCost(20, 9),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.MAINHAND
                                )
                        )
                .withEffect(
                        EnchantmentEffectComponents.ATTRIBUTES,
                        new EnchantmentAttributeEffect(
                                Identifier.withDefaultNamespace("enchantment.sweeping_edge_ex"),
                                Attributes.SWEEPING_DAMAGE_RATIO,
                                new LevelBasedValue.Fraction(LevelBasedValue.perLevel(1.0F), LevelBasedValue.perLevel(2.0F, 1.0F)),
                                AttributeModifier.Operation.ADD_VALUE
                        )
                )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.SWEEPING_EDGE_EXCLUSIVE))
                .withEffect(
                        EnchantmentEffectComponents.DAMAGE,
                        new AddValue(LevelBasedValue.perLevel(1.5f, 1.5f)),
                        LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity()
                                        .entityType(
                                                EntityTypePredicate.of(
                                                        entityTypes,
                                                        EXMobTagProvider.FLYING_MOBS
                                                )
                                        )
                        )
                )
        );

        // register Swift Sneak EX
        register(entries, EXEnchantmentEffects.SWIFT_SNEAK_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        5,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(25, 25),
                                        // same fields as above but for max cost
                                        Enchantment.dynamicCost(75, 25),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.LEGS
                                )
                        )
                .withEffect(
                        EnchantmentEffectComponents.ATTRIBUTES,
                        new EnchantmentAttributeEffect(
                                Identifier.withDefaultNamespace("enchantment.swift_sneak_ex"),
                                Attributes.SNEAKING_SPEED,
                                LevelBasedValue.perLevel(0.15F),
                                AttributeModifier.Operation.ADD_VALUE
                        )
                )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.SWIFT_SNEAK_EXCLUSIVE))
                .withEffect(
                        EnchantmentEffectComponents.ATTRIBUTES,
                        new EnchantmentAttributeEffect(
                                Identifier.withDefaultNamespace("enchantment.swift_sneak_ex"),
                                Attributes.STEP_HEIGHT,
                                LevelBasedValue.constant(0.5F),
                                AttributeModifier.Operation.ADD_VALUE
                        )
                )
        );

        // register Thorns EX
        register(entries, EXEnchantmentEffects.THORNS_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        3,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(10, 20),
                                        // same fields as above but for max cost
                                        Enchantment.dynamicCost(60, 20),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.ARMOR
                                )
                        )
                .withEffect(
                        EnchantmentEffectComponents.POST_ATTACK,
                        EnchantmentTarget.VICTIM,
                        EnchantmentTarget.ATTACKER,
                        AllOf.entityEffects(
                                new DamageEntity(LevelBasedValue.perLevel(1.0F, 1.0F), LevelBasedValue.perLevel(5.0F, 1.0F), damageTypes.getOrThrow(DamageTypes.THORNS)),
                                new ChangeItemDamage(LevelBasedValue.constant(1.0F))
                        ),
                        LootItemRandomChanceCondition.randomChance(EnchantmentLevelProvider.forEnchantmentLevel(LevelBasedValue.perLevel(0.25F)))
                )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.THORNS_EXCLUSIVE))
        );

        // register Unbreaking EX
        register(entries, EXEnchantmentEffects.UNBREAKING_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.DURABILITY_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        5,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(5, 8),
                                        // same fields as above but for max cost
                                        Enchantment.dynamicCost(55, 8),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.ANY
                                )
                        )
                .withEffect(
                        EnchantmentEffectComponents.ITEM_DAMAGE,
                        new RemoveBinomial(new LevelBasedValue.Fraction(LevelBasedValue.perLevel(2.0F), LevelBasedValue.perLevel(10.0F, 2.5F))),
                        MatchTool.toolMatches(net.minecraft.advancements.criterion.ItemPredicate.Builder.item().of(items, ItemTags.ARMOR_ENCHANTABLE))
                )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.UNBREAKING_EXCLUSIVE))
                .withEffect(
                        EnchantmentEffectComponents.ITEM_DAMAGE,
                        new RemoveBinomial(new LevelBasedValue.Fraction(LevelBasedValue.perLevel(1.0F), LevelBasedValue.perLevel(2.0F, 1.0F))),
                        InvertedLootItemCondition.invert(
                                MatchTool.toolMatches(net.minecraft.advancements.criterion.ItemPredicate.Builder.item().of(items, ItemTags.ARMOR_ENCHANTABLE))
                        )
                )
                .withEffect(
                        EnchantmentEffectComponents.POST_ATTACK,
                        EnchantmentTarget.ATTACKER,
                        EnchantmentTarget.ATTACKER,
                        new SummonEntityEffect(
                                HolderSet.direct(EntityType.EXPERIENCE_ORB.builtInRegistryHolder()),
                                false
                        ),
                        AllOfCondition.allOf(
                                LootItemRandomChanceCondition.randomChance(EnchantmentLevelProvider.forEnchantmentLevel(LevelBasedValue.perLevel(0.15F)))
                        )
                )
                .withEffect(
                        EnchantmentEffectComponents.POST_ATTACK,
                        EnchantmentTarget.VICTIM,
                        EnchantmentTarget.VICTIM,
                        new SummonEntityEffect(
                                HolderSet.direct(EntityType.EXPERIENCE_ORB.builtInRegistryHolder()),
                                false
                        ),
                        AllOfCondition.allOf(
                                LootItemRandomChanceCondition.randomChance(EnchantmentLevelProvider.forEnchantmentLevel(LevelBasedValue.perLevel(0.15F)))
                        )
                )
        );

        // register Wind Burst EX
        register(entries, EXEnchantmentEffects.WIND_BURST_EX, Enchantment.enchantment(
                                Enchantment.definition(
                                        // which items can be enchanted
                                        items.getOrThrow(ItemTags.DURABILITY_ENCHANTABLE),
                                        // weight of showing up in enchantment table
                                        1,
                                        // enchantment max level
                                        3,
                                        // base cost for level 1 of the enchantment, and min levels required for something higher
                                        Enchantment.dynamicCost(15, 9),
                                        // same fields as above but for max cost
                                        Enchantment.dynamicCost(65, 9),
                                        // anvil cost
                                        5,
                                        // valid slots
                                        EquipmentSlotGroup.MAINHAND
                                )
                        )
                .exclusiveWith(enchantments.getOrThrow(EXEnchantmentTagProvider.WIND_BURST_EXCLUSIVE))
                .withEffect(
                        EnchantmentEffectComponents.POST_ATTACK,
                        EnchantmentTarget.ATTACKER,
                        EnchantmentTarget.ATTACKER,
                        AllOf.entityEffects(
                                new ExplodeEffect(
                                        false,
                                        Optional.empty(),
                                        Optional.of(LevelBasedValue.lookup(List.of(1.2F, 1.75F, 2.2F), LevelBasedValue.perLevel(1.5F, 0.35F))),
                                        blocks.get(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity()),
                                        Vec3.ZERO,
                                        LevelBasedValue.constant(3.5F),
                                        false,
                                        Level.ExplosionInteraction.TRIGGER,
                                        ParticleTypes.GUST_EMITTER_SMALL,
                                        ParticleTypes.GUST_EMITTER_LARGE,
                                        WeightedList.of(),
                                        SoundEvents.WIND_CHARGE_BURST
                                ),
                                new ApplyMobEffect(
                                        HolderSet.direct(MobEffects.STRENGTH),
                                        LevelBasedValue.constant(10.0F),
                                        LevelBasedValue.constant(10.0F),
                                        LevelBasedValue.perLevel(1.0F),
                                        LevelBasedValue.perLevel(1.0F)
                                ),
                                new ApplyMobEffect(
                                        HolderSet.direct(MobEffects.RESISTANCE),
                                        LevelBasedValue.constant(10.0F),
                                        LevelBasedValue.constant(10.0F),
                                        LevelBasedValue.perLevel(1.0F),
                                        LevelBasedValue.perLevel(1.0F)
                                )
                        ),
                        LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.DIRECT_ATTACKER,
                                net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                                        .flags(net.minecraft.advancements.criterion.EntityFlagsPredicate.Builder.flags().setIsFlying(false))
                                        .moving(MovementPredicate.fallDistance(MinMaxBounds.Doubles.atLeast(1.5)))
                        )
                )
                .withEffect(
                        EnchantmentEffectComponents.DAMAGE_PROTECTION,
                        new AddValue(LevelBasedValue.perLevel(1.5F)),
                        DamageSourceCondition.hasDamageSource(
                                DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_FALL)).tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))
                        )
                )
        );
    }

    /// Register function.
    private void register(Entries entries, ResourceKey<Enchantment> key, Enchantment.Builder builder, ResourceCondition... resourceConditions) {
        entries.add(key, builder.build(key.identifier()), resourceConditions);
    }

    @Override
    public @NotNull String getName() {
        return "EXEnchantmentGenerator";
    }
}
