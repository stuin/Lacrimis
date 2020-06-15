package modfest.soulflame.block.rune;

import modfest.soulflame.block.BlockConduitConnect;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PipeConnectorBlock extends RuneBlock implements BlockConduitConnect {
    public PipeConnectorBlock(int tier) {
        super( tier);
    }

    @Override
    protected BlockPos getCenter(BlockView world, BlockPos pos, BlockState state) {
        BlockPos center = super.getCenter(world, pos, state);
        if(center != null) {
            //Update pipe location
            int i = (state.get(CENTER) + 4) % 8;
            BlockState state2 = world.getBlockState(center);
            if(world instanceof World && state2.get(CenterRuneBlock.PIPE) != i)
                ((World)world).setBlockState(center, state2.with(CenterRuneBlock.PIPE, i));
        }
        return center;
    }

    @Override
    public boolean canConnectConduitTo(BlockPos pos, BlockView world, Direction side) {
        return pos.offset(side) != getCenter(world, pos) && side != Direction.UP;
    }

    @Override
    public Object extract(BlockPos pos, BlockView world) {
        //Extract from center
        BlockPos center = getCenter(world, pos);
        if(center != null) {
            Block block = world.getBlockState(center).getBlock();
            if(block instanceof BlockConduitConnect)
                return ((BlockConduitConnect) block).extract(center, world);
        }
        return null;
    }

    @Override
    public int extractTears(BlockPos pos, BlockView world, int request, boolean simulate) {
        return 0;
    }

    @Override
    public boolean insert(BlockPos pos, BlockView world, Object value) {
        //Insert to center
        BlockPos center = getCenter(world, pos);
        if(center != null) {
            Block block = world.getBlockState(center).getBlock();
            if(block instanceof BlockConduitConnect)
                return ((BlockConduitConnect) block).insert(center, world, value);
        }
        return false;
    }
}
