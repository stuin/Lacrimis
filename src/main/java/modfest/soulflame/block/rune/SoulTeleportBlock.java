package modfest.soulflame.block.rune;

import modfest.soulflame.init.ModStatusEffects;
import modfest.soulflame.util.ConduitEntry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;

public class SoulTeleportBlock extends SoulExtractionBlock {
    private final boolean canSend;

    public SoulTeleportBlock(boolean canSend) {
        super(canSend ? 100 : 200, canSend ? 2 : 1);
        this.canSend = canSend;
    }

    @Override
    protected boolean activate(World world, BlockPos pos, BlockPos pipe, LivingEntity entity, PlayerEntity player) {
        if(canSend)
            return super.activate(world, pos, pipe, entity, player);
        else
            error(player, "send");
        return false;
    }

    @Override
    public boolean insert(BlockPos pos, BlockView world, Object value) {
        Direction flipped = flipside(world, pos);
        if(value instanceof LivingEntity && testCage(world, pos, flipped, null) > 0) {
            int vertical = (flipped == Direction.UP) ? 1 : -(int)Math.ceil(((LivingEntity) value).getHeight());
            ((LivingEntity) value).teleport(pos.getX() + 0.5, pos.getY() + vertical, pos.getZ() + 0.5);
            ((LivingEntity) value).addStatusEffect(new StatusEffectInstance(ModStatusEffects.WAVERING_SOUL, 50));
            return true;
        }
        return false;
    }
}
