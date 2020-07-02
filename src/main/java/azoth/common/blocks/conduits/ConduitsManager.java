package azoth.common.blocks.conduits;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import azoth.common.AzothBlocks;
import azoth.common.blocks.ConduitConnectable;
import azoth.common.blocks.conduits.ConduitsUtils.PathMode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ConduitsManager {
    protected final World world;
    protected final Map<BlockPos, List<ConduitPath>> cache = new HashMap<>();

    public ConduitsManager(World world) {
        this.world = world;
    }

    public void notifyUpdate(BlockPos pos) {
        BlockState state = this.world.getBlockState(pos);
        Block block = state.getBlock();
        if (block == AzothBlocks.CONDUIT
                || block instanceof ConduitConnectable && ((ConduitConnectable) block).isSink()) {
            List<ConduitPath> sources = ConduitsUtils.buildPaths(this.world, pos, PathMode.TO_SOURCES);
            for (ConduitPath source : sources) {
                if (this.cache.containsKey(source.end)) {
                    this.cache.remove(source.end);
                }
            }
        }
        this.cache.remove(pos);
    }

    public int offer(BlockPos pos, int amount) {
        if (!this.cache.containsKey(pos)) {
            List<ConduitPath> paths = ConduitsUtils.buildPaths(world, pos, PathMode.TO_SINKS);
            paths.sort((a, b) -> Integer.compare(a.length, b.length));
            this.cache.put(pos, paths);
        }

        for (ConduitPath path : this.cache.get(pos)) {
            BlockState state = this.world.getBlockState(path.end);
            Block block = state.getBlock();
            if (block instanceof ConduitConnectable) {
                amount -= ((ConduitConnectable) block).tryReceive(this.world, path.end, amount);
                if (amount == 0) {
                    break;
                }
            }
        }
        return amount;
    }
}
