package modfest.lacrimis.block;

import modfest.lacrimis.block.entity.CombinerEntity;
import modfest.lacrimis.init.ModCrafting;
import modfest.lacrimis.init.ModItems;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CombinerBlock extends SoulTankBlock {
    public CombinerBlock(Settings settings) {
        super(settings, false);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new CombinerEntity();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ActionResult parentResult = super.onUse(state, world, pos, player, hand, hit);
        if(parentResult == ActionResult.PASS && player.getStackInHand(hand).getItem() != ModItems.diviningRod) {
            if(world.isClient) {
                return ActionResult.SUCCESS;
            } else {
                ContainerProviderRegistry.INSTANCE.openContainer(ModCrafting.COMBINER_SCREEN_ID, player, buf -> buf.writeBlockPos(pos));

                return ActionResult.CONSUME;
            }
        }
        return parentResult;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof Inventory) {
                ItemScatterer.spawn(world, pos, (Inventory)blockEntity);
                world.updateComparators(pos, this);
                ((Inventory) blockEntity).clear();
            }
        }
    }

    @Override
    public boolean canConnectDuctTo(BlockPos pos, BlockView world, Direction side) {
        return true;
    }

    @Override
    public boolean insert(BlockPos pos, BlockView world, Object value) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if(value instanceof LivingEntity && !(value instanceof PlayerEntity) &&
                blockEntity instanceof CombinerEntity && ((CombinerEntity) blockEntity).type == null) {
            ((CombinerEntity) blockEntity).type = ((LivingEntity) value).getType();
            ((LivingEntity) value).kill();
            return true;
        }
        return false;
    }
}
