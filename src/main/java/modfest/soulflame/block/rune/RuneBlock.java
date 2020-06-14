package modfest.soulflame.block.rune;

import modfest.soulflame.block.Activatable;
import modfest.soulflame.init.ModBlocks;
import modfest.soulflame.util.NeighborList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class RuneBlock extends Block implements Activatable {
    public static final BooleanProperty POWERED;
    public static final IntProperty CENTER;

    private final int tier;

    public RuneBlock(int tier) {
        super(ModBlocks.runeSettings);
        this.tier = tier;
        setDefaultState(getStateManager().getDefaultState().with(POWERED, false).with(CENTER, 8));
    }

    public int testCage(BlockView world, BlockPos pos, Direction flipped) {
        getCenter(world, pos);
        if(tier == 1) {
            for(int y = 0; y < 2; y++) {
                pos = pos.offset(flipped);
                if(!world.getBlockState(pos).getBlock().isIn(ModBlocks.conductive))
                    return 0;
            }
        }
        return tier;
    }
    
    protected boolean shouldActivate(World world, BlockPos pos, BlockPos fromPos) {
        if(world.getBlockState(fromPos).getBlock() instanceof CenterRuneBlock)
            return fromPos.north() == pos;
        return world.isReceivingRedstonePower(pos) && !(world.getBlockState(fromPos).getBlock() instanceof RuneBlock);
    }

    protected BlockPos getCenter(BlockView world, BlockPos pos) {
        return getCenter(world, pos, world.getBlockState(pos));
    }

    protected BlockPos getCenter(BlockView world, BlockPos pos, BlockState state) {
        //Check for valid rotation
        int i = state.get(CENTER);
        BlockPos next;
        if(i != 8) {
            next = pos.add(NeighborList.platform[i]);
            if(world.getBlockState(next).getBlock() instanceof CenterRuneBlock)
                return next;
        }

        //Find correct rotation
        for(i = 0; i < 8; i++) {
            next = pos.add(NeighborList.platform[i]);
            Block block = world.getBlockState(next).getBlock();
            if(world instanceof World && block instanceof CenterRuneBlock) {
                ((World)world).setBlockState(pos, state.with(CENTER, i));
                return next;
            }
        }
        
        if(world instanceof World)
            ((World)world).setBlockState(pos, state.with(CENTER, 8));
        return null;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        getCenter(world, pos, state);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        boolean a = state.get(POWERED);
        boolean b = shouldActivate(world, pos, fromPos);
        if(!a && b) {
            world.setBlockState(pos, state.with(POWERED, true));
            activate(world, pos, null);
        } else if(a && !b)
            world.setBlockState(pos, state.with(POWERED, false));
    }

    @Override
    public boolean activate(World world, BlockPos pos, PlayerEntity player) {
        BlockPos center = getCenter(world, pos);
        if(center != null)
            return ((CenterRuneBlock) world.getBlockState(center).getBlock()).activate(world, center, player);
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
