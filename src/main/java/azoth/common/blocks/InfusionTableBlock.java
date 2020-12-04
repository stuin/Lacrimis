package azoth.common.blocks;

import azoth.Azoth;
import azoth.common.blocks.entity.InfusionTableBlockEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class InfusionTableBlock extends Block implements ConduitConnectable, BlockEntityProvider {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);

    public InfusionTableBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Override
    public boolean connectsToConduit(BlockState state, Direction side) {
        return side != Direction.DOWN;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new InfusionTableBlockEntity();
    }

    @Override
    public boolean isSink() {
        return true;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        super.onStateReplaced(state, world, pos, newState, moved);
        Azoth.getConduitManager(world).notifyUpdate(pos);
    }

    @Override
    public int tryReceive(World world, BlockPos pos, int amount) {
        InfusionTableBlockEntity entity = (InfusionTableBlockEntity) world.getBlockEntity(pos);
        return entity.tryAcceptAzoth(amount);
    }
}
