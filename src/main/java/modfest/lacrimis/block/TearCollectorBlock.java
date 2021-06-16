package modfest.lacrimis.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class TearCollectorBlock extends FacingBlock implements DuctConnectBlock {
    private static final VoxelShape[] SHAPES = generateShapes();

    public TearCollectorBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection());
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
        return side == world.getBlockState(pos).get(FACING);
    }

    @Override
    public int extractTears(BlockPos pos, World world, int request, boolean simulate) {
        return 0;
    }

    @Override
    public boolean insert(BlockPos pos, World world, Object value) {
        return false;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        int idx = (byte) (state.get(FACING) == Direction.DOWN ? 3 : 0) |
                (byte) (state.get(FACING) == Direction.UP ? 3 : 0) |
                (byte) (state.get(FACING) == Direction.NORTH ? 12 : 0) |
                (byte) (state.get(FACING) == Direction.SOUTH ? 12 : 0) |
                (byte) (state.get(FACING) == Direction.WEST ? 48 : 0) |
                (byte) (state.get(FACING) == Direction.EAST ? 48 : 0) |
                (byte) (64);
        return SHAPES[idx];
    }

    private static VoxelShape[] generateShapes() {
        float radius = 1 / 16f;
        VoxelShape[] shapes = new VoxelShape[128];
        float min = 0.5F - radius;
        float max = 0.5F + radius;
        VoxelShape center = VoxelShapes.cuboid(min, min, min, max, max, max);

        VoxelShape[] connections = new VoxelShape[]{
                VoxelShapes.cuboid(min, 0f, min, max, max, max),
                VoxelShapes.cuboid(min, min, min, max, 1f, max),
                VoxelShapes.cuboid(min, min, 0f, max, max, max),
                VoxelShapes.cuboid(min, min, min, max, max, 1f),
                VoxelShapes.cuboid(0f, min, min, max, max, max),
                VoxelShapes.cuboid(min, min, min, 1f, max, max)
        };

        float nodeRadius = 2 / 16f;
        float nodeMin = 0.5F - nodeRadius;
        float nodeMax = 0.5F + nodeRadius;
        VoxelShape node = VoxelShapes.cuboid(nodeMin, nodeMin, nodeMin, nodeMax, nodeMax, nodeMax);

        for (int i = 0; i < 128; i++) {
            VoxelShape shape = i >= 64 ? node : center;
            for (int side = 0; side < FACING.getValues().size(); ++side) {
                if ((i & 1 << side) != 0) {
                    shape = VoxelShapes.union(shape, connections[side]);
                }
            }
            shapes[i] = shape;
        }
        return shapes;
    }
}
