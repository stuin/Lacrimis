package azoth.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;

public interface ConduitConnectable {
    boolean connectsToConduit(BlockState state, Direction side);
}
