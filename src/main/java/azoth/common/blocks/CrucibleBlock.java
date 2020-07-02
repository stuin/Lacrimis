package azoth.common.blocks;

import azoth.Azoth;
import azoth.common.blocks.entity.CrucibleBlockEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CrucibleBlock extends Block implements BlockEntityProvider, ConduitConnectable {
    protected static final VoxelShape RAY_TRACE_SHAPE = createCuboidShape(3.0D, 4.0D, 3.0D, 13.0D, 16.0D, 13.0D);
    protected static final VoxelShape OUTLINE_SHAPE = VoxelShapes.combineAndSimplify(
            VoxelShapes.fullCube(),
            RAY_TRACE_SHAPE,
            BooleanBiFunction.ONLY_FIRST);

    public CrucibleBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return OUTLINE_SHAPE;
    }

    @Override
    public VoxelShape getRayTraceShape(BlockState state, BlockView world, BlockPos pos) {
        return RAY_TRACE_SHAPE;
    }

    @Override
    public boolean connectsToConduit(BlockState state, Direction side) {
        return side != Direction.DOWN;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new CrucibleBlockEntity();
    }

    @Override
    public boolean isSource() {
        return true;
    }

    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        super.onStateReplaced(state, world, pos, newState, moved);
        Azoth.getConduitManager(world).notifyUpdate(pos);
    }
}
