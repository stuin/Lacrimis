package azoth.blocks;

import java.util.EnumSet;

import azoth.AzothInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class AzothConduitBlock extends Block {
    public static final EnumProperty<ConnectionType> DOWN = EnumProperty.of("down", ConnectionType.class);
    public static final EnumProperty<ConnectionType> UP = EnumProperty.of("up", ConnectionType.class);
    public static final EnumProperty<ConnectionType> NORTH = EnumProperty.of("north", ConnectionType.class);
    public static final EnumProperty<ConnectionType> SOUTH = EnumProperty.of("south", ConnectionType.class);
    public static final EnumProperty<ConnectionType> EAST = EnumProperty.of("east", ConnectionType.class);
    public static final EnumProperty<ConnectionType> WEST = EnumProperty.of("west", ConnectionType.class);

    public static final BooleanProperty NODE = BooleanProperty.of("node");

    private static final VoxelShape[] SHAPES;
    static {
        SHAPES = new VoxelShape[128];

        float min = 7;
        float max = 9;
        VoxelShape center = Block.createCuboidShape(min, min, min, max, max, max);

        VoxelShape[] connections = new VoxelShape[] {
                Block.createCuboidShape(min, 0, min, max, max, max),
                Block.createCuboidShape(min, min, min, max, 16, max),
                Block.createCuboidShape(min, min, 0, max, max, max),
                Block.createCuboidShape(min, min, min, max, max, 16),
                Block.createCuboidShape(0, min, min, max, max, max),
                Block.createCuboidShape(min, min, min, 16, max, max)
        };

        float nodeMin = 6;
        float nodeMax = 10;
        VoxelShape node = Block.createCuboidShape(nodeMin, nodeMin, nodeMin, nodeMax, nodeMax, nodeMax);

        for (int i = 0; i < 128; i++) {
            VoxelShape shape = i >= 64 ? node : center;
            for (int side = 0; side < FACINGS.length; ++side) {
                if ((i & 1 << side) != 0) {
                    shape = VoxelShapes.union(shape, connections[side]);
                }
            }
            SHAPES[i] = shape;
        }
    }

    public AzothConduitBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState()
                .with(NODE, false)
                .with(DOWN, ConnectionType.NONE)
                .with(UP, ConnectionType.NONE)
                .with(NORTH, ConnectionType.NONE)
                .with(SOUTH, ConnectionType.NONE)
                .with(EAST, ConnectionType.NONE)
                .with(WEST, ConnectionType.NONE));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
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
            EnumProperty<ConnectionType> property = getConnectionProperty(direction);
            ConnectionType value = this.getConnectionType(world, pos, direction);

            state = state.with(property, value);
            if (value != ConnectionType.NONE) {
                axes.add(direction.getAxis());
                connections++;
            }
        }

        return state.with(NODE, axes.size() != 1 || connections <= 1);
    }

    public ConnectionType getConnectionType(BlockView view, BlockPos pos, Direction side) {
        BlockState neighbor = view.getBlockState(pos.offset(side));
        Block block = neighbor.getBlock();
        if (block instanceof AzothConduitBlock) {
            return ConnectionType.NORMAL;
        }
        if (block instanceof ConduitConnectable) {
            return ((ConduitConnectable) block).getConnectionType(neighbor, side);
        }
        return ConnectionType.NONE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        int idx = (state.get(DOWN) != ConnectionType.NONE ? 1 : 0) |
                (state.get(UP) != ConnectionType.NONE ? 2 : 0) |
                (state.get(NORTH) != ConnectionType.NONE ? 4 : 0) |
                (state.get(SOUTH) != ConnectionType.NONE ? 8 : 0) |
                (state.get(WEST) != ConnectionType.NONE ? 16 : 0) |
                (state.get(EAST) != ConnectionType.NONE ? 32 : 0) |
                (state.get(NODE) ? 64 : 0);
        return SHAPES[idx];
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        super.onStateReplaced(state, world, pos, newState, moved);
        AzothInitializer.getConduitManager(world).notifyUpdate(pos);
    }

    public static EnumProperty<ConnectionType> getConnectionProperty(Direction direction) {
        switch (direction) {
        case NORTH:
            return NORTH;
        case SOUTH:
            return SOUTH;
        case WEST:
            return WEST;
        case EAST:
            return EAST;
        case DOWN:
            return DOWN;
        case UP:
        default:
            return UP;
        }
    }

    public enum ConnectionType implements StringIdentifiable {
        NONE("none"),
        NORMAL("normal"),
        CAULDRON("cauldron");

        private final String name;

        private ConnectionType(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

}
