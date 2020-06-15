package modfest.soulflame.block.rune;

import modfest.soulflame.init.ModBlocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WardingBlock extends CenterRuneBlock {
    public WardingBlock() {
        super(800, 3);
    }

    @Override
    protected boolean activate(World world, BlockPos pos, BlockPos pipe, LivingEntity entity, PlayerEntity player) {
        if(world.getBlockState(pos).getBlock() == ModBlocks.runeStone) {
            world.setBlockState(pos, ModBlocks.wardedStone.getDefaultState());
            return true;
        }
        if(world.getBlockState(pos).getBlock() == ModBlocks.wardedStone) {
            world.setBlockState(pos, ModBlocks.runeStone.getDefaultState());
            return true;
        }
        return false;
    }
}
