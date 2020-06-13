package modfest.soulflame.block.runic;

import modfest.soulflame.SoulFlame;
import modfest.soulflame.block.BlockConduitConnect;
import modfest.soulflame.util.ConduitUtil;
import modfest.soulflame.util.NeighborList;
import modfest.soulflame.util.SoulTank;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class HealCenterBlock extends RuneCenterBlock implements BlockConduitConnect {
    private static final int REQUIRED_TEARS = 50;
    
    public HealCenterBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean activate(World world, BlockPos pos, LivingEntity entity) {
        int i = world.getBlockState(pos).get(PIPE);
        if(i == 8)
            return false;

        //Collect required tears
        Object tank = ConduitUtil.locateSource(world, pos.add(NeighborList.platform[i]));
        if(tank instanceof SoulTank && ((SoulTank) tank).getTears() > REQUIRED_TEARS) {
            ((SoulTank) tank).removeTears(REQUIRED_TEARS);
            entity.heal(2);
            SoulFlame.LOGGER.info("Used heal rune");
        }
        return true;
    }

    @Override
    public boolean canConnectConduitTo(BlockPos pos, BlockView world, Direction side) {
        return false;
    }

    @Override
    public Object extract(BlockPos pos, World world, boolean simulate) {
        return null;
    }

    @Override
    public boolean insert(BlockPos pos, World world, Object value, boolean simulate) {
        if(value instanceof SoulTank)
            return ((SoulTank) value).getTears() > REQUIRED_TEARS;
        return false;
    }
}
