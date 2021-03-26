package modfest.lacrimis.block.rune;

import modfest.lacrimis.util.DuctUtil;
import modfest.lacrimis.util.TaintPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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
    protected boolean activate(World world, BlockPos pos, BlockPos duct, Entity entity, PlayerEntity player) {
        if(canSend)
            return super.activate(world, pos, duct, entity, player);
        else
            error(player, "send");
        return false;
    }

    @Override
    public boolean insert(BlockPos pos, BlockView world, Object value) {
        Direction flipped = flipside(world, pos);
        if(value instanceof Entity && testCage(world, pos, flipped, null) > 0) {
            //Teleport entity
            int vertical = flipped == Direction.UP ? 1 : -(int)Math.ceil(((Entity) value).getHeight());
            if(value instanceof LivingEntity)
                ((LivingEntity) value).teleport(pos.getX() + 0.5, pos.getY() + vertical, pos.getZ() + 0.5, true);
            else
                ((Entity) value).teleport(pos.getX() + 0.5, pos.getY() + vertical, pos.getZ() + 0.5);

            //Spawn taint
            TaintPacket taint = new TaintPacket(requiredTears);
            if(DuctUtil.locateSink(world, getDuct(world, pos), taint) == null && world instanceof World)
                    taint.spawn((World) world, pos.up(vertical));
            return true;
        }
        return false;
    }
}
