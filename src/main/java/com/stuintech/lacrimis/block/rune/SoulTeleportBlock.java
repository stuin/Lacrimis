package com.stuintech.lacrimis.block.rune;

import com.stuintech.lacrimis.util.DuctUtil;
import com.stuintech.lacrimis.util.TaintPacket;
import com.stuintech.lacrimis.init.ModStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class SoulTeleportBlock extends SoulExtractionBlock {
    private final boolean canSend;

    public SoulTeleportBlock(boolean canSend) {
        super(canSend ? 100 : 200, canSend ? 2 : 1);
        this.canSend = canSend;
    }

    @Override
    protected boolean onActivate(World world, BlockPos pos, BlockPos duct, Entity entity, PlayerEntity player) {
        if(canSend)
            return super.onActivate(world, pos, duct, entity, player);
        else
            error(player, "send");
        return false;
    }

    @Override
    public boolean insert(BlockPos pos, World world, Object value) {
        Direction flipped = flipside(world, pos);
        int tier = testCage(world, pos, flipped, null);
        if(value instanceof Entity && tier > 0) {
            //Teleport entity
            int vertical = (flipped == Direction.UP) ? 1 : -(int)Math.ceil(((Entity) value).getHeight());
            if(world.getBlockState(pos.add(0, vertical + 1, 0)).isAir()) {
                if(value instanceof LivingEntity) {
                    ((LivingEntity) value).setPosition(pos.getX() + 0.5, pos.getY() + vertical, pos.getZ() + 0.5);
                    ((LivingEntity) value).addStatusEffect(new StatusEffectInstance(ModStatusEffects.WAVERING_SOUL, 15 * 20));
                } else
                    ((Entity) value).setPosition(pos.getX() + 0.5, pos.getY() + vertical, pos.getZ() + 0.5);

                //Spawn taint
                TaintPacket taint = new TaintPacket(actualCost(tier));
                if(DuctUtil.locateSink(world, getDuct(world, pos), taint) == null)
                    taint.spawn(world, pos.up(vertical));
                return true;
            }
        }
        return false;
    }
}
