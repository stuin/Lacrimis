package modfest.soulflame.util;

import net.minecraft.util.math.BlockPos;

public class NeighborList {
    public static BlockPos[] platform = new BlockPos[] {
            new BlockPos(-1, 0, -1),
            new BlockPos(-1, 0, 0),
            new BlockPos(-1, 0, 1),
            new BlockPos(0, 0, -1),
            new BlockPos(1, 0, 1),
            new BlockPos(1, 0, 0),
            new BlockPos(1, 0, -1),
            new BlockPos(0, 0, 1),
    };
}
