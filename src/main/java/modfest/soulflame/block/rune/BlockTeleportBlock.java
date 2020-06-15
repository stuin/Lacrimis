package modfest.soulflame.block.rune;

import modfest.soulflame.SoulFlame;
import modfest.soulflame.util.ConduitEntry;
import modfest.soulflame.util.ConduitUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;

public class BlockTeleportBlock extends CenterRuneBlock {
    public BlockTeleportBlock() {
        super(300, 2);
    }

    @Override
    protected boolean activate(World world, BlockPos pos, List<ConduitEntry> list, LivingEntity entity, PlayerEntity player) {
        BlockState source = world.getBlockState(pos);
        if(!source.isAir()) {
            BlockPos destination = ConduitUtil.locateSink(world, list, pos);
            if(destination != null) {
                if(!world.isClient)
                    SoulFlame.LOGGER.info("Block Moved");
                return true;
            } else
                error(player, "destination");
        } else
            error(player, "block");
        return false;
    }

    @Override
    public boolean insert(BlockPos pos, BlockView world, Object value) {
        Direction flipped = flipside(world, pos);
        if(world instanceof World && value instanceof BlockPos && testCage(world, pos, flipped)) {
            BlockState source = world.getBlockState((BlockPos) value);
            pos = pos.offset(flipped);

            //Set destination block
            if(world.getBlockState(pos).isAir()) {
                ((World) world).setBlockState(pos, source);
                BlockEntity sourceEntity = world.getBlockEntity((BlockPos) value);
                BlockEntity destEntity = world.getBlockEntity(pos);

                //Copy entity
                if(sourceEntity != null && destEntity != null) {
                    CompoundTag tag = sourceEntity.toTag(new CompoundTag());
                    sourceEntity.fromTag(source, new CompoundTag());

                    //Write entity
                    tag.putInt("x", pos.getX());
                    tag.putInt("y", pos.getY());
                    tag.putInt("z", pos.getZ());
                    destEntity.fromTag(source, tag);
                }
                ((World) world).setBlockState((BlockPos) value, Blocks.AIR.getDefaultState());
                return true;
            }
        }
        return false;
    }
}
