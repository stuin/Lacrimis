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

    protected final int tier;

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
                if(!world.getBlockState(pos).getBlock().isIn(ModBlocks.cage_materials))
                    return -1;
            }
        }
        return tier;
    }

    protected BlockPos getCenter(BlockView world, BlockPos pos) {
        return getCenter(world, pos, world.getBlockState(pos));
    }

    protected BlockPos getCenter(BlockView world, BlockPos pos, BlockState state) {
        //Check for valid rotation
        int i = state.get(CENTER);
        BlockPos next;
        Block block;
        if(i != 8) {
            next = pos.add(NeighborList.platform[i]);
            if(validCenter(world.getBlockState(next)))
                return next;
        }

        //Find correct rotation
        for(i = 0; i < 8; i++) {
            next = pos.add(NeighborList.platform[i]);
            if(world instanceof World && validCenter(world.getBlockState(next))) {
                ((World)world).setBlockState(pos, state.with(CENTER, i));
                return next;
            }
        }
        
        if(world instanceof World)
            ((World)world).setBlockState(pos, state.with(CENTER, 8));
        return null;
    }
    
    protected BlockPos getTrueCenter(BlockView world, BlockPos pos) {
        //Get actual center
        BlockPos center = getCenter(world, pos);
        if(center != null) {
            Block block = world.getBlockState(center).getBlock();
            if(block instanceof CenterRuneBlock)
                return center;
            if(block instanceof AdvancedRuneBlock)
                return ((AdvancedRuneBlock) block).getCenter(world, center);
        }
        return null;
    }

    protected boolean validCenter(BlockState state) {
        Block block = state.getBlock();
        return block instanceof CenterRuneBlock ||
                (block instanceof AdvancedRuneBlock && NeighborList.isEdge(state.get(CENTER)));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        getCenter(world, pos, state);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        Block from = world.getBlockState(fromPos).getBlock();
        boolean a = state.get(POWERED);
        boolean b = world.isReceivingRedstonePower(pos);
        boolean c = (from instanceof RuneBlock || from instanceof CenterRuneBlock);
        if(!a && b && !c) {
            world.setBlockState(pos, state.with(POWERED, true));
            activate(world, pos, null);
        } else if(a && !b)
            world.setBlockState(pos, state.with(POWERED, false));
    }

    @Override
    public boolean activate(World world, BlockPos pos, PlayerEntity player) {
        BlockPos center = getTrueCenter(world, pos);
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
        CENTER = IntProperty.of("center", 0, 8);
    }
}
