package modfest.lacrimis.util;

import modfest.lacrimis.Lacrimis;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Identifier;
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
        Lacrimis.LOGGER.warn("Lacrimis Networking");
    }

    public static int getColor(float[] color) {
        return ((int)(color[0] * 255) << 16) + ((int)(color[1] * 255) << 8) + (int)(color[2] * 255);
    }

    public NetworkList getNetwork(int color) {
        return colorMap.getOrDefault(color, null);
    }

    public NetworkList addLink(int color, Pair<RegistryKey<World>, BlockPos> pair) {
        NetworkList l;
        if(colorMap.containsKey(color))
            l = colorMap.get(color);
        else {
            l = new NetworkList(color);
            colorMap.put(color, l);
        }

        l.add(pair);
        return l;
    }

    public void removeLink(int color, Pair<RegistryKey<World>, BlockPos> pair) {
        if(colorMap.containsKey(color))
            colorMap.get(color).remove(pair);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        for(Tag networkTag : tag.getList("list", 1)) {
            int color = ((CompoundTag)networkTag).getInt("color");
            NetworkList list = new NetworkList(color);
            for(Tag pairTag : ((CompoundTag) networkTag).getList("list", 1)) {
                RegistryKey<World> key = RegistryKey.of(RegistryKey.ofRegistry(Identifier.tryParse("dimension")), Identifier.tryParse(((CompoundTag) pairTag).getString("world")));
                int x = ((CompoundTag) pairTag).getInt("X");
                int y = ((CompoundTag) pairTag).getInt("Y");
                int z = ((CompoundTag) pairTag).getInt("Z");
                list.add(new Pair<>(key, new BlockPos(x, y, z)));
            }
            colorMap.put(color, list);
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        ListTag list = new ListTag();
        for (NetworkList network : colorMap.values()) {
            ListTag linkList = new ListTag();
            for(Pair<RegistryKey<World>, BlockPos> pair : network) {
                CompoundTag pairTag = new CompoundTag();
                pairTag.putString("world", pair.getLeft().getValue().toString());
                pairTag.putInt("X", pair.getRight().getX());
                pairTag.putInt("Y", pair.getRight().getY());
                pairTag.putInt("Z", pair.getRight().getZ());
                linkList.add(pairTag);
            }

            CompoundTag networkTag = new CompoundTag();
            networkTag.putInt("color", network.color);
            networkTag.put("list", linkList);
            list.add(networkTag);
        }
        tag.put("list", list);
        return tag;
    }

    public static class NetworkList extends ArrayList<Pair<RegistryKey<World>, BlockPos>> {
        public int color;

        public NetworkList(int color) {
            this.color = color;
        }
    }
}
