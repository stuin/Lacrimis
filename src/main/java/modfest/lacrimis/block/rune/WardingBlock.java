package modfest.lacrimis.block.rune;

import modfest.lacrimis.init.ModBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WardingBlock extends CenterRuneBlock {
    public WardingBlock() {
        super(800, 3);
    }

    @Override
    protected boolean activate(World world, BlockPos pos, BlockPos pipe, Entity entity, PlayerEntity player) {
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
