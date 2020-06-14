package modfest.soulflame.block.rune;

import modfest.soulflame.SoulFlame;
import modfest.soulflame.util.ConduitUtil;
import modfest.soulflame.util.SoulTank;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SoulExtractionBlock extends CenterRuneBlock {
    public SoulExtractionBlock() {
        super(200);
    }

    @Override
    public boolean activate(World world, BlockPos pos, LivingEntity entity, PlayerEntity player) {
        if(entity != null) {
            BlockPos destination = ConduitUtil.locateSink(world, pipePos(world, pos), entity);
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
