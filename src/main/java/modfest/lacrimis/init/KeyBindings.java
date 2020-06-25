package modfest.lacrimis.init;

import net.minecraft.client.options.KeyBinding;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

import modfest.lacrimis.Lacrimis;

public class KeyBindings {

    public static KeyBinding tarot;

    public static void initialize() {
        tarot = KeyBindingHelper.registerKeyBinding(new KeyBinding(Lacrimis.MODID + ".key.tarotAbility", 86, "key.categories.gameplay"));
    }

}
