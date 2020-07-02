package azoth.common;

import azoth.Azoth;
import azoth.client.particles.AzothMistParticle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.registry.Registry;

public class AzothParticles {
    public static final DefaultParticleType AZOTH_MIST = new AzothParticleType(true);

    public static void register() {
        Registry.register(Registry.PARTICLE_TYPE, Azoth.createID("azoth_mist"), AZOTH_MIST);
    }

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        ParticleFactoryRegistry.getInstance().register(AZOTH_MIST, AzothMistParticle.Factory::new);
    }

    private static final class AzothParticleType extends DefaultParticleType {
        private AzothParticleType(boolean alwaysSpawn) {
            super(alwaysSpawn);
        }
    }
}
