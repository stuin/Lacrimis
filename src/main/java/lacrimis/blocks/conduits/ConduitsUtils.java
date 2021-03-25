package lacrimis.blocks.conduits;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lacrimis.Lacrimis.Blocks;
import lacrimis.blocks.ConduitConnectable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ConduitsUtils {
    public static List<ConduitPath> buildPaths(World world, BlockPos start, PathMode mode) {
        Map<BlockPos, Link> links = new HashMap<>();
        Deque<BlockPos> queue = new ArrayDeque<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            BlockPos pos = queue.poll();
            int length = pos == start ? 0 : links.get(pos).length;

            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = pos.offset(direction);
                if (neighborPos != start) {
                    BlockState neighbor = world.getBlockState(neighborPos);
                    if (neighbor.getBlock() == Blocks.AZOTH_CONDUIT || mode.isValidEnd(neighbor)) {
                        int nextLength = length + 1;
                        if (links.containsKey(neighborPos) && nextLength >= links.get(neighborPos).length) {
                            continue;
                        }
                        links.put(neighborPos, new Link(pos, direction, nextLength));
                        if (neighbor.getBlock() == Blocks.AZOTH_CONDUIT && !queue.contains(neighborPos)) {
                            queue.add(neighborPos);
                        }
                    }
                }
            }
        }

        List<ConduitPath> paths = new ArrayList<>();
        for (Map.Entry<BlockPos, Link> entry : links.entrySet()) {
            BlockPos pos = entry.getKey();
            if (mode.isValidEnd(world.getBlockState(pos))) {
                Link link = entry.getValue();

                Deque<BlockPos> conduits = new ArrayDeque<>(link.length);
                while (link != null && link.prev != start) {
                    BlockPos prevPos = link.prev;
                    if (world.getBlockState(prevPos).getBlock() == Blocks.AZOTH_CONDUIT) {
                        conduits.addFirst(prevPos);
                    }
                    link = links.get(prevPos);
                }

                ConduitPath path = new ConduitPath(pos, link.direction.getOpposite(), link.length, new ArrayList<>(conduits));
                paths.add(path);
            }
        }
        return paths;
    }

    public enum PathMode {
        TO_SOURCES, TO_SINKS;

        boolean isValidEnd(BlockState state) {
            Block block = state.getBlock();
            if (!(block instanceof ConduitConnectable)) {
                return false;
            }
            ConduitConnectable connectable = (ConduitConnectable) block;
            return this == TO_SOURCES ? connectable.isSource() : connectable.isSink();
        }
    }

    static class Link {
        BlockPos prev;
        Direction direction;
        int length;

        public Link(BlockPos prev, Direction direction, int length) {
            this.prev = prev;
            this.direction = direction;
            this.length = length;
        }
    }
}
