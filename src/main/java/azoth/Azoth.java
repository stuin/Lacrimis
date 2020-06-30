package azoth;

import azoth.common.AzothBlocks;
import azoth.common.AzothItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class Azoth implements ModInitializer {
    public static final String MODID = "azoth";

    @Override
    public void onInitialize() {
        AzothBlocks.register();
        AzothItems.register();
    }

    public static final Identifier createID(String name) {
        return new Identifier(MODID, name);
    }
}
