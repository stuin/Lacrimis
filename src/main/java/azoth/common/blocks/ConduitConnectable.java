package azoth.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public interface ConduitConnectable {
    boolean connectsToConduit(BlockState state, Direction side);

    default boolean isSource() {
        return false;
    }

    default boolean isSink() {
        return false;
    }

    default int tryReceive(World world, BlockPos pos, int amount) {
        // return excess
        return 0;
    }
}
