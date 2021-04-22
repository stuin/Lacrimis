package modfest.lacrimis.util;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.init.ModStatusEffects;

public class TaintPacket {
    private final int amount;

    public TaintPacket(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void spawn(World world, BlockPos pos) {
        if(amount > 0 && !world.isClient) {
            AreaEffectCloudEntity entity = new AreaEffectCloudEntity(world, pos.getX(), pos.getY(), pos.getZ());
            entity.setRadius(amount / 150.0f);
            entity.addEffect(new StatusEffectInstance(ModStatusEffects.TEAR_POISON, amount));
            entity.setDuration(300);
            world.spawnEntity(entity);

            Lacrimis.LOGGER.debug("Spawned taint {} at {}", amount, pos.toString());
        }
    }
}
