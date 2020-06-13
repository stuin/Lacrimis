package modfest.soulflame.block;

import modfest.soulflame.util.SoulTank;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Optional;

public class CreativeTearsBlock extends Block implements BlockConduitConnect {
    private static final int MAX = 1000;
    private static final SoulTank tank = new SoulTank(MAX);

    public CreativeTearsBlock(Settings settings) {
        super(settings);
        tank.setTears(Integer.MAX_VALUE);
        tank.setListener(() -> {
            if(tank.getTears() < MAX)
                tank.setTears(MAX);
        });
    }

    @Override
    public boolean canConnectConduitTo(BlockPos pos, BlockView world, Direction side) {
        return true;
    }

    @Override
    public Optional<SoulTank> extract(BlockPos pos, World world, boolean simulate) {
        return Optional.of(tank);
    }

    @Override
    public boolean insert(BlockPos pos, World world, Object value, boolean simulate) {
        if(value instanceof SoulTank) {
            ((SoulTank) value).removeTears(10);
            return true;
        }
        return false;
    }
}
