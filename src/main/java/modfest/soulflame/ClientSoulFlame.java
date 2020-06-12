package modfest.soulflame;

import modfest.soulflame.infusion.InfusionScreen;
import modfest.soulflame.infusion.RequiredTypes;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class ClientSoulFlame implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

    }

    static {
        HandledScreens.register(RequiredTypes.infusionScreen, InfusionScreen::new);
    }
}
