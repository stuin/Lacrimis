package com.stuintech.lacrimis.block;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class CreativeTearsBlock extends Block implements DuctConnectBlock {

    public CreativeTearsBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canConnectDuctTo(BlockPos pos, WorldAccess world, Direction side) {
        return true;
    }

    @Override
    public int extractTears(BlockPos pos, World world, int request, boolean simulate) {
        return request;
    }

    @Override
    public boolean insert(BlockPos pos, World world, Object value) {
        return false;
    }
}
