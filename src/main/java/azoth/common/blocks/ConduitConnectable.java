package azoth.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;

public interface ConduitConnectable {
    boolean connectsToConduit(BlockState state, Direction side);

    default boolean isSource() {
        return false;
    }

    default boolean isSink() {
        return false;
    }

    default int tryReceive(int amount) {
        // return excess
        return 0;
    }
}
