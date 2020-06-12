package modfest.soulflame.block.runic;

import modfest.soulflame.block.Activatable;
import modfest.soulflame.util.NeighborList;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public abstract class RunicCenterBlock extends Block implements Activatable {
    private static final Box box = new Box(-0.5, 1, -0.5, 1.5, 3, 1.5);

    public RunicCenterBlock(Settings settings) {
        super(settings);
    }

    public boolean testCage(World world, BlockPos pos) {
        for(BlockPos next : NeighborList.platform) {
            Block block = world.getBlockState(pos.add(next)).getBlock();
            if(!(block instanceof RuneBlock && ((RuneBlock) block).testCage(world, pos)))
                return false;
        }
        return true;
    }

    public boolean activate(World world, BlockPos pos) {
        if(!testCage(world, pos))
            return false;
        for(Entity entity : world.getEntities(null, box.offset(pos))) {
            if(entity instanceof LivingEntity)
                return activate((LivingEntity) entity);
        }
        return false;
    }

    public abstract boolean activate(LivingEntity entity);
}
