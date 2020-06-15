package modfest.soulflame.block.rune;

import modfest.soulflame.SoulFlame;
import modfest.soulflame.init.ModStatusEffects;
import modfest.soulflame.util.ConduitUtil;
import modfest.soulflame.util.TaintPacket;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class SoulTeleportBlock extends SoulExtractionBlock {
    private final boolean canSend;

    public SoulTeleportBlock(boolean canSend) {
        super(canSend ? 100 : 200, canSend ? 2 : 1);
        this.canSend = canSend;
    }

    @Override
    protected boolean activate(World world, BlockPos pos, BlockPos pipe, Entity entity, PlayerEntity player) {
        if(canSend)
            return super.activate(world, pos, pipe, entity, player);
        else
            error(player, "send");
        return false;
    }

    @Override
    public boolean insert(BlockPos pos, BlockView world, Object value) {
        Direction flipped = flipside(world, pos);
        if(value instanceof Entity && testCage(world, pos, flipped, null) > 0) {
            int vertical = (flipped == Direction.UP) ? 1 : -(int)Math.ceil(((Entity) value).getHeight());
            ((Entity) value).teleport(pos.getX() + 0.5, pos.getY() + vertical, pos.getZ() + 0.5);

            TaintPacket taint = new TaintPacket(1);
            if(ConduitUtil.locateSink(world, getPipe(world, pos), taint) == null && world instanceof World)
                    taint.spawn((World) world, pos.up(vertical));
            return true;
        }
        return false;
    }
}
