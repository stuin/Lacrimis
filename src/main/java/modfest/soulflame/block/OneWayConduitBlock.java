package modfest.soulflame.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class OneWayConduitBlock extends FacingBlock implements BlockConduitConnect {
    public OneWayConduitBlock(Settings settings) {
        super(settings);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public boolean canConnectConduitTo(BlockPos pos, BlockView world, Direction side) {
        Direction facing = world.getBlockState(pos).get(FACING);
        return side == facing || side == facing.getOpposite();
    }

    @Override
    public Object extract(BlockPos pos, BlockView world) {
        return null;
    }

    @Override
    public int extractTears(BlockPos pos, BlockView world, int request, boolean simulate) {
        return 0;
    }

    @Override
    public boolean insert(BlockPos pos, BlockView world, Object value) {
        return false;
    }
}
