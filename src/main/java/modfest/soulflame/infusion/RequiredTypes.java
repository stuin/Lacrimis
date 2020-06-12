package modfest.soulflame.infusion;

import modfest.soulflame.SoulFlame;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;

public class RequiredTypes {
    public static ScreenHandlerType<InfusionScreenHandler> infusionScreen;
    
    public static void register() {
        infusionScreen = Registry.register(Registry.SCREEN_HANDLER, SoulFlame.MODID + ":infusion",
                new ScreenHandlerType<>(InfusionScreenHandler::new));
    }
}
