package modfest.soulflame.block;

import grondag.fluidity.api.storage.Store;
import modfest.soulflame.SoulFlame;
import modfest.soulflame.block.entity.LiquidTankEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class LiquidTankBlock extends BlockWithEntity {
    protected LiquidTankBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasComparatorOutput(BlockState blockState) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState blockState, World world, BlockPos blockPos) {
        final BlockEntity blockEntity = world.getBlockEntity(blockPos);

        if (blockEntity instanceof LiquidTankEntity)
            return (int)(Math.floor(14.0 * ((LiquidTankEntity)blockEntity).getUsage())) + 1;

        return 0;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        final ItemStack stack = player.getStackInHand(hand);

        if(Block.getBlockFromItem(stack.getItem()) instanceof LiquidTankBlock)
            return ActionResult.PASS;

        if (!world.isClient) {
            final BlockEntity be = world.getBlockEntity(pos);
            if(be instanceof LiquidTankEntity) {
                final LiquidTankEntity tankBe = (LiquidTankEntity) be;
                if(Store.STORAGE_COMPONENT.applyActionsWithHeld(tankBe.getEffectiveStorage(), (ServerPlayerEntity)player)) {
                    SoulFlame.LOGGER.info(tankBe.getUsage());
                    return ActionResult.SUCCESS;
                }
            }
        }

        return ActionResult.PASS;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

}
