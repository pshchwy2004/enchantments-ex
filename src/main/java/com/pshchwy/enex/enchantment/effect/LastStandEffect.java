package com.pshchwy.enex.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pshchwy.enex.enchantment.EXEnchantmentEffects;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public record LastStandEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<LastStandEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("amount").forGetter(LastStandEffect::amount)
            ).apply(instance, LastStandEffect::new)
    );
    @Override
    public void apply(@NonNull ServerLevel serverLevel, int enchantmentLevel, @NonNull EnchantedItemInUse item, @NonNull Entity entity, @NonNull Vec3 position) {
        // executes per tick
        // if the item is at or below 25% durability, give the following effects:
        // armor: Resistance 0
        // weapons: Strength 0
        // tools: Haste 0
        // fire protection(EX): Fire Resistance
        // fortune(EX): Luck
        if (entity instanceof Player player && ((double) item.itemStack().getDamageValue()/item.itemStack().getMaxDamage()) >= 0.75) {
            ItemStack itemStack = item.itemStack();
            ItemEnchantments enchants = itemStack.getEnchantments();
            if (itemStack.is(ItemTags.ARMOR_ENCHANTABLE)) {
                player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 100, 0, true, false));
            } else if (itemStack.is(ItemTags.WEAPON_ENCHANTABLE)) {
                player.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 100, 0, true, false));
            } else { // tool
                player.addEffect(new MobEffectInstance(MobEffects.HASTE, 100, 0, true, false));
            }
            if (hasEnchantment(enchants, Enchantments.FIRE_PROTECTION) || hasEnchantment(enchants, EXEnchantmentEffects.FIRE_PROTECTION_EX)) {
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0, true, false));
            }
            if (hasEnchantment(enchants, Enchantments.FORTUNE) || hasEnchantment(enchants, EXEnchantmentEffects.FORTUNE_EX)) {
                player.addEffect(new MobEffectInstance(MobEffects.LUCK, 100, 0, true, false));
            }

        }
    }

    @Override
    public @NonNull MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }

    boolean hasEnchantment(ItemEnchantments itemEnchantments, ResourceKey<Enchantment> enchantment) {
        return itemEnchantments.toString().contains(enchantment.identifier().getPath());
    }
}
