package com.stuintech.lacrimis.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class GatedDuctBlock extends DuctBlock {
    public static final BooleanProperty POWERED;

    public GatedDuctBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(POWERED, false).with(NODE, true));
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        state = state.with(POWERED, world.isReceivingRedstonePower(pos));
        world.setBlockState(pos, connectToBlocks(state, world, pos));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        if(state != null)
            return state.with(POWERED, ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()));
        return null;
    }

    @Override
    protected BlockState connectToBlocks(BlockState state, WorldAccess world, BlockPos pos) {
        if(state.get(POWERED))
            return super.connectToBlocks(state, world, pos).with(POWERED, true);
        else return getDefaultState();
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(POWERED);
    }

    static {
        POWERED = Properties.POWERED;
    }

}
