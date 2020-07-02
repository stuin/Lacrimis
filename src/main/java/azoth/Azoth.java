package azoth;

import java.util.Map;

import com.google.common.collect.MapMaker;

import azoth.common.AzothBlocks;
import azoth.common.AzothItems;
import azoth.common.AzothParticles;
import azoth.common.blocks.conduits.ConduitsManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class Azoth implements ModInitializer {
    public static final String MODID = "azoth";

    private static Map<World, ConduitsManager> conduits = new MapMaker().weakKeys().makeMap();

    public static final Identifier createID(String name) {
        return new Identifier(MODID, name);
    }

    public static ConduitsManager getConduitManager(World world) {
        if (!conduits.containsKey(world)) {
            conduits.put(world, new ConduitsManager(world));
        }
        return conduits.get(world);
    }

    @Override
    public void onInitialize() {
        AzothBlocks.register();
        AzothItems.register();
        AzothParticles.register();
    }

}
