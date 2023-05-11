package com.stuintech.lacrimis.init;

import com.stuintech.lacrimis.block.entity.NetworkLinkBlock;
import com.stuintech.lacrimis.block.rune.SoulSwapBlock;
import com.stuintech.lacrimis.util.DuctUtil;
import com.stuintech.lacrimis.item.ModItems;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;

public class ModGameRules {
    public static GameRules.Key<GameRules.BooleanRule> TAINT_SPREAD;
    public static GameRules.Key<GameRules.BooleanRule> TOTEM_ANY_SLOT;

    public static void register() {
        TAINT_SPREAD = GameRuleRegistry.register("lacrimisTaintSpread", GameRules.Category.UPDATES, GameRuleFactory.createBooleanRule(true));
        TOTEM_ANY_SLOT = GameRuleRegistry.register("lacrimisTotemAnySlot", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));

        ServerLivingEntityEvents.ALLOW_DEATH.register(ModGameRules::allowDeath);
    }

    public static boolean allowDeath(LivingEntity entity, DamageSource damageSource, float damageAmount) {
        ItemStack itemStack = null;
        if(entity instanceof PlayerEntity player) {
            PlayerInventory inventory = player.getInventory();

            for(Hand hand : Hand.values()) {
                ItemStack stack = player.getStackInHand(hand);
                if(stack.isOf(ModItems.soulTotem) || stack.isOf(Items.TOTEM_OF_UNDYING)) {
                    itemStack = stack;
                    break;
                }
            }

            //Check for other totem
            if(itemStack == null && player.world.getGameRules().getBoolean(ModGameRules.TOTEM_ANY_SLOT)) {
                for(ItemStack stack : inventory.main) {
                    if(stack.isOf(ModItems.soulTotem) || stack.isOf(Items.TOTEM_OF_UNDYING)) {
                        itemStack = stack;
                        break;
                    }
                }
            }

            //Get Soul Totem destination
            if(itemStack != null && itemStack.isOf(ModItems.soulTotem) && itemStack.hasNbt()) {
                int x = itemStack.getNbt().getInt("X");
                int y = itemStack.getNbt().getInt("Y");
                int z = itemStack.getNbt().getInt("Z");
                BlockPos pos = new BlockPos(x, y, z);
                if(player.world.getBlockState(pos).getBlock() instanceof NetworkLinkBlock &&
                        DuctUtil.locateSink(player.world, pos, new SoulSwapBlock.PlayerContainer(player, itemStack)) != null) {
                    return false;
                }
            }

            //Totem of Undying
            if(itemStack != null && itemStack.isOf(Items.TOTEM_OF_UNDYING)) {
                player.incrementStat(Stats.USED.getOrCreateStat(Items.TOTEM_OF_UNDYING));
                if(player instanceof ServerPlayerEntity)
                    Criteria.USED_TOTEM.trigger((ServerPlayerEntity) player, itemStack);
                itemStack.decrement(1);

                player.setHealth(1.0F);
                player.clearStatusEffects();
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
                player.world.sendEntityStatus(player, (byte) 35);
                return false;
            }
        }
        return true;
    }
}
