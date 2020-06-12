package modfest.soulflame.block;

import modfest.soulflame.block.entity.CrucibleEntity;
import modfest.soulflame.block.entity.SoulTankEntity;
import modfest.soulflame.init.ModItems;
import modfest.soulflame.item.BottleOfTearsItem;
import modfest.soulflame.util.SoulTank;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public abstract class SoulTankBlock extends BlockWithEntity implements BlockConduitConnect {
    protected SoulTankBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasComparatorOutput(BlockState blockState) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState blockState, World world, BlockPos blockPos) {
        final BlockEntity blockEntity = world.getBlockEntity(blockPos);

        if (blockEntity instanceof SoulTankEntity)
            return (int)(Math.floor(14.0 * ((SoulTankEntity)blockEntity).getRelativeLevel())) + 1;

        return 0;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.isEmpty()) {
            return ActionResult.PASS;
        }
        SoulTankEntity entity = (SoulTankEntity) world.getBlockEntity(pos);
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
                entity.addTears(BottleOfTearsItem.capacity);
            }

            return ActionResult.success(world.isClient);
        } else if (item == ModItems.diviningRod) {
            if(world.isClient) {
                Text text = new LiteralText(level + " Tears");
                player.sendMessage(text, false);
            }
            return ActionResult.PASS;
        } else {
            ItemStack itemStack4;
            if (item == Items.GLASS_BOTTLE) {
                if (level >= 250 && !world.isClient) {
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
                    entity.removeTears(level - BottleOfTearsItem.capacity);
                }

                return ActionResult.success(world.isClient);
            }

            return ActionResult.PASS;
        }

    }

    public SoulTank getTank(BlockView world, BlockState state, BlockPos pos) {
        BlockEntity entity = world.getBlockEntity(pos);
        if(entity instanceof SoulTankEntity)
            return ((SoulTankEntity) entity).getTank();
        return null;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean canConnectConduitTo(BlockState state, BlockPos pos, BlockView world, Direction side) {
        return side.getAxis() != Direction.Axis.Y;
    }

    @Override
    public int extract(BlockState state, BlockPos pos, World world, int amount, boolean simulate) {
        BlockEntity entity = world.getBlockEntity(pos);
        if(entity instanceof SoulTankEntity)
            return ((SoulTankEntity) entity).removeTears(amount);
        return 0;
    }

    @Override
    public int insert(BlockState state, BlockPos pos, World world, int amount, boolean simulate) {
        BlockEntity entity = world.getBlockEntity(pos);
        if(entity instanceof SoulTankEntity)
            return ((SoulTankEntity) entity).addTears(amount);
        return 0;
    }

    @Override
    public int getMaxTearsAmount(BlockState state, BlockPos pos, BlockView world) {
        SoulTank tank = getTank(world, state, pos);
        if(tank != null)
            return tank.getCapacity();
        return 0;
    }

    @Override
    public int getCurrentTearsAmount(BlockState state, BlockPos pos, BlockView world) {
        SoulTank tank = getTank(world, state, pos);
        if(tank != null)
            return tank.getTears();
        return 0;
    }

}
