package modfest.lacrimis.block.rune;

import modfest.lacrimis.block.DuctConnectBlock;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class DuctRuneBlock extends BasicRuneBlock implements DuctConnectBlock {
    public DuctRuneBlock(int tier) {
        super( tier);
    }

    @Override
    public boolean canConnectDuctTo(BlockPos pos, WorldAccess world, Direction side) {
        return pos.offset(side) != getCenter(world, pos) && side != Direction.UP;
    }

    @Override
    public int extractTears(BlockPos pos, World world, int request, boolean simulate) {
        return 0;
    }

    @Override
    public boolean insert(BlockPos pos, World world, Object value) {
        //Insert to center
        BlockPos center = getTrueCenter(world, pos);
        if(center != null) {
            Block block = world.getBlockState(center).getBlock();
            if(block instanceof DuctConnectBlock)
                return ((DuctConnectBlock) block).insert(center, world, value);
        }
        return false;
    }
}
