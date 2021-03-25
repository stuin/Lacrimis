package modfest.lacrimis.block.rune;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import modfest.lacrimis.Lacrimis;

public class HealBlock extends CenterRuneBlock {
    public HealBlock() {
        super(50, 1);
    }

    @Override
    protected boolean activate(World world, BlockPos pos, BlockPos duct, Entity entity, PlayerEntity player) {
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            if (livingEntity.getHealth() < livingEntity.getMaxHealth()) {
                livingEntity.heal(2);
                if (!world.isClient)
                    Lacrimis.LOGGER.debug("Entity Healed");
                return true;
            } else
                error(player, "entity");
        } else
            error(player, "entity");
        return true;
    }
}
