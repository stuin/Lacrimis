package modfest.lacrimis.util;

import modfest.lacrimis.Lacrimis;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NetworksState extends PersistentState {
    public static final String KEY = "lacrimis_network";
    private final Map<Integer, NetworkList> colorMap = new HashMap<>();

    public NetworkList getNetwork(int color) {
        return colorMap.getOrDefault(color, null);
    }

    public NetworkList addLink(int color, BlockPos pos) {
        NetworkList l;
        if(colorMap.containsKey(color))
            l = colorMap.get(color);
        else {
            l = new NetworkList(color);
            colorMap.put(color, l);
        }

        if(!l.contains(pos))
            l.add(pos);
        markDirty();
        return l;
    }

    public void removeLink(int color, BlockPos pos) {
        if(colorMap.containsKey(color)) {
            colorMap.get(color).remove(pos);
            markDirty();
        }
    }

    public static NetworksState fromNbt(NbtCompound tag) {
        NetworksState state = new NetworksState();
        for(NbtElement networkTag : tag.getList("list", 10)) {
            int color = ((NbtCompound)networkTag).getInt("color");
            NetworkList list = new NetworkList(color);
            for(NbtElement pairTag : ((NbtCompound) networkTag).getList("list", 10)) {
                int x = ((NbtCompound) pairTag).getInt("X");
                int y = ((NbtCompound) pairTag).getInt("Y");
                int z = ((NbtCompound) pairTag).getInt("Z");
                BlockPos pos = new BlockPos(x, y, z);
                if(!list.contains(pos))
                    list.add(pos);
            }
            state.colorMap.put(color, list);
            Lacrimis.LOGGER.info("Loading network " + color + " " + list);
        }
        return state;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        NbtList list = new NbtList();
        for (NetworkList network : colorMap.values()) {
            if(network.size() > 0) {
                NbtList linkList = new NbtList();
                for (BlockPos pos : network) {
                    NbtCompound pairTag = new NbtCompound();
                    pairTag.putInt("X", pos.getX());
                    pairTag.putInt("Y", pos.getY());
                    pairTag.putInt("Z", pos.getZ());
                    linkList.add(pairTag);
                }

                NbtCompound networkTag = new NbtCompound();
                networkTag.putInt("color", network.color);
                networkTag.put("list", linkList);
                list.add(networkTag);
            }
        }
        tag.put("list", list);
        return tag;
    }

    public static class NetworkList extends ArrayList<BlockPos> {
        public int color;

        public NetworkList(int color) {
            this.color = color;
        }
    }
}
