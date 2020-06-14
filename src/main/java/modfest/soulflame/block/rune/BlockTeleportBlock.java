package modfest.soulflame.block.rune;

import modfest.soulflame.SoulFlame;
import modfest.soulflame.util.ConduitUtil;
import modfest.soulflame.util.SoulTank;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BlockTeleportBlock extends CenterRuneBlock {
    private static final int REQUIRED_TEARS = 300;

    @Override
    public boolean activate(World world, BlockPos pos, LivingEntity entity, PlayerEntity player) {
        //Collect required tears
        Object tank = ConduitUtil.locateSource(world, pipePos(world, pos), SoulTank.source(REQUIRED_TEARS));
        BlockState source = world.getBlockState(pos.up());
        if(tank instanceof SoulTank) {
            if(!source.isAir()) {
                BlockPos destination = ConduitUtil.locateSink(world, pipePos(world, pos), pos.up());
                if(destination != null) {
                    ((SoulTank) tank).removeTears(REQUIRED_TEARS);

                    if(!world.isClient)
                        SoulFlame.LOGGER.info("Block Moved");
                } else
                    error(player, "destination");
            } else
                error(player, "target");
        } else
            error(player, "tears");
        return true;
    }

    @Override
    public boolean insert(BlockPos pos, BlockView world, Object value) {
        if(world instanceof World && value instanceof BlockPos && testCage(world, pos)) {
            BlockState source = world.getBlockState((BlockPos) value);
            pos = pos.up();

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
