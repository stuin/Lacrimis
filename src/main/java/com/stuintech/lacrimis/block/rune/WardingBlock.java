package com.stuintech.lacrimis.block.rune;

import com.stuintech.lacrimis.block.ModBlocks;
import com.stuintech.lacrimis.init.ModEnchantments;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

public class WardingBlock extends CenterRuneBlock {
    public static final HashMap<Block, Block> wardedMap = new HashMap<>();

    public WardingBlock() {
        super(800, 3);
    }

    @Override
    protected boolean onActivate(World world, BlockPos pos, BlockPos duct, Entity entity, PlayerEntity player) {
        Block block = world.getBlockState(pos).getBlock();
        if(wardedMap.containsKey(block)) {
            world.setBlockState(pos, wardedMap.get(block).getDefaultState());
            return true;
        }
        if(entity instanceof PlayerEntity) {
            boolean added = false;
            for(ItemStack item : entity.getArmorItems())
                if(EnchantmentHelper.getLevel(ModEnchantments.WARDED, item) == 0) {
                    item.addEnchantment(ModEnchantments.WARDED, 1);
                    added = true;
                }
            return added;
        }
        if(entity instanceof ItemEntity) {
            ItemStack stack = ((ItemEntity) entity).getStack();
            if(ModEnchantments.WARDED.isAcceptableItem(stack) && EnchantmentHelper.getLevel(ModEnchantments.WARDED, stack) == 0) {
                stack.addEnchantment(ModEnchantments.WARDED, 1);
                return true;
            }
        }
        return false;
    }

    static {
        //Runic to warded
        wardedMap.put(ModBlocks.runicStone, ModBlocks.wardedStone);
        wardedMap.put(ModBlocks.runicBrick, ModBlocks.wardedBrick);
        wardedMap.put(ModBlocks.runicGlass, ModBlocks.wardedGlass);
        wardedMap.put(ModBlocks.runicLight, ModBlocks.wardedLight);

        //Warded to Runic
        wardedMap.put(ModBlocks.wardedStone, ModBlocks.runicStone);
        wardedMap.put(ModBlocks.wardedBrick, ModBlocks.runicBrick);
        wardedMap.put(ModBlocks.wardedGlass, ModBlocks.runicGlass);
        wardedMap.put(ModBlocks.wardedLight, ModBlocks.runicLight);
    }
}
