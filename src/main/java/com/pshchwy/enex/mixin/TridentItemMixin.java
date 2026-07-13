package com.pshchwy.enex.mixin;

import com.pshchwy.enex.enchantment.EXEnchantmentEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TridentItem.class)
abstract class TridentItemMixin {

    @Shadow public abstract int getUseDuration(ItemStack itemStack, LivingEntity livingEntity);


    @Inject(method = "use", at = @At(value = "HEAD"), cancellable = true)
    private void modUse(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (EnchantmentHelper.getTridentSpinAttackStrength(itemStack, player) > 0.0F && hasEnchantment(itemStack, EXEnchantmentEffects.RIPTIDE_EX)) {
            player.startUsingItem(interactionHand);
            cir.setReturnValue(InteractionResultHolder.consume(itemStack));
        }
    }

    @Inject(method = "releaseUsing", at = @At(value = "HEAD"), cancellable = true)
    private void modReleaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int i, CallbackInfo ci) {
        if (livingEntity instanceof Player player) {
            int j = this.getUseDuration(itemStack, livingEntity) - i;
            if (j >= 10) {
                float f = EnchantmentHelper.getTridentSpinAttackStrength(itemStack, player);
                if (f > 0.0F && !player.isInWaterOrRain() && hasEnchantment(itemStack, EXEnchantmentEffects.RIPTIDE_EX)) {
                    if (!(itemStack.getDamageValue() >= itemStack.getMaxDamage() - 1)) {
                        player.awardStat(Stats.ITEM_USED.get((TridentItem) (Object) this));
                        Holder<SoundEvent> holder = EnchantmentHelper.pickHighestLevel(itemStack, EnchantmentEffectComponents.TRIDENT_SOUND).orElse(SoundEvents.TRIDENT_THROW);

                        itemStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(livingEntity.getUsedItemHand()));
                        float g = player.getYRot();
                        float h = player.getXRot();
                        float k = -Mth.sin(g * (float) (Math.PI / 180.0)) * Mth.cos(h * (float) (Math.PI / 180.0));
                        float l = -Mth.sin(h * (float) (Math.PI / 180.0));
                        float m = Mth.cos(g * (float) (Math.PI / 180.0)) * Mth.cos(h * (float) (Math.PI / 180.0));
                        float n = Mth.sqrt(k * k + l * l + m * m);
                        k *= f / n;
                        l *= f / n;
                        m *= f / n;
                        player.push(k, l, m);
                        player.startAutoSpinAttack(20, 8.0F, itemStack);
                        if (player.onGround()) {
                            float o = 1.1999999F;
                            player.move(MoverType.SELF, new Vec3(0.0, 1.1999999F, 0.0));
                        }

                        level.playSound(null, player, holder.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                        ci.cancel();
                    }
                }
            }
        }
    }

    @Unique
    private boolean hasEnchantment(ItemStack stack, ResourceKey<Enchantment> enchantment) {
        return stack.getEnchantments().toString().contains(enchantment.location().getPath());
    }
}
