package modfest.soulflame.block.rune;

import modfest.soulflame.SoulFlame;
import modfest.soulflame.util.ConduitEntry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class HealBlock extends CenterRuneBlock {
    public HealBlock() {
        super(50, 1);
    }

    @Override
    protected boolean activate(World world, BlockPos pos, BlockPos pipe, LivingEntity entity, PlayerEntity player) {
        if(entity != null && entity.getHealth() < entity.getMaxHealth()) {
            entity.heal(2);
            if(!world.isClient)
                SoulFlame.LOGGER.info("Entity Healed");
            return true;
        } else
            error(player, "entity");
        return true;
    }
}
