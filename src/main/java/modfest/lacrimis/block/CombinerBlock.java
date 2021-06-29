package modfest.lacrimis.block;

import modfest.lacrimis.block.entity.CombinerEntity;
import modfest.lacrimis.init.ModEntities;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.HashSet;

public class CombinerBlock extends BlockWithEntity implements DuctConnectBlock {
    public static HashSet<EntityType<?>> blocked = new HashSet<>();

    public CombinerBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CombinerEntity(pos, state);
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CombinerEntity) {
                player.openHandledScreen((CombinerEntity)blockEntity);
            }

            return ActionResult.CONSUME;
        }
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
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean canConnectDuctTo(BlockPos pos, WorldAccess world, Direction side) {
        return true;
    }

    @Override
    public int extractTears(BlockPos pos, World world, int request, boolean simulate) {
        return 0;
    }

    @Override
    public boolean insert(BlockPos pos, World world, Object value) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if(value instanceof LivingEntity && !(((LivingEntity) value).getType().isIn(ModEntities.combiner_blocked) || ((LivingEntity) value).getType().getSpawnGroup().equals(SpawnGroup.MISC)) &&
                blockEntity instanceof CombinerEntity && ((CombinerEntity) blockEntity).type == null) {
            ((CombinerEntity) blockEntity).type = ((LivingEntity) value).getType();
            ((LivingEntity) value).kill();
            return true;
        }
        return false;
    }
}
