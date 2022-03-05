package modfest.lacrimis.util;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.block.TaintBlock;
import modfest.lacrimis.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class TaintPacket {
    private final int amount;
    private boolean spawned = false;

    public TaintPacket(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void spawn(World world, BlockPos pos) {
        if(amount > 0 && !world.isClient && !spawned) {
            if(convert(world, pos, amount / 50, false) == 0) {
                for(Direction dir : Direction.values())
                    if(!spawned && convert(world, pos.offset(dir), amount / 50, false) > 0)
                        spawned = true;

                if(!spawned)
                    for(Direction dir : Direction.values())
                        spawn(world, pos.offset(dir, 2));
            }

            Lacrimis.LOGGER.debug("Spawned taint {} at {}", amount, pos.toString());
            spawned = true;
        }
    }

    public static void setLayers(World world, BlockPos pos, int layers) {
        if(layers <= 0)
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        else
            world.setBlockState(pos, ModBlocks.taint.getDefaultState().with(TaintBlock.LAYERS, Math.min(layers, 8)));
    }

    public static int convert(World world, BlockPos dest, int layers, boolean spread) {
        BlockState state = world.getBlockState(dest);
        int strength = (int)state.getHardness(world, dest);

        //Special cases
        if(state.getBlock() == ModBlocks.taint) {
            if(spread)
                layers /= 2;
            strength = state.get(TaintBlock.LAYERS);
            setLayers(world, dest, strength + layers);
            return Math.max(Math.min(8 - strength, layers), 1);
        } else if(state.isAir()) {
            setLayers(world, dest, Math.min(layers / 2 + 1, 8));
            return layers / 2 + 1;
        } else if(!state.getFluidState().isEmpty()) {
            setLayers(world, dest, Math.min(state.getFluidState().getLevel(), layers));
            return 1;
        } else if(state.isOf(Blocks.FIRE)) {
            return layers;
        }

        //Normal blocks
        if(strength != -1 && strength < layers || (strength > 8 && layers == 8)) {
            if(state.isIn(ModBlocks.tainted))
                return 0;
            else if(state.isIn(ModBlocks.resistant) && strength * 2 >= layers)
                return 0;
            else if(state.getMaterial() == Material.SOIL)
                world.setBlockState(dest, ModBlocks.taintedDirt.getDefaultState());
            else if(state.getMaterial() == Material.STONE || state.getMaterial() == Material.PISTON || state.getMaterial() == Material.METAL)
                world.setBlockState(dest, ModBlocks.taintedStone.getDefaultState());
            else
                setLayers(world, dest, strength + 1);
            return Math.max(strength, 1);
        }
        return 0;
    }
}
