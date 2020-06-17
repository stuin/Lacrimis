package modfest.lacrimis.util;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.init.ModStatusEffects;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TaintPacket {
    private int amount = 0;

    public TaintPacket(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
    
    public void spawn(World world, BlockPos pos) {
        if(amount > 0) {
            AreaEffectCloudEntity entity = new AreaEffectCloudEntity(world, pos.getX(), pos.getY(), pos.getZ());
            entity.setRadius(amount / 150.0f);
            entity.addEffect(new StatusEffectInstance(ModStatusEffects.WAVERING_SOUL, amount * 2));
            entity.addEffect(new StatusEffectInstance(StatusEffects.POISON, amount));
            entity.setDuration(amount * 2);
            world.spawnEntity(entity);

            if(!world.isClient)
                Lacrimis.LOGGER.info("Spawned taint " + amount + " at " + pos.toString());
        }
    }
}
