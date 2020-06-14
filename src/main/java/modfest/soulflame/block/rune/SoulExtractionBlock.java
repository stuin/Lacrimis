package modfest.soulflame.block.rune;

import modfest.soulflame.SoulFlame;
import modfest.soulflame.util.ConduitUtil;
import modfest.soulflame.util.SoulTank;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SoulExtractionBlock extends CenterRuneBlock {
    private static final int REQUIRED_TEARS = 200;

    @Override
    public boolean activate(World world, BlockPos pos, LivingEntity entity, PlayerEntity player) {
        //Collect required tears
        Object tank = ConduitUtil.locateSource(world, pipePos(world, pos), SoulTank.source(REQUIRED_TEARS));
        if(tank instanceof SoulTank) {
            if(entity != null) {
                BlockPos destination = ConduitUtil.locateSink(world, pipePos(world, pos), entity);
                if(destination != null) {
                    ((SoulTank) tank).removeTears(REQUIRED_TEARS);

                    if(!world.isClient)
                        SoulFlame.LOGGER.info("Soul Extracted");
                } else
                    error(player, "destination");
            } else
                error(player, "target");
        } else
            error(player, "tears");
        return true;
    }
}
