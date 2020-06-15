package modfest.soulflame.init;

import modfest.soulflame.SoulFlame;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModSounds {
    public static SoundEvent BUBBLES;

    public static void register() {
        BUBBLES = register("bubbles", new SoundEvent(new Identifier(SoulFlame.MODID, "bubbles")));
    }

    private static <T extends SoundEvent> T register(String name, T soundEvent) {
        return Registry.register(Registry.SOUND_EVENT, new Identifier(SoulFlame.MODID, name), soundEvent);
    }

}
