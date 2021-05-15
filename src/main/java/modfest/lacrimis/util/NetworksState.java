package modfest.lacrimis.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NetworksState extends PersistentState {
    private final Map<Integer, NetworkList> colorMap = new HashMap<>();

    public NetworksState() {
        super("lacrimis_network");
    }

    public static int getColor(float[] color) {
        return ((int)(color[0] * 255) << 16) + ((int)(color[1] * 255) << 8) + (int)(color[2] * 255);
    }

    public NetworkList getNetwork(int color) {
        return colorMap.getOrDefault(color, null);
    }

    public NetworkList addLink(int color, BlockPos pos, RegistryKey<World> worldKey) {
        NetworkList l;
        if(colorMap.containsKey(color))
            l = colorMap.get(color);
        else {
            l = new NetworkList();
            colorMap.put(color, l);
        }

        l.add(new Pair<>(worldKey, pos));
        return l;
    }

    @Override
    public void fromTag(CompoundTag tag) {

    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        return null;
    }

    public static class NetworkList extends ArrayList<Pair<RegistryKey<World>, BlockPos>> {
    }
}
