package modfest.lacrimis.block.rune;

import modfest.lacrimis.entity.SoulShellEntity;
import modfest.lacrimis.util.DuctUtil;
import modfest.lacrimis.util.TaintPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;

public class SoulSwapBlock extends SoulExtractionBlock {
    public SoulSwapBlock() {
        super(50, 1);
    }

    @Override
    public boolean insert(BlockPos pos, BlockView world, Object value) {
        Direction flipped = flipside(world, pos);
        int tier = testCage(world, pos, flipped, null);
        if(value instanceof PlayerEntity && tier > 0 && world instanceof World) {
            List<SoulShellEntity> shells = ((World) world).getEntitiesByClass(
                    SoulShellEntity.class, getTargetBox(pos, flipped, tier), null);
            if(shells.size() > 0) {
                shells.get(0).swapWithPlayer((World) world, (PlayerEntity) value);

                //Spawn taint
                TaintPacket taint = new TaintPacket(actualCost(tier));
                if(DuctUtil.locateSink(world, getDuct(world, pos), taint) == null)
                    taint.spawn((World) world, shells.get(0).getBlockPos());
                return true;
            }
        }
        return false;
    }
}
