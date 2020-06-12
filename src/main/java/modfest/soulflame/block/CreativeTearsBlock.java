package modfest.soulflame.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CreativeTearsBlock extends Block implements BlockConduitConnect {

    public CreativeTearsBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canConnectConduitTo(BlockState state, BlockPos pos, BlockView world, Direction side) {
        return true;
    }

    @Override
    public int extract(BlockState state, BlockPos pos, World world, int amount, boolean simulate) {
        return amount;
    }

    @Override
    public int insert(BlockState state, BlockPos pos, World world, int amount, boolean simulate) {
        return amount;
    }

    @Override
    public int getMaxTearsAmount(BlockState state, BlockPos pos, BlockView world) {
        return 2147483647;
    }

    @Override
    public int getCurrentTearsAmount(BlockState state, BlockPos pos, BlockView world) {
        return 1073741823;
    }

}
