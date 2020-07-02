package azoth.common.blocks.conduits;

import java.util.List;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class ConduitPath {
    public final BlockPos end;
    public final Direction direction;
    public final int length;

    public final List<BlockPos> conduits;

    public ConduitPath(BlockPos end, Direction direction, int length, List<BlockPos> conduits) {
        this.end = end;
        this.direction = direction;
        this.length = length;
        this.conduits = conduits;
    }
}
