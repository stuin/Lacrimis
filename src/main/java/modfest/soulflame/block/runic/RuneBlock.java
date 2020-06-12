package modfest.soulflame.block.runic;

import jdk.internal.jline.internal.Nullable;
import modfest.soulflame.block.Activatable;
import modfest.soulflame.util.NeighborList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RuneBlock extends Block implements Activatable {
    private static final BooleanProperty POWERED;
    private static final IntProperty CENTER;
    private final int tier;

    public RuneBlock(Settings settings, int tier) {
        super(settings);
        this.tier = tier;
        setDefaultState(getStateManager().getDefaultState().with(POWERED, false).with(CENTER, 8));
    }

    public boolean testCage(World world, BlockPos pos) {
        /*if(tier == 1) {
            for(int y = 0; y < 2; y++) {
                pos = pos.up();
                if(world.getBlockState(pos).getBlock() != Blocks.IRON_BARS)
                    return false;
            }
        }*/
        return true;
    }
    
    private boolean shouldActivate(World world, BlockPos pos, BlockPos fromPos) {
        return world.isReceivingRedstonePower(pos) && !(world.getBlockState(fromPos).getBlock() instanceof RuneBlock);
    }

    @Nullable
    private BlockPos getCenter(World world, BlockPos pos) {
        return getCenter(world, pos, world.getBlockState(pos));
    }

    @Nullable
    private BlockPos getCenter(World world, BlockPos pos, BlockState state) {
        int i = state.get(CENTER);
        BlockPos next;
        if(i != 8) {
            next = pos.add(NeighborList.platform[i]);
            if(world.getBlockState(next).getBlock() instanceof RunicCenterBlock)
                return next;
        }

        
        for(i = 0; i < 8; i++) {
            next = pos.add(NeighborList.platform[i]);
            Block block = world.getBlockState(next).getBlock();
            if(block instanceof RunicCenterBlock) {
                world.setBlockState(pos, state.with(CENTER, i));
                return next;
            }
        }
        
        world.setBlockState(pos, state.with(CENTER, i));
        return null;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        getCenter(world, pos, state);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if(!state.get(POWERED) && shouldActivate(world, pos, fromPos)) {
            world.setBlockState(pos, state.with(POWERED, true));
            activate(world, pos);
        } else if(state.get(POWERED) && !shouldActivate(world, pos, fromPos))
            world.setBlockState(pos, state.with(POWERED, false));
    }

    @Override
    public boolean activate(World world, BlockPos pos) {
        BlockPos center = getCenter(world, pos);
        if(center != null)
            return ((RunicCenterBlock) world.getBlockState(center).getBlock()).activate(world, center);
        return false;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED, CENTER);
    }

    static {
        POWERED = Properties.POWERED;
        CENTER = Properties.LEVEL_8;
    }
}
