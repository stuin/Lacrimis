package modfest.lacrimis.block;

import java.util.EnumSet;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ConnectingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public class DuctBlock extends Block {
    public static final BooleanProperty DOWN = Properties.DOWN;
    public static final BooleanProperty UP = Properties.UP;
    public static final BooleanProperty NORTH = Properties.NORTH;
    public static final BooleanProperty SOUTH = Properties.SOUTH;
    public static final BooleanProperty EAST = Properties.EAST;
    public static final BooleanProperty WEST = Properties.WEST;

    public static final Map<Direction, BooleanProperty> FACING_PROPERTIES = ConnectingBlock.FACING_PROPERTIES;

    public static final BooleanProperty NODE = BooleanProperty.of("node");

    private static final VoxelShape[] SHAPES = generateShapes();

    public DuctBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(NODE, false)
                .with(DOWN, false).with(UP, false)
                .with(NORTH, false).with(SOUTH, false)
                .with(EAST, false).with(WEST, false));
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(NODE, DOWN, UP, NORTH, SOUTH, EAST, WEST);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        return this.connectToBlocks(state, world, pos);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.connectToBlocks(this.getDefaultState(), ctx.getWorld(), ctx.getBlockPos());
    }

    protected BlockState connectToBlocks(BlockState state, WorldAccess world, BlockPos pos) {
        EnumSet<Axis> axes = EnumSet.noneOf(Axis.class);
        int connections = 0;
        for (Direction direction : Direction.values()) {
            BooleanProperty property = FACING_PROPERTIES.get(direction);
            boolean value = this.connectsTo(world, pos.offset(direction), direction);

            state = state.with(property, value);
            if (value) {
                axes.add(direction.getAxis());
                connections++;
            }
        }

        return state.with(NODE, axes.size() != 1 || connections == 1);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        int idx = (state.get(DOWN) ? 1 : 0) |
                (state.get(UP) ? 2 : 0) |
                (state.get(NORTH) ? 4 : 0) |
                (state.get(SOUTH) ? 8 : 0) |
                (state.get(WEST) ? 16 : 0) |
                (state.get(EAST) ? 32 : 0) |
                (state.get(NODE) ? 64 : 0);
        return SHAPES[idx];
    }

    private boolean connectsTo(WorldAccess world, BlockPos pos, Direction side) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        return block instanceof DuctBlock || block instanceof DuctConnectBlock && ((DuctConnectBlock) block).canConnectDuctTo(pos, world, side);
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
