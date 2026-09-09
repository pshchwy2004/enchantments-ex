package com.pshchwy.enex.mixin;

import com.pshchwy.enex.enchantment.EXEnchantmentEffects;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(MatchTool.class)
public class MatchToolMixin {
    @Shadow
    @Final
    private Optional<ItemPredicate> predicate;

    @Inject(method = "test(Lnet/minecraft/world/level/storage/loot/LootContext;)Z", at = @At(value = "HEAD"), cancellable = true)
    private void overrideTest(final LootContext context,  final CallbackInfoReturnable<Boolean> cir) {
        if (predicate.isPresent() && isSilkTouchPredicate(context, predicate.get())) {
            ItemInstance tool = context.getOptionalParameter(LootContextParams.TOOL);
            Entity entity = context.getOptionalParameter(LootContextParams.THIS_ENTITY);
            if (tool != null && entity instanceof Player player) {
                HolderLookup.RegistryLookup<Enchantment> lookup = player.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
                Holder<Enchantment> silkTouchExHolder = lookup.getOrThrow(EXEnchantmentEffects.SILK_TOUCH_EX);

                int silkTouchExLevel = EnchantmentHelper.getItemEnchantmentLevel(silkTouchExHolder, tool);

                if (silkTouchExLevel > 0) {
                    // Return true ONLY if player is crouching; otherwise false (allowing Fortune EX / regular drops)
                    cir.setReturnValue(player.isCrouching());
                }
            }
        }
    }

    /// Helper method to test if the MatchTool predicate requires Silk Touch
    @Unique
    private boolean isSilkTouchPredicate(LootContext context, ItemPredicate itemPredicate) {
        Entity entity = context.getOptionalParameter(LootContextParams.THIS_ENTITY);
        if (entity == null) return false;

        HolderLookup.RegistryLookup<Enchantment> lookup = entity.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> vanillaSilkTouch = lookup.getOrThrow(Enchantments.SILK_TOUCH);

        // Create a dummy item with ONLY vanilla Silk Touch to probe the predicate
        ItemStack testStick = new ItemStack(Items.STICK);
        testStick.enchant(vanillaSilkTouch, 1);

        // If the predicate matches the dummy Silk Touch item, this MatchTool is looking for Silk Touch!
        return itemPredicate.test(testStick);
    }
}
