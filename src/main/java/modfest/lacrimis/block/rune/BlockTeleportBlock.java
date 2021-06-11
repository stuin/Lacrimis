package modfest.lacrimis.block.rune;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

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
    public boolean insert(BlockPos dest, World world, Object value) {
        Direction flipped = flipside(world, dest);
        if(value instanceof BlockPos source && testCage(world, dest, flipped, null) > 0) {
            dest = dest.offset(flipped);
            BlockState sourceState = world.getBlockState(source);
            BlockState destState = world.getBlockState(dest);

            if(ModBlocks.non_transportable.contains(sourceState.getBlock()) ||
                ModBlocks.non_transportable.contains(destState.getBlock()))
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
        NbtCompound sourceTag = sourceEntity.writeNbt(new NbtCompound());
        sourceEntity.readNbt(new NbtCompound());

        world.setBlockState(dest, sourceState);
        BlockEntity destEntity = world.getBlockEntity(dest);
        if(destEntity != null) {
            //Write destination entity
            sourceTag.putInt("x", dest.getX());
            sourceTag.putInt("y", dest.getY());
            sourceTag.putInt("z", dest.getZ());
            destEntity.readNbt(sourceTag);
        }
    }
}
