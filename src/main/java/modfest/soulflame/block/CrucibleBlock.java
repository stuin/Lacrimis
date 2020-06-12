package modfest.soulflame.block;

import modfest.soulflame.block.entity.CrucibleEntity;
import modfest.soulflame.init.ModItems;
import modfest.soulflame.item.BottleOfTearsItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CrucibleBlock extends LiquidTankBlock {
    private static final VoxelShape RAY_TRACE_SHAPE = createCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape OUTLINE_SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.union(createCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), createCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), RAY_TRACE_SHAPE), BooleanBiFunction.ONLY_FIRST);

    public CrucibleBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new CrucibleEntity();
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
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.isEmpty()) {
            return ActionResult.PASS;
        }
        CrucibleEntity entity = (CrucibleEntity) world.getBlockEntity(pos);
        int level = entity.getLevel();
        Item item = stack.getItem();
        if (item == ModItems.bottleOfTears) {
            if (level <= 750 && !world.isClient) {
                if (!player.abilities.creativeMode) {
                    ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
                    stack.decrement(1);
                    player.inventory.offerOrDrop(world, bottle);
                }

                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                entity.setLevel(level + BottleOfTearsItem.capacity);
            }

            return ActionResult.success(world.isClient);
        } else {
            ItemStack itemStack4;
            if (item == Items.GLASS_BOTTLE) {
                if (level > 0 && !world.isClient) {
                    if (!player.abilities.creativeMode) {
                        itemStack4 = new ItemStack(ModItems.bottleOfTears);
                        stack.decrement(1);
                        if (stack.isEmpty()) {
                            player.setStackInHand(hand, itemStack4);
                        } else if (!player.inventory.insertStack(itemStack4)) {
                            player.dropItem(itemStack4, false);
                        }
                    }

                    world.playSound((PlayerEntity) null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    entity.setLevel(level - BottleOfTearsItem.capacity);
                }

                return ActionResult.success(world.isClient);
            }

            return ActionResult.PASS;
        }

    }
}
