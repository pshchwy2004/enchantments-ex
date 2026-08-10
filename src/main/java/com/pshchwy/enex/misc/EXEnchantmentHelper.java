package com.pshchwy.enex.misc;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class EXEnchantmentHelper {
    public static boolean hasEnchantment(ItemStack stack, ResourceKey<Enchantment> enchantmentKey) {
        return stack.getEnchantments().toString().contains(enchantmentKey.location().getPath());
    }
}
