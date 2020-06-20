package modfest.lacrimis.block;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import java.util.EnumSet;
import java.util.Map;

public class OneWayConduitBlock extends FacingBlock implements BlockConduitConnect {
    public static final BooleanProperty DOWN = Properties.DOWN;
    public static final BooleanProperty UP = Properties.UP;
    public static final BooleanProperty NORTH = Properties.NORTH;
    public static final BooleanProperty SOUTH = Properties.SOUTH;
    public static final BooleanProperty EAST = Properties.EAST;
    public static final BooleanProperty WEST = Properties.WEST;

    public static final Map<Direction, BooleanProperty> FACING_PROPERTIES = ConnectingBlock.FACING_PROPERTIES;

    private static final VoxelShape[] SHAPES = generateShapes();

    public OneWayConduitBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.connectToBlocks(this.getDefaultState(), ctx.getWorld(), ctx.getBlockPos()).with(FACING, ctx.getPlayerLookDirection());
    }

    protected BlockState connectToBlocks(BlockState state, WorldAccess world, BlockPos pos) {
        EnumSet<Direction.Axis> axes = EnumSet.noneOf(Direction.Axis.class);
        int connections = 0;
        for (Direction direction : Direction.values()) {
            BooleanProperty property = FACING_PROPERTIES.get(direction);
            boolean value = this.connectsTo(world, pos.offset(direction),  direction);

            state = state.with(property, value);
            if (value) {
                axes.add(direction.getAxis());
                connections++;
            }
        }

        return state;
    }

    private boolean connectsTo(WorldAccess world, BlockPos pos, Direction side) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        return block instanceof ConduitBlock || block instanceof BlockConduitConnect && ((BlockConduitConnect) block).canConnectConduitTo(pos, world, side);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, DOWN, UP, NORTH, SOUTH, EAST, WEST);
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

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        int idx = (byte)(state.get(FACING) == Direction.DOWN?3:0) |
                (byte)(state.get(FACING) == Direction.UP?3:0) |
                (byte)(state.get(FACING) == Direction.NORTH?12:0) |
                (byte)(state.get(FACING) == Direction.SOUTH?12:0) |
                (byte)(state.get(FACING) == Direction.WEST?48:0) |
                (byte)(state.get(FACING) == Direction.EAST?48:0) |
                (byte)(64);
        return SHAPES[idx];
    }

    private static VoxelShape[] generateShapes() {
        float radius = 1 / 16f;
        VoxelShape[] shapes = new VoxelShape[128];
        float min = 0.5F - radius;
        float max = 0.5F + radius;
        VoxelShape center = VoxelShapes.cuboid(min, min, min, max, max, max);

        VoxelShape[] connections = new VoxelShape[] {
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
            for (int side = 0; side < FACINGS.length; ++side) {
                if ((i & 1 << side) != 0) {
                    shape = VoxelShapes.union(shape, connections[side]);
                }
            }
            shapes[i] = shape;
        }
        return shapes;
    }
}
