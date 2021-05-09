package modfest.lacrimis.compat.rei;

import me.shedaniel.rei.server.ContainerInfo;
import modfest.lacrimis.crafting.InfusionScreenHandler;
import net.minecraft.screen.ScreenHandler;

public class InfusionContainerInfo implements ContainerInfo<InfusionScreenHandler> {
    @Override
    public Class<? extends ScreenHandler> getContainerClass() {
        return InfusionScreenHandler.class;
    }

    @Override
    public int getCraftingResultSlotIndex(InfusionScreenHandler container) {
        return container.getCraftingResultSlotIndex();
    }

    @Override
    public int getCraftingWidth(InfusionScreenHandler container) {
        return container.getCraftingWidth();
    }

    @Override
    public int getCraftingHeight(InfusionScreenHandler container) {
        return container.getCraftingHeight();
    }
}
