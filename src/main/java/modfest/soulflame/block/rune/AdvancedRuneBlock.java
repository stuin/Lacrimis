package modfest.soulflame.block.rune;

import modfest.soulflame.util.NeighborList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class AdvancedRuneBlock extends RuneBlock {
    public static final IntProperty PIPE;

    public AdvancedRuneBlock() {
        super(3);
        setDefaultState(getStateManager().getDefaultState().with(PIPE, 3));
    }

    public static BlockPos getPipe(BlockView world, BlockPos pos) {
        //Calculate pipe connector position
        int i = NeighborList.getOpposite(world.getBlockState(pos).get(CENTER));
        i = (i + world.getBlockState(pos).get(PIPE) - 1) % 8;
        return pos.add(NeighborList.platform[i]);
    }

    @Override
    public int testCage(BlockView world, BlockPos pos, Direction flipped) {
        int i = NeighborList.getOpposite(world.getBlockState(pos).get(CENTER));
        boolean pipe = false;

        if(NeighborList.isEdge(i)) {
            for(int j = -1; j < 2; j++) {
                Block block = world.getBlockState(pos.add(NeighborList.platform[(i + j) % 8])).getBlock();
                if(!(block instanceof RuneBlock && ((RuneBlock) block).tier == 2))
                    return -2;

                //Check for single pipe
                if(block instanceof PipeConnectorBlock) {
                    if(pipe) {
                        if(world instanceof World)
                            ((World) world).setBlockState(pos, world.getBlockState(pos).with(PIPE, 8));
                        return -3;
                    }
                    pipe = true;

                    //Save pipe location
                    if(world instanceof World)
                        ((World) world).setBlockState(pos, world.getBlockState(pos).with(PIPE, j + 1));
                }
            }
        }
        return 3;
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
        PIPE = IntProperty.of("pipe", 0, 3);
    }
}
