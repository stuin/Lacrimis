package modfest.soulflame.block.rune;

import modfest.soulflame.SoulFlame;
import modfest.soulflame.util.ConduitUtil;
import modfest.soulflame.util.SoulTank;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HealBlock extends CenterRuneBlock {
    public HealBlock() {
        super(50);
    }

    @Override
    public boolean activate(World world, BlockPos pos, LivingEntity entity, PlayerEntity player) {
        if(entity != null) {
            entity.heal(2);
            if(!world.isClient)
                SoulFlame.LOGGER.info("Entity Healed");
            return true;
        } else
            error(player, "entity");
        return true;
    }
}
