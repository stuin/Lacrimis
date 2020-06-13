package modfest.soulflame.block.runic;

import modfest.soulflame.block.Activatable;
import modfest.soulflame.util.NeighborList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public abstract class RuneCenterBlock extends Block implements Activatable {
    private static final Box box = new Box(-0.5, 1, -0.5, 1.5, 3, 1.5);

    public static final IntProperty PIPE;

    public RuneCenterBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(PIPE, 8));
    }

    public boolean testCage(World world, BlockPos pos) {
        boolean pipe = false;
        for(BlockPos next : NeighborList.platform) {
            Block block = world.getBlockState(pos.add(next)).getBlock();
            if(!(block instanceof RuneBlock && ((RuneBlock) block).testCage(world, pos)))
                return false;

            //Only one pipe allowed
            if(block instanceof PipeRuneBlock) {
                if(pipe) {
                    world.setBlockState(next,
                            world.getBlockState(next).with(RuneBlock.CENTER, 8));
                    return false;
                }
                pipe = true;
            }
        }
        return true;
    }

    public boolean activate(World world, BlockPos pos) {
        if(!testCage(world, pos))
            return false;
        //For all entities on platform
        for(Entity entity : world.getEntities(null, box.offset(pos))) {
            if(entity instanceof LivingEntity)
                return activate(world, pos, (LivingEntity) entity);
        }
        return false;
    }

    public abstract boolean activate(World world, BlockPos pos, LivingEntity entity);

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(PIPE);
    }

    static {
        PIPE = Properties.LEVEL_8;
    }
}
