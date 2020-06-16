package modfest.soulflame.util;

import modfest.soulflame.SoulFlame;
import modfest.soulflame.init.ModStatusEffects;
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
        AreaEffectCloudEntity entity = new AreaEffectCloudEntity(world, pos.getX(), pos.getY(), pos.getZ());
        entity.setRadius(amount / 150.0f);
        entity.addEffect(new StatusEffectInstance(ModStatusEffects.WAVERING_SOUL, amount * 2));
        entity.addEffect(new StatusEffectInstance(StatusEffects.POISON, amount));
        entity.setDuration(amount * 2);
        world.spawnEntity(entity);

        if(!world.isClient)
            SoulFlame.LOGGER.info("Spawned taint at " + pos.toString());
    }
}
