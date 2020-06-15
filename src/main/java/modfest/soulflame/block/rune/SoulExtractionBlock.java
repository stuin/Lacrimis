package modfest.soulflame.block.rune;

import modfest.soulflame.SoulFlame;
import modfest.soulflame.util.ConduitEntry;
import modfest.soulflame.util.ConduitUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class SoulExtractionBlock extends CenterRuneBlock {
    
    public SoulExtractionBlock() {
        this(200,1);
    }

    public SoulExtractionBlock(int requiredTears, int requiredTier) {
        super(requiredTears, requiredTier);
    }

    @Override
    protected boolean activate(World world, BlockPos pos, BlockPos pipe, LivingEntity entity, PlayerEntity player) {
        if(entity != null) {
            BlockPos destination = ConduitUtil.locateSink(world, pipe, entity);
            if(destination != null) {
                if(!world.isClient)
                    SoulFlame.LOGGER.info("Soul Extracted");
                return true;
            } else
                error(player, "destination");
        } else
            error(player, "entity");
        return true;
    }
}
