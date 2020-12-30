package azoth.blocks;

import azoth.blocks.AzothConduitBlock.ConnectionType;
import azoth.blocks.entity.AzothCauldronBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
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
                Item item = stack.getItem();
                if (item == Items.WATER_BUCKET) {
                    if (!world.isClient) {
                        if (!player.abilities.creativeMode) {
                            player.setStackInHand(hand, new ItemStack(Items.BUCKET));
                        }
                        player.incrementStat(Stats.FILL_CAULDRON);
                        world.setBlockState(pos, Blocks.CAULDRON.getDefaultState().with(CauldronBlock.LEVEL, 3));
                        world.playSound((PlayerEntity) null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    }
                    return ActionResult.success(world.isClient);
                } else if (item == Items.POTION && PotionUtil.getPotion(stack) == Potions.WATER) {
                    if (!world.isClient) {
                        if (!player.abilities.creativeMode) {
                            player.setStackInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
                            if (player instanceof ServerPlayerEntity) {
                                ((ServerPlayerEntity) player).refreshScreenHandler(player.playerScreenHandler);
                            }
                        }
                        player.incrementStat(Stats.USE_CAULDRON);
                        world.setBlockState(pos, Blocks.CAULDRON.getDefaultState().with(CauldronBlock.LEVEL, 1));
                        world.playSound((PlayerEntity) null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    }
                    return ActionResult.success(world.isClient);
                }
            }
        }
        return ActionResult.PASS;
    }
}
