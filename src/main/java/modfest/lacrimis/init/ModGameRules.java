package modfest.lacrimis.init;

import modfest.lacrimis.block.entity.NetworkLinkBlock;
import modfest.lacrimis.block.rune.SoulSwapBlock;
import modfest.lacrimis.item.ModItems;
import modfest.lacrimis.util.DuctUtil;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
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
    public static GameRules.Key<GameRules.BooleanRule> TOTEM_ANY_SLOT_SOUL;
    public static GameRules.Key<GameRules.BooleanRule> TOTEM_ANY_SLOT_VANILLA;

    public static void register() {
        TAINT_SPREAD = GameRuleRegistry.register("lacrimisTaintSpread", GameRules.Category.UPDATES, GameRuleFactory.createBooleanRule(true));
        TOTEM_ANY_SLOT_SOUL = GameRuleRegistry.register("lacrimisSoulTotemAnySlot", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));
        TOTEM_ANY_SLOT_VANILLA = GameRuleRegistry.register("lacrimisVanillaTotemAnySlot", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));

        ServerPlayerEvents.ALLOW_DEATH.register(ModGameRules::allowDeath);
    }

    public static boolean allowDeath(ServerPlayerEntity player, DamageSource damageSource, float damageAmount) {
        ItemStack itemStack = null;
        PlayerInventory inventory = player.getInventory();

        for(Hand hand : Hand.values()) {
            ItemStack stack = player.getStackInHand(hand);
            if(stack.isOf(ModItems.soulTotem) || stack.isOf(Items.TOTEM_OF_UNDYING)) {
                itemStack = stack;
                break;
            }
        }

        //Check for other totem
        boolean soulTotem = player.world.getGameRules().getBoolean(ModGameRules.TOTEM_ANY_SLOT_SOUL);
        boolean vanillaTotem = player.world.getGameRules().getBoolean(ModGameRules.TOTEM_ANY_SLOT_VANILLA);
        if(itemStack == null && (soulTotem || vanillaTotem)) {
            for(ItemStack stack : inventory.main) {
                if((soulTotem && stack.isOf(ModItems.soulTotem)) || (vanillaTotem && stack.isOf(Items.TOTEM_OF_UNDYING))) {
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
            Criteria.USED_TOTEM.trigger(player, itemStack);
            itemStack.decrement(1);

            player.setHealth(1.0F);
            player.clearStatusEffects();
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
            player.world.sendEntityStatus(player, (byte)35);
            return false;
        }
        return true;
    }
}
