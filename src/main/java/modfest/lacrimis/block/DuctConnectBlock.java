package modfest.lacrimis.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public interface DuctConnectBlock {

    boolean canConnectDuctTo(BlockPos pos, BlockView world, Direction side);

    int extractTears(BlockPos pos, BlockView world, int request, boolean simulate);

    boolean insert(BlockPos pos, BlockView world, Object value);
}
