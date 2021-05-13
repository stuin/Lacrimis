package modfest.lacrimis.compat.rei;

import me.shedaniel.rei.server.ContainerInfoHandler;
import net.minecraft.util.Identifier;

public class LacrimisContainerPlugin implements Runnable {
    public void run() {
        ContainerInfoHandler.registerContainerInfo(new Identifier("lacrimis", "infusion"),
                new InfusionContainerInfo());
        ContainerInfoHandler.registerContainerInfo(new Identifier("lacrimis", "crucible"),
                new InfusionContainerInfo());
        ContainerInfoHandler.registerContainerInfo(new Identifier("minecraft", "plugins/crafting"),
                new InfusionContainerInfo());
    }
}
