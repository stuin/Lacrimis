package modfest.lacrimis.block.rune;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.util.ConduitUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SoulExtractionBlock extends CenterRuneBlock {
    
    public SoulExtractionBlock() {
        this(200,1);
    }

    public SoulExtractionBlock(int requiredTears, int requiredTier) {
        super(requiredTears, requiredTier);
    }

    @Override
    protected boolean activate(World world, BlockPos pos, BlockPos pipe, Entity entity, PlayerEntity player) {
        if(entity instanceof LivingEntity || entity instanceof ItemEntity) {
            BlockPos destination = ConduitUtil.locateSink(world, pipe, entity);
            if(destination != null) {
                if(!world.isClient)
                    Lacrimis.LOGGER.info("Soul Extracted");
                return true;
            } else
                error(player, "destination");
        } else
            error(player, "entity");
        return true;
    }
}
