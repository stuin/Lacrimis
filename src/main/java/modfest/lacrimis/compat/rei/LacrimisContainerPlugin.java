package modfest.lacrimis.compat.rei;

import me.shedaniel.rei.plugin.containers.CraftingContainerInfoWrapper;
import me.shedaniel.rei.server.ContainerInfoHandler;
import modfest.lacrimis.crafting.CombinerScreenHandler;
import modfest.lacrimis.crafting.InfusionScreenHandler;
import net.minecraft.util.Identifier;

public class LacrimisContainerPlugin implements Runnable {
    public void run() {
        ContainerInfoHandler.registerContainerInfo(new Identifier("lacrimis", "infusion"),
                CraftingContainerInfoWrapper.create(InfusionScreenHandler.class));        ContainerInfoHandler.registerContainerInfo(new Identifier("lacrimis", "infusion"), CraftingContainerInfoWrapper.create(InfusionScreenHandler.class));
        ContainerInfoHandler.registerContainerInfo(new Identifier("lacrimis", "crucible"),
                CraftingContainerInfoWrapper.create(InfusionScreenHandler.class));
    }
}
