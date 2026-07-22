package com.pshchwy.enex.mixin;

import com.pshchwy.enex.enchantment.EXEnchantmentEffects;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/// Trident mixin meant to change its behavior when enchanted with Riptide EX, allowing the player to use it even when not raining.
@Mixin(TridentItem.class) @SuppressWarnings("unused")
abstract class TridentItemMixin {

    @Shadow public abstract int getUseDuration(ItemStack itemStack, LivingEntity livingEntity);


    @Inject(method = "use", at = @At(value = "HEAD"), cancellable = true)
    private void modUse(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (EnchantmentHelper.getTridentSpinAttackStrength(itemStack, player) > 0.0F && hasEnchantment(itemStack)) {
            player.startUsingItem(interactionHand);
            cir.setReturnValue(InteractionResult.CONSUME);
        }
    }

    @Inject(method = "releaseUsing", at = @At(value = "HEAD"), cancellable = true)
    private void modReleaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int i, CallbackInfoReturnable<Boolean> ci) {
        if (livingEntity instanceof Player player) {
            int j = this.getUseDuration(itemStack, livingEntity) - i;
            if (j < 10) {
                ci.setReturnValue(false);
            } else {
                float f = EnchantmentHelper.getTridentSpinAttackStrength(itemStack, player);
                if (f > 0.0F && !player.isInWaterOrRain() && !hasEnchantment(itemStack)) {
                    ci.setReturnValue(false);
                } else if (itemStack.nextDamageWillBreak()) {
                    ci.setReturnValue(false);
                } else {
                    Holder<SoundEvent> holder = EnchantmentHelper.pickHighestLevel(itemStack, EnchantmentEffectComponents.TRIDENT_SOUND).orElse(SoundEvents.TRIDENT_THROW);
                    if (level instanceof ServerLevel serverLevel) {
                        itemStack.hurtWithoutBreaking(1, player);
                        if (f == 0.0F) {
                            ThrownTrident thrownTrident = Projectile.spawnProjectileFromRotation(ThrownTrident::new, serverLevel, itemStack, player, 0.0F, 2.5F, 1.0F);
                            if (player.hasInfiniteMaterials()) {
                                thrownTrident.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                            } else {
                                player.getInventory().removeItem(itemStack);
                            }

                            level.playSound(null, thrownTrident, holder.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                            ci.setReturnValue(true);
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get((TridentItem) (Object) this));
                    if (f > 0.0F) {
                        float g = player.getYRot();
                        float h = player.getXRot();
                        float k = -Mth.sin(g * ((float)Math.PI / 180F)) * Mth.cos(h * ((float)Math.PI / 180F));
                        float l = -Mth.sin(h * ((float)Math.PI / 180F));
                        float m = Mth.cos(g * ((float)Math.PI / 180F)) * Mth.cos(h * ((float)Math.PI / 180F));
                        float n = Mth.sqrt(k * k + l * l + m * m);
                        k *= f / n;
                        l *= f / n;
                        m *= f / n;
                        player.push(k, l, m);
                        player.startAutoSpinAttack(20, 8.0F, itemStack);
                        if (player.onGround()) {
                            float o = 1.1999999F;
                            player.move(MoverType.SELF, new Vec3(0.0F, 1.1999999F, 0.0F));
                        }

                        level.playSound(null, player, holder.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                        ci.setReturnValue(true);
                    } else {
                        ci.setReturnValue(false);
                    }
                }
            }
        } else {
            ci.setReturnValue(false);
        }
    }

    @Unique
    private boolean hasEnchantment(ItemStack stack) {
        return stack.getEnchantments().toString().contains(EXEnchantmentEffects.RIPTIDE_EX.location().getPath());
    }
}
