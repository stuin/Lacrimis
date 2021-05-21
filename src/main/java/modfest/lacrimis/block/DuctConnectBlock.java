package modfest.lacrimis.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public interface DuctConnectBlock {

    boolean canConnectDuctTo(BlockPos pos, WorldAccess world, Direction side);

    int extractTears(BlockPos pos, World world, int request, boolean simulate);

    boolean insert(BlockPos pos, World world, Object value);
}
