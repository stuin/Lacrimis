package modfest.soulflame.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public interface BlockConduitConnect {

    boolean canConnectConduitTo(BlockPos pos, BlockView world, Direction side);

    Object extract(BlockPos pos, World world, boolean simulate);

    boolean insert(BlockPos pos, World world, Object value, boolean simulate);
}
