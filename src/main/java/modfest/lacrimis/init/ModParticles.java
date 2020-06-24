package modfest.lacrimis.init;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.client.ObsidianTearFlyingParticle;
import modfest.lacrimis.client.PurpleMistParticle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.registry.Registry;

public class ModParticles {
    public static final DefaultParticleType PURPLE_MIST = new DefaultParticleType(true) {};

    public static final DefaultParticleType OBSIDIAN_TEAR_FLYING = new DefaultParticleType(true) {};

    public static void register() {
        Registry.register(Registry.PARTICLE_TYPE, Lacrimis.MODID + ":purple_mist", PURPLE_MIST);
        Registry.register(Registry.PARTICLE_TYPE, Lacrimis.MODID + ":obsidian_tear_flying", OBSIDIAN_TEAR_FLYING);
    }

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        ParticleFactoryRegistry.getInstance().register(PURPLE_MIST, PurpleMistParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(OBSIDIAN_TEAR_FLYING, ObsidianTearFlyingParticle.Factory::new);
    }
}
