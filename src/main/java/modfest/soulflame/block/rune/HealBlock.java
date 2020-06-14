package modfest.soulflame.block.rune;

import modfest.soulflame.SoulFlame;
import modfest.soulflame.util.ConduitUtil;
import modfest.soulflame.util.SoulTank;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HealBlock extends CenterRuneBlock {
    private static final int REQUIRED_TEARS = 50;

    @Override
    public boolean activate(World world, BlockPos pos, LivingEntity entity, PlayerEntity player) {
        //Collect required tears
        Object tank = ConduitUtil.locateSource(world, pipePos(world, pos), SoulTank.source(REQUIRED_TEARS));
        if(tank instanceof SoulTank) {
            if(entity != null) {
                ((SoulTank) tank).removeTears(REQUIRED_TEARS);
                entity.heal(2);
                if(!world.isClient)
                    SoulFlame.LOGGER.info("Entity Healed");
            } else
                error(player, "entity");
        } else
            error(player, "tears");
        return true;
    }
}
