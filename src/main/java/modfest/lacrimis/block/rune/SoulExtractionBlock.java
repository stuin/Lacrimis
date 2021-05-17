package modfest.lacrimis.block.rune;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import modfest.lacrimis.util.DuctUtil;

public class SoulExtractionBlock extends CenterRuneBlock {

    public SoulExtractionBlock() {
        this(200, 1);
    }

    public SoulExtractionBlock(int requiredTears, int requiredTier) {
        super(requiredTears, requiredTier);
    }

    @Override
    protected boolean onActivate(World world, BlockPos pos, BlockPos duct, Entity entity, PlayerEntity player) {
        if(entity instanceof LivingEntity || entity instanceof ItemEntity) {
            BlockPos destination = DuctUtil.locateSink(world, duct, entity);
            if(destination != null)
                return true;
            else
                error(player, "destination");
        } else
            error(player, "entity");
        return false;
    }
}
