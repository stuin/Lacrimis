package lacrimis.blocks;

import lacrimis.blocks.AzothConduitBlock.ConnectionType;
import lacrimis.blocks.entity.AzothCauldronBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class AzothCauldronBlock extends Block implements BlockEntityProvider, ConduitConnectable {
    private static final VoxelShape RAY_TRACE_SHAPE = createCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape OUTLINE_SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(),
            VoxelShapes.union(createCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), createCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), RAY_TRACE_SHAPE),
            BooleanBiFunction.ONLY_FIRST);

    public AzothCauldronBlock(Block.Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(Blocks.CAULDRON);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return OUTLINE_SHAPE;
    }

    @Override
    public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return RAY_TRACE_SHAPE;
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Override
    public ConnectionType getConnectionType(BlockState state, Direction side) {
        return side.getAxis().isHorizontal() ? ConnectionType.CAULDRON : ConnectionType.NONE;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new AzothCauldronBlockEntity();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if (!stack.isEmpty()) {
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof AzothCauldronBlockEntity && ((AzothCauldronBlockEntity) entity).getLevel() == 0) {
                world.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
                return Blocks.CAULDRON.onUse(world.getBlockState(pos), world, pos, player, hand, hit);
            }
        }
        return ActionResult.PASS;
    }
}
