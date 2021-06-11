package modfest.lacrimis.block.rune;

import com.zundrel.wrenchable.block.BlockWrenchable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import modfest.lacrimis.init.ModBlocks;
import modfest.lacrimis.util.NeighborList;
import net.minecraft.world.WorldAccess;

public class BasicRuneBlock extends Block implements BlockWrenchable {
    public static final BooleanProperty POWERED;
    public static final IntProperty CENTER;

    protected final int tier;

    public BasicRuneBlock(int tier) {
        super(ModBlocks.runeSettings);
        this.tier = tier;
        setDefaultState(getDefaultState().with(POWERED, false).with(CENTER, 8));
    }

    public int testCage(World world, BlockPos pos, Direction flipped) {
        getCenter(world, pos);
        if(tier == 1) {
            for(int y = 0; y < 2; y++) {
                pos = pos.offset(flipped);
                if(!ModBlocks.cage_materials.contains(world.getBlockState(pos).getBlock()))
                    return -1;
            }
        }
        return tier;
    }

    protected BlockPos getCenter(WorldAccess world, BlockPos pos) {
        return getCenter(world, pos, world.getBlockState(pos));
    }

    protected BlockPos getCenter(WorldAccess world, BlockPos pos, BlockState state) {
        //Check for valid rotation
        int centerState = state.get(CENTER);
        if (centerState != 8) {
            BlockPos next = pos.add(NeighborList.platform[centerState]);
            if (validCenter(world.getBlockState(next)))
                return next;
        }

        //Find correct rotation
        for (int i = 0; i < 8; i++) {
            BlockPos next = pos.add(NeighborList.platform[i]);
            if (validCenter(world.getBlockState(next))) {
                if(world instanceof World)
                    ((World) world).setBlockState(pos, state.with(CENTER, i));
                return next;
            }
        }

        if(world instanceof World)
            ((World) world).setBlockState(pos, state.with(CENTER, 8));
        return null;
    }

    protected BlockPos getTrueCenter(World world, BlockPos pos) {
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
                block instanceof AdvancedRuneBlock && NeighborList.isEdge(state.get(CENTER));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        getCenter(world, pos, state);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        Block from = world.getBlockState(fromPos).getBlock();
        boolean powered = state.get(POWERED);
        boolean redstonePower = world.isReceivingRedstonePower(pos);
        boolean isRune = from instanceof BasicRuneBlock || from instanceof CenterRuneBlock;
        if (!powered && redstonePower && !isRune) {
            world.setBlockState(pos, state.with(POWERED, true));
            activate(world, pos, null);
        } else if (powered && !redstonePower)
            world.setBlockState(pos, state.with(POWERED, false));
    }

    @Override
    public void onWrenched(World world, PlayerEntity player, BlockHitResult blockHitResult) {
        activate(world, blockHitResult.getBlockPos(), player);
    }

    public void activate(World world, BlockPos pos, PlayerEntity player) {
        BlockPos center = getTrueCenter(world, pos);
        if(center != null)
            ((CenterRuneBlock) world.getBlockState(center).getBlock()).activate(world, center, player);
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
