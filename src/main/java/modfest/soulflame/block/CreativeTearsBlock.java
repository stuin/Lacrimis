package modfest.soulflame.block;

import modfest.soulflame.util.SoulTank;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class CreativeTearsBlock extends Block implements BlockConduitConnect {
    private static final int MAX = 1000;
    private static final SoulTank tank = new SoulTank(MAX);

    public CreativeTearsBlock(Settings settings) {
        super(settings);
        tank.setTears(Integer.MAX_VALUE);
        tank.addListener(() -> {
            if(tank.getTears() < MAX)
                tank.setTears(MAX);
        });
    }

    @Override
    public boolean canConnectConduitTo(BlockPos pos, BlockView world, Direction side) {
        return true;
    }

    @Override
    public Object extract(BlockPos pos, BlockView world) {
        return tank;
    }

    @Override
    public boolean insert(BlockPos pos, BlockView world, Object value) {
        if(value instanceof SoulTank) {
            ((SoulTank) value).removeTears(10);
            return true;
        }
        return false;
    }
}
