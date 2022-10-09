package com.stuintech.lacrimis.init;

import com.stuintech.lacrimis.Lacrimis;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModParticles {
    public static final DefaultParticleType PURPLE_MIST = new DefaultParticleType(true) {};

    public static final DefaultParticleType OBSIDIAN_TEAR_FLYING = new DefaultParticleType(true) {};

    public static void register() {
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(Lacrimis.MODID, "purple_mist"), PURPLE_MIST);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(Lacrimis.MODID, "obsidian_tear_flying"), OBSIDIAN_TEAR_FLYING);
    }
}
