package modfest.soulflame.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public interface BlockConduitConnect {

    boolean canConnectConduitTo(BlockState state, BlockPos pos, BlockView world, Direction side);

    int extract(BlockState state, BlockPos pos, World world, int amount, boolean simulate);

    int insert(BlockState state, BlockPos pos, World world, int amount, boolean simulate);

    int getMaxTearsAmount(BlockState state, BlockPos pos, BlockView world);

    int getCurrentTearsAmount(BlockState state, BlockPos pos, BlockView world);

}
