package com.stuintech.lacrimis.init;

import com.stuintech.lacrimis.Lacrimis;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModSounds {
    public static SoundEvent BUBBLES;

    public static void register() {
        BUBBLES = register("bubbles", new SoundEvent(new Identifier(Lacrimis.MODID, "bubbles")));
    }

    private static <T extends SoundEvent> T register(String name, T soundEvent) {
        return Registry.register(Registry.SOUND_EVENT, new Identifier(Lacrimis.MODID, name), soundEvent);
    }

}
