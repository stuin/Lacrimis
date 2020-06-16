package modfest.lacrimis.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public interface BlockConduitConnect {

    boolean canConnectConduitTo(BlockPos pos, BlockView world, Direction side);

    Object extract(BlockPos pos, BlockView world);
    
    int extractTears(BlockPos pos, BlockView world, int request, boolean simulate);

    boolean insert(BlockPos pos, BlockView world, Object value);
}
