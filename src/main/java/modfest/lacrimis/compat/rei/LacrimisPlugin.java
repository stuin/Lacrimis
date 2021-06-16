package modfest.lacrimis.compat.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoRegistry;
import modfest.lacrimis.Lacrimis;

public class LacrimisPlugin implements REIServerPlugin {
    public static CategoryIdentifier<InfusionDisplay<?>> INFUSION = CategoryIdentifier.of(Lacrimis.MODID, "plugins/infusion");
    public static CategoryIdentifier<CrucibleDisplay> CRUCIBLE = CategoryIdentifier.of(Lacrimis.MODID, "plugins/crucible");

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(INFUSION, InfusionDisplay.serializer(InfusionDisplay::new));
        registry.register(CRUCIBLE, CrucibleDisplay.serializer(CrucibleDisplay::new));
    }

    @Override
    public void registerMenuInfo(MenuInfoRegistry registry) {

    }
}
