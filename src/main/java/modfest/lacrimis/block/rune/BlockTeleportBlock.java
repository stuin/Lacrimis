package modfest.lacrimis.block.rune;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.init.ModBlocks;
import modfest.lacrimis.util.DuctUtil;

public class BlockTeleportBlock extends CenterRuneBlock {
    public BlockTeleportBlock() {
        super(100, 2);
    }

    @Override
    protected boolean onActivate(World world, BlockPos pos, BlockPos duct, Entity entity, PlayerEntity player) {
        BlockPos destination = DuctUtil.locateSink(world, duct, pos);
        if (destination != null) {
            return true;
        } else
            error(player, "destination");
        return false;
    }

    @Override
    public boolean insert(BlockPos dest, BlockView blockView, Object value) {
        Direction flipped = flipside(blockView, dest);
        if(blockView instanceof World && value instanceof BlockPos && testCage(blockView, dest, flipped, null) > 0) {
            World world = (World) blockView;
            dest = dest.offset(flipped);
            BlockPos source = (BlockPos) value;
            BlockState sourceState = world.getBlockState(source);
            BlockState destState = world.getBlockState(dest);

            if(sourceState.getBlock().isIn(ModBlocks.non_transportable) ||
                    destState.getBlock().isIn(ModBlocks.non_transportable))
                return false;

            //Set destination block
            BlockEntity sourceEntity = world.getBlockEntity(source);
            BlockEntity destEntity = world.getBlockEntity(dest);

            //Copy entity
            if(sourceEntity != null && destEntity != null)
                return false;
            if(sourceEntity != null) {
                swap(world, sourceEntity, sourceState, dest);
                world.setBlockState(source, destState);
            } else if(destEntity != null) {
                swap(world, destEntity, destState, source);
                world.setBlockState(dest, sourceState);
            } else {
                world.setBlockState(source, destState);
                world.setBlockState(dest, sourceState);
            }
            return true;
        }
        return false;
    }

    private void swap(World world, BlockEntity sourceEntity, BlockState sourceState, BlockPos dest) {
        CompoundTag sourceTag = sourceEntity.toTag(new CompoundTag());
        sourceEntity.fromTag(sourceState, new CompoundTag());

        world.setBlockState(dest, sourceState);
        BlockEntity destEntity = world.getBlockEntity(dest);
        if(destEntity != null) {
            //Write destination entity
            sourceTag.putInt("x", dest.getX());
            sourceTag.putInt("y", dest.getY());
            sourceTag.putInt("z", dest.getZ());
            destEntity.fromTag(sourceState, sourceTag);
        }
    }
}
