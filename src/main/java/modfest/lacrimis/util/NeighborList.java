package modfest.lacrimis.util;

import net.minecraft.util.math.BlockPos;

public class NeighborList {
    public static BlockPos[] platform = new BlockPos[] {
            new BlockPos(-1, 0, -1),
            new BlockPos(-1, 0, 0),
            new BlockPos(-1, 0, 1),
            new BlockPos(0, 0, 1),
            new BlockPos(1, 0, 1),
            new BlockPos(1, 0, 0),
            new BlockPos(1, 0, -1),
            new BlockPos(0, 0, -1),
    };
    
    public static boolean isEdge(int i) {
        return i % 2 == 1;
    }
    
    public static int getOpposite(int i) {
        return (i + 4) % 8;
    }
}
