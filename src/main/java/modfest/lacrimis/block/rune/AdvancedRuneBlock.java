package modfest.lacrimis.block.rune;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import modfest.lacrimis.util.NeighborList;

public class AdvancedRuneBlock extends BasicRuneBlock {
    public static final IntProperty PIPE;

    public AdvancedRuneBlock() {
        super(3);
        setDefaultState(getDefaultState().with(PIPE, 3));
    }

    public static BlockPos getDuct(BlockView world, BlockPos pos) {
        //Calculate duct connector position
        int i = NeighborList.getOpposite(world.getBlockState(pos).get(CENTER));
        i = (i + world.getBlockState(pos).get(PIPE) - 1) % 8;
        return pos.add(NeighborList.platform[i]);
    }

    @Override
    public int testCage(BlockView world, BlockPos pos, Direction flipped) {
        int i = NeighborList.getOpposite(world.getBlockState(pos).get(CENTER));
        boolean duct = false;

        if(NeighborList.isEdge(i)) {
            for(int j = -1; j < 2; j++) {
                Block block = world.getBlockState(pos.add(NeighborList.platform[(i + j) % 8])).getBlock();
                if((!(block instanceof BasicRuneBlock) || (((BasicRuneBlock) block).tier != 2)))
                    return -2;

                //Check for single duct
                if(block instanceof DuctRuneBlock) {
                    if(duct) {
                        if(world instanceof World)
                            ((World) world).setBlockState(pos, world.getBlockState(pos).with(PIPE, 3));
                        return -3;
                    }
                    duct = true;

                    //Save duct location
                    if(world instanceof World)
                        ((World) world).setBlockState(pos, world.getBlockState(pos).with(PIPE, j + 1));
                }
            }
        }
        return tier;
    }

    @Override
    protected boolean validCenter(BlockState state) {
        return state.getBlock() instanceof CenterRuneBlock;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(PIPE);
    }

    static {
        PIPE = IntProperty.of("duct", 0, 3);
    }
}
