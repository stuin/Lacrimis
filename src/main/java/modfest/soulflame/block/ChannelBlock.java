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

public class ChannelBlock extends Block {

    public static final BooleanProperty NORTH = Properties.NORTH;
    public static final BooleanProperty SOUTH = Properties.SOUTH;
    public static final BooleanProperty EAST = Properties.EAST;
    public static final BooleanProperty WEST = Properties.WEST;

    private static final VoxelShape[] SHAPES = generateShapes();

    public ChannelBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(NORTH, false).with(SOUTH, false).with(EAST, false).with(WEST, false));
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(NORTH, SOUTH, EAST, WEST);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        return state
                .with(NORTH, this.connectsTo(world, pos.north()))
                .with(SOUTH, this.connectsTo(world, pos.south()))
                .with(EAST, this.connectsTo(world, pos.east()))
                .with(WEST, this.connectsTo(world, pos.west()));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        int idx = (state.get(NORTH) ? 1 : 0) | (state.get(SOUTH) ? 2 : 0) | (state.get(WEST) ? 4 : 0) | (state.get(EAST) ? 8 : 0);
        return SHAPES[idx];
    }

    private boolean connectsTo(WorldAccess world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == this;
    }

    private static VoxelShape[] generateShapes() {
        VoxelShape center = VoxelShapes.cuboid(3 / 16f, 5 / 16f, 3 / 16f, 13 / 16f, 7 / 16f, 13 / 16f);
        VoxelShape north = VoxelShapes.cuboid(3 / 16f, 5 / 16f, 0 / 16f, 13 / 16f, 7 / 16f, 3 / 16f);
        VoxelShape south = VoxelShapes.cuboid(3 / 16f, 5 / 16f, 13 / 16f, 13 / 16f, 7 / 16f, 16 / 16f);
        VoxelShape west = VoxelShapes.cuboid(0 / 16f, 5 / 16f, 3 / 16f, 3 / 16f, 7 / 16f, 13 / 16f);
        VoxelShape east = VoxelShapes.cuboid(13 / 16f, 5 / 16f, 3 / 16f, 16 / 16f, 7 / 16f, 13 / 16f);
        VoxelShape[] shapes = new VoxelShape[16];
        Arrays.fill(shapes, center);
        for (int i = 0; i < 8; i++) {
            int n = i << 1 | 0b0001;
            int s = i << 1 & 0b1100 | i & 0b0001 | 0b0010;
            int w = i << 1 & 0b1000 | i & 0b0011 | 0b0100;
            int e = i | 0b1000;
            shapes[n] = VoxelShapes.combineAndSimplify(shapes[n], north, BooleanBiFunction.OR);
            shapes[s] = VoxelShapes.combineAndSimplify(shapes[s], south, BooleanBiFunction.OR);
            shapes[w] = VoxelShapes.combineAndSimplify(shapes[w], west, BooleanBiFunction.OR);
            shapes[e] = VoxelShapes.combineAndSimplify(shapes[e], east, BooleanBiFunction.OR);
        }
        return shapes;
    }

}
