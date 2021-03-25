package modfest.lacrimis.block;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class CreativeTearsBlock extends Block implements DuctConnectBlock {

    public CreativeTearsBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canConnectDuctTo(BlockPos pos, BlockView world, Direction side) {
        return true;
    }

    @Override
    public Object extract(BlockPos pos, BlockView world) {
        return null;
    }

    @Override
    public int extractTears(BlockPos pos, BlockView world, int request, boolean simulate) {
        return request;
    }

    @Override
    public boolean insert(BlockPos pos, BlockView world, Object value) {
        return false;
    }
}
