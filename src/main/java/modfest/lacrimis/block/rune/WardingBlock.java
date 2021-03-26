package modfest.lacrimis.block.rune;

import modfest.lacrimis.item.Soaked;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import modfest.lacrimis.init.ModBlocks;
import modfest.lacrimis.init.ModEnchantments;

public class WardingBlock extends CenterRuneBlock {
    public WardingBlock() {
        super(800, 3);
    }

    @Override
    protected boolean activate(World world, BlockPos pos, BlockPos duct, Entity entity, PlayerEntity player) {
        if(world.getBlockState(pos).getBlock() == ModBlocks.runeStone) {
            world.setBlockState(pos, ModBlocks.wardedStone.getDefaultState());
            return true;
        }
        if(world.getBlockState(pos).getBlock() == ModBlocks.wardedStone) {
            world.setBlockState(pos, ModBlocks.runeStone.getDefaultState());
            return true;
        }
        if(entity instanceof PlayerEntity) {
            boolean added = false;
            for(ItemStack item : entity.getArmorItems())
                if(item.getItem() instanceof Soaked) {
                    item.addEnchantment(ModEnchantments.WARDED, 1);
                    added = true;
                }
            return added;
        }
        return false;
    }
}
