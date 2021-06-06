package modfest.lacrimis.block;

import modfest.lacrimis.util.TaintPacket;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class TaintOutputBlock extends FacingBlock implements DuctConnectBlock {
    public TaintOutputBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public boolean canConnectDuctTo(BlockPos pos, WorldAccess world, Direction side) {
        return side != world.getBlockState(pos).get(FACING).getOpposite();
    }

    @Override
    public int extractTears(BlockPos pos, World world, int request, boolean simulate) {
        return 0;
    }

    @Override
    public boolean insert(BlockPos pos, World world, Object value) {
        if(value instanceof TaintPacket && !world.isClient) {
            ((TaintPacket) value).spawn(world, pos.offset(world.getBlockState(pos).get(FACING)));
            return true;
        }
        return false;
    }
}
