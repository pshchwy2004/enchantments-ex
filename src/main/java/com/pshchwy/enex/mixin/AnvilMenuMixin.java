package com.pshchwy.enex.mixin;

import com.pshchwy.enex.enchantment.EXEnchantmentMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

/// This is an Anvil Mixin, changing the Anvil's behavior to be able to override Vanilla enchantments with their EX variant (right item only). It will overwrite the left Vanilla enchantment but inherit its level if the Vanilla enchantment was higher. If the EX enchantment already exists, then it is ignored
@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin {

    /**
     * This method is essentially a copy of AnvilMenu.createResult(), but with logic inserted in the middle to change the item enchantment's logic.
     * @param ci: the callback info
     */
    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    private void overrideCreateResult(CallbackInfo ci) {
        ItemStack itemStack = ((AnvilMenuAccessor) this).enex$getInputSlots().getItem(0); // input left
        ((AnvilMenuAccessor) this).enex$getCost().set(1);
        int i = 0;
        long l = 0L;
        int j = 0;
        if (!itemStack.isEmpty() && EnchantmentHelper.canStoreEnchantments(itemStack)) {
            ItemStack itemStack2 = itemStack.copy(); // copy of left input
            ItemStack itemStack3 = ((AnvilMenuAccessor) this).enex$getInputSlots().getItem(1); // right input
            ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(EnchantmentHelper.getEnchantmentsForCrafting(itemStack2));
            l += (long) itemStack.getOrDefault(DataComponents.REPAIR_COST, 0) + itemStack3.getOrDefault(DataComponents.REPAIR_COST, 0);

            ((AnvilMenuAccessor) this).enex$setRepairItemCountCost(0);
            if (!itemStack3.isEmpty()) {
                boolean bl = itemStack3.has(DataComponents.STORED_ENCHANTMENTS);
                if (itemStack2.isDamageableItem() && itemStack2.getItem().isValidRepairItem(itemStack, itemStack3)) {
                    int k = Math.min(itemStack2.getDamageValue(), itemStack2.getMaxDamage() / 4);
                    if (k <= 0) {
                        ((AnvilMenuAccessor) this).enex$getResultSlots().setItem(0, ItemStack.EMPTY);
                        ((AnvilMenuAccessor) this).enex$getCost().set(0);
                        return;
                    }

                    int m;
                    for (m = 0; k > 0 && m < itemStack3.getCount(); m++) {
                        int n = itemStack2.getDamageValue() - k;
                        itemStack2.setDamageValue(n);
                        i++;
                        k = Math.min(itemStack2.getDamageValue(), itemStack2.getMaxDamage() / 4);
                    }

                    ((AnvilMenuAccessor) this).enex$setRepairItemCountCost(m);
                } else {
                    if (!bl && (!itemStack2.is(itemStack3.getItem()) || !itemStack2.isDamageableItem())) {
                        ((AnvilMenuAccessor) this).enex$getResultSlots().setItem(0, ItemStack.EMPTY);
                        ((AnvilMenuAccessor) this).enex$getCost().set(0);
                        return;
                    }

                    if (itemStack2.isDamageableItem() && !bl) {
                        int k = itemStack.getMaxDamage() - itemStack.getDamageValue();
                        int m = itemStack3.getMaxDamage() - itemStack3.getDamageValue();
                        int n = m + itemStack2.getMaxDamage() * 12 / 100;
                        int o = k + n;
                        int p = itemStack2.getMaxDamage() - o;
                        if (p < 0) {
                            p = 0;
                        }

                        if (p < itemStack2.getDamageValue()) {
                            itemStack2.setDamageValue(p);
                            i += 2;
                        }
                    }

                    ItemEnchantments itemEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(itemStack3);
                    boolean bl2 = false;
                    boolean bl3 = false;



                    for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) { // looping through all enchants in right input
                        Holder<Enchantment> holder = entry.getKey();
                        int q = mutable.getLevel(holder);
                        int r = entry.getIntValue(); // r calculates final enchantment level
                        r = q == r ? r + 1 : Math.max(r, q);
                        Enchantment enchantment = holder.value();
                        boolean bl4 = enchantment.canEnchant(itemStack);
                        if (((AnvilMenuAccessor) this).enex$getPlayer().getAbilities().instabuild || itemStack.is(Items.ENCHANTED_BOOK)) {
                            bl4 = true;
                        }

                        // override code
                        // EX upgrade check
                        Holder<Enchantment> vanillaToPurge = null;

                        Optional<ResourceKey<Enchantment>> rightKeyOpt = holder.unwrapKey();
                        if (rightKeyOpt.isPresent()) {
                            ResourceKey<Enchantment> rightKey = rightKeyOpt.get();

                            // test left item enchants
                            for (Holder<Enchantment> holder2 : mutable.keySet()) {
                                Optional<ResourceKey<Enchantment>> leftKeyOpt = holder2.unwrapKey();
                                if (leftKeyOpt.isPresent()) {
                                    ResourceKey<Enchantment> leftKey = leftKeyOpt.get();
                                    if (EXEnchantmentMap.isUpgradable(leftKey) && EXEnchantmentMap.getUpgrade(leftKey).equals(rightKey)) {
                                        // left key select
                                        vanillaToPurge = holder2;
                                        int holder2Level = mutable.getLevel(holder2);

                                        // Recalculate r
                                        r = Math.max(holder2Level, entry.getIntValue());
                                        break;
                                    }
                                }
                            }
                        }

                        // 3. Safely purge the vanilla enchantment outside the loop if found
                        if (vanillaToPurge != null) {
                            mutable.set(vanillaToPurge, 0);
                        }
                        // --- END ENEX PRE-PROCESSING ---

                        for (Holder<Enchantment> holder2 : mutable.keySet()) { // tests all enchants in left input
                            if (!holder2.equals(holder) && !Enchantment.areCompatible(holder, holder2)) {
                                bl4 = false;
                                i++;
                            }
                        }

                        if (!bl4) {
                            bl3 = true;
                        } else { // sets the enchantment
                            bl2 = true;
                            if (r > enchantment.getMaxLevel()) {
                                r = enchantment.getMaxLevel();
                            }

                            mutable.set(holder, r); // adds the enchantment
                            int s = enchantment.getAnvilCost();
                            if (bl) {
                                s = Math.max(1, s / 2);
                            }

                            i += s * r;
                            if (itemStack.getCount() > 1) {
                                i = 40;
                            }
                        }
                    }

                    if (bl3 && !bl2) {
                        ((AnvilMenuAccessor) this).enex$getResultSlots().setItem(0, ItemStack.EMPTY);
                        ((AnvilMenuAccessor) this).enex$getCost().set(0);
                        ci.cancel();
                    }
                }
            }

            if (((AnvilMenuAccessor) this).enex$getItemName() != null && !StringUtil.isBlank(((AnvilMenuAccessor) this).enex$getItemName())) {
                if (!((AnvilMenuAccessor) this).enex$getItemName().equals(itemStack.getHoverName().getString())) {
                    j = 1;
                    i += j;
                    itemStack2.set(DataComponents.CUSTOM_NAME, Component.literal(((AnvilMenuAccessor) this).enex$getItemName()));
                }
            } else if (itemStack.has(DataComponents.CUSTOM_NAME)) {
                j = 1;
                i += j;
                itemStack2.remove(DataComponents.CUSTOM_NAME);
            }

            int t = (int) Mth.clamp(l + i, 0L, 2147483647L);
            ((AnvilMenuAccessor) this).enex$getCost().set(t);
            if (i <= 0) {
                itemStack2 = ItemStack.EMPTY;
            }

            if (j == i && j > 0 && ((AnvilMenuAccessor) this).enex$getCost().get() >= 40) {
                ((AnvilMenuAccessor) this).enex$getCost().set(39);
            }

            if (((AnvilMenuAccessor) this).enex$getCost().get() >= 40 && !((AnvilMenuAccessor) this).enex$getPlayer().getAbilities().instabuild) {
                itemStack2 = ItemStack.EMPTY;
            }

            if (!itemStack2.isEmpty()) {
                int k = itemStack2.getOrDefault(DataComponents.REPAIR_COST, 0);
                if (k < itemStack3.getOrDefault(DataComponents.REPAIR_COST, 0)) {
                    k = itemStack3.getOrDefault(DataComponents.REPAIR_COST, 0);
                }

                if (j != i || j == 0) {
                    k = AnvilMenu.calculateIncreasedRepairCost(k);
                }

                itemStack2.set(DataComponents.REPAIR_COST, k);
                EnchantmentHelper.setEnchantments(itemStack2, mutable.toImmutable());
            }

            ((AnvilMenuAccessor) this).enex$getResultSlots().setItem(0, itemStack2);
            ((AnvilMenuAccessor) this).enex$broadcastChanges();
        } else {
            ((AnvilMenuAccessor) this).enex$getResultSlots().setItem(0, ItemStack.EMPTY);
            ((AnvilMenuAccessor) this).enex$getCost().set(0);
        }
        ci.cancel(); // replaces the entire anvil mixin LMAO
    }
}