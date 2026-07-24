package com.pshchwy.enex.item.custom;

import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Molten Ink item. It is declared as a PotionItem due to the need for it to be brewed with Nether Crystal Fragments, though it poses no positive effect on any user.
 */
public class MoltenInkItem extends PotionItem {
    private static final int DRINK_DURATION = 40;
    public MoltenInkItem(Properties properties) {
        super(properties);
    }

    public int getUseDuration(ItemStack itemStack, LivingEntity livingEntity) {
        return DRINK_DURATION;
    }


    /**
     * Kills the player when they drink it.
     * @param itemStack The ItemStack, namely, the instance of Molten Ink being used.
     * @param level The world.
     * @param livingEntity The entity using the item.
     * @return An ItemStack.
     */
    public @NotNull ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        Player player = livingEntity instanceof Player ? (Player)livingEntity : null;
        if (player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)player, itemStack);
        }

        if (!level.isClientSide()) {
            if (!livingEntity.fireImmune()) {
                livingEntity.igniteForTicks(500);
            }
            livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 500, 2));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 500, 2));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 500, 2));
        }

        if (player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
            itemStack.consume(1, player);
        }

        if (player == null || !player.hasInfiniteMaterials()) {
            if (itemStack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }

            if (player != null) {
                player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
            }
        }

        livingEntity.gameEvent(GameEvent.DRINK);
        return itemStack;
    }

    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {

    }

}
