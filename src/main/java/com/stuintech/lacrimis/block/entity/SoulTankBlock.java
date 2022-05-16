package com.stuintech.lacrimis.block.entity;

import com.stuintech.lacrimis.item.BottleOfTearsItem;
import com.stuintech.lacrimis.util.SoulTank;
import com.stuintech.lacrimis.util.TaintPacket;
import com.stuintech.socketwrench.fasteners.FastenerBlock;
import com.stuintech.lacrimis.block.DuctConnectBlock;
import com.stuintech.lacrimis.item.ModItems;
import net.minecraft.block.AbstractBlock;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public abstract class SoulTankBlock extends BlockWithEntity implements DuctConnectBlock, FastenerBlock {
    private final boolean canExtract;

    protected SoulTankBlock(AbstractBlock.Settings settings, boolean canExtract) {
        super(settings);
        this.canExtract = canExtract;
    }

    @Override
    public boolean hasComparatorOutput(BlockState blockState) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState blockState, World world, BlockPos blockPos) {
        final BlockEntity blockEntity = world.getBlockEntity(blockPos);

        if (blockEntity instanceof SoulTankEntity) {
            SoulTank tank = ((SoulTankEntity) blockEntity).getTank();
            if(tank.getCapacity() == 0)
                return 0;
            return MathHelper.floor(14.0F * tank.getTears() / tank.getCapacity()) + (tank.getTears() > 0 ? 1 : 0);
        }

        return 0;
    }

    @Override
    public boolean onFasten(PlayerEntity player, World world, BlockPos pos, Vec3d vec3d, Direction direction) {
        //Read tears
        if(player != null && !player.isSneaking()) {
            if(!world.isClient) {
                int level = getTank(world, pos).getTears();
                Text text = new LiteralText(level + " Tears");
                player.sendMessage(text, false);
            }
            return true;
        }
        return false;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.isEmpty()) {
            return ActionResult.PASS;
        }
        SoulTankEntity entity = (SoulTankEntity) world.getBlockEntity(pos);
        if(entity != null) {
            SoulTank tank = entity.getTank();
            Item item = stack.getItem();
            if(item == ModItems.bottleOfTears) {
                if(tank.getSpace() >= BottleOfTearsItem.capacity && !world.isClient) {
                    //Empty bottle
                    if(!player.isCreative()) {
                        ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
                        stack.decrement(1);
                        player.getInventory().offerOrDrop(bottle);
                    }

                    world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    tank.addTears(BottleOfTearsItem.capacity);
                }

                return ActionResult.success(world.isClient);
            } else {
                ItemStack itemStack4;
                if(item == Items.GLASS_BOTTLE) {
                    if(tank.getTears() >= BottleOfTearsItem.capacity && !world.isClient) {
                        //Fill bottle
                        if(!player.isCreative()) {
                            itemStack4 = new ItemStack(ModItems.bottleOfTears);
                            stack.decrement(1);
                            if(stack.isEmpty()) {
                                player.setStackInHand(hand, itemStack4);
                            } else if(!player.getInventory().insertStack(itemStack4)) {
                                player.dropItem(itemStack4, false);
                            }
                        }

                        world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        tank.removeTears(BottleOfTearsItem.capacity);
                    }

                    return ActionResult.success(world.isClient);
                }
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        int tears = getTank(world, pos).getTears();
        super.onBreak(world, pos, state, player);
        new TaintPacket(tears).spawn(world, pos.up());
    }

    public SoulTank getTank(BlockView world, BlockPos pos) {
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof SoulTankEntity)
            return ((SoulTankEntity) entity).getTank();
        return null;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean canConnectDuctTo(BlockPos pos, WorldAccess world, Direction side) {
        return side != Direction.UP;
    }

    @Override
    public int extractTears(BlockPos pos, World world, int request, boolean simulate) {
        SoulTank tank = getTank(world, pos);
        if(tank != null && tank.getTears() > 0 && canExtract) {
            if(simulate)
                return Math.min(tank.getTears(), request);
            return tank.removeTears(request);
        }
        return 0;
    }

    @Override
    public boolean insert(BlockPos pos, World world, Object value) {
        return false;
    }
}
