package modfest.soulflame.block.runic;

import modfest.soulflame.SoulFlame;
import net.minecraft.entity.LivingEntity;

public class HealCenterBlock extends RunicCenterBlock {
    public HealCenterBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean activate(LivingEntity entity) {
        entity.heal(2);
        SoulFlame.LOGGER.info("Used heal rune");
        return true;
    }
}
