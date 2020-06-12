package modfest.soulflame.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import java.util.Arrays;

public class ConduitBlock extends Block {

    public static final BooleanProperty DOWN = Properties.DOWN;
    public static final BooleanProperty UP = Properties.UP;
    public static final BooleanProperty NORTH = Properties.NORTH;
    public static final BooleanProperty SOUTH = Properties.SOUTH;
    public static final BooleanProperty EAST = Properties.EAST;
    public static final BooleanProperty WEST = Properties.WEST;

    private static final VoxelShape[] SHAPES = generateShapes();

    public ConduitBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(DOWN, false).with(UP, false).with(NORTH, false).with(SOUTH, false).with(EAST, false).with(WEST, false));
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(DOWN,UP, NORTH, SOUTH, EAST, WEST);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        return state
                .with(DOWN, this.connectsTo(world, pos.down(), Direction.UP))
                .with(UP, this.connectsTo(world, pos.up(), Direction.DOWN))
                .with(NORTH, this.connectsTo(world, pos.north(), Direction.SOUTH))
                .with(SOUTH, this.connectsTo(world, pos.south(), Direction.NORTH))
                .with(EAST, this.connectsTo(world, pos.east(), Direction.WEST))
                .with(WEST, this.connectsTo(world, pos.west(), Direction.EAST));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        int idx = (state.get(DOWN) ? 1 : 0) |
                (state.get(UP) ? 2 : 0) |
                (state.get(NORTH) ? 4 : 0) |
                (state.get(SOUTH) ? 8 : 0) |
                (state.get(WEST) ? 16 : 0) |
                (state.get(EAST) ? 32 : 0);
        return SHAPES[idx];
    }

    private boolean connectsTo(WorldAccess world, BlockPos pos, Direction side) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        return block == this || (block instanceof BlockConduitConnect && ((BlockConduitConnect) block).canConnectConduitTo(state, pos, world, side));
    }

    private static VoxelShape[] generateShapes() {
        float r = 2 / 16f;
        VoxelShape[] shapes = new VoxelShape[1 << 6];
        VoxelShape center = VoxelShapes.cuboid(8 / 16f - r, 8 / 16f - r, 8 / 16f - r, 8 / 16f + r, 8 / 16f + r, 8 / 16f + r);
        VoxelShape down = VoxelShapes.cuboid(8 / 16f - r, 0f, 8 / 16f - r, 8 / 16f + r, 8 / 16f + r, 8 / 16f + r);
        VoxelShape up = VoxelShapes.cuboid(8 / 16f - r, 8 / 16f - r, 8 / 16f - r, 8 / 16f + r, 1f, 8 / 16f + r);
        VoxelShape north = VoxelShapes.cuboid(8 / 16f - r, 8 / 16f - r, 0f, 8 / 16f + r, 8 / 16f + r, 8 / 16f + r);
        VoxelShape south = VoxelShapes.cuboid(8 / 16f - r, 8 / 16f - r, 8 / 16f - r, 8 / 16f + r, 8 / 16f + r, 1f);
        VoxelShape west = VoxelShapes.cuboid(0f, 8 / 16f - r, 8 / 16f - r, 8 / 16f + r, 8 / 16f + r, 8 / 16f + r);
        VoxelShape east = VoxelShapes.cuboid(8 / 16f - r, 8 / 16f - r, 8 / 16f - r, 1f, 8 / 16f + r, 8 / 16f + r);
        Arrays.fill(shapes, center);
        for (int i = 0; i < 1 << 5; i++) {
            int d = i << 1 | 0b000001;
            int u = i << 1 & 0b111100 | i & 0b000001 | 0b000010;
            int n = i << 1 & 0b111000 | i & 0b000011 | 0b000100;
            int s = i << 1 & 0b110000 | i & 0b000111 | 0b001000;
            int w = i << 1 & 0b100000 | i & 0b001111 | 0b010000;
            int e = i | 0b100000;
            shapes[d] = VoxelShapes.combineAndSimplify(shapes[d], down, BooleanBiFunction.OR);
            shapes[u] = VoxelShapes.combineAndSimplify(shapes[u], up, BooleanBiFunction.OR);
            shapes[n] = VoxelShapes.combineAndSimplify(shapes[n], north, BooleanBiFunction.OR);
            shapes[s] = VoxelShapes.combineAndSimplify(shapes[s], south, BooleanBiFunction.OR);
            shapes[w] = VoxelShapes.combineAndSimplify(shapes[w], west, BooleanBiFunction.OR);
            shapes[e] = VoxelShapes.combineAndSimplify(shapes[e], east, BooleanBiFunction.OR);
        }
        return shapes;
    }

}
