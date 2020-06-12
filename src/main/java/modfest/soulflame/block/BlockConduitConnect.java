package modfest.soulflame.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Optional;

public interface BlockConduitConnect<T> {

    boolean canConnectConduitTo(BlockPos pos, BlockView world, Direction side);

    Optional<T> extract(BlockPos pos, World world, boolean simulate);

    boolean insert(BlockPos pos, World world, T value, boolean simulate);
}
