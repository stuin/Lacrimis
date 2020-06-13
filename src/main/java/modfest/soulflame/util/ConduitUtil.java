package modfest.soulflame.util;

import modfest.soulflame.block.ConduitBlock;
import modfest.soulflame.block.GatedConduitBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

import java.util.*;
import java.util.function.Predicate;

import modfest.soulflame.block.BlockConduitConnect;
import modfest.soulflame.init.ModBlocks;

public class ConduitUtil {

    public static List<Entry> listScanConduits(BlockView world, BlockPos pos) {
        if(world == null) return null;

        Set<BlockPos> scanned = new HashSet<>();
        Deque<BlockPos> stack = new ArrayDeque<>();

        List<Direction> all = Arrays.asList(Direction.values());
        List<Entry> out = new ArrayList<>();

        EnumSet<Direction> outputs;
        BlockState source = world.getBlockState(pos);
        if (source.getBlock() instanceof BlockConduitConnect) {
            outputs = EnumSet.noneOf(Direction.class);
            BlockConduitConnect b = (BlockConduitConnect) source.getBlock();
            for (Direction direction : all) {
                if (b.canConnectConduitTo(pos, world, direction)) {
                    outputs.add(direction);
                }
            }
        } else if (source.getBlock() == ModBlocks.conduit)
            outputs = EnumSet.allOf(Direction.class);
        else if (source.getBlock() == ModBlocks.gatedConduit && source.get(GatedConduitBlock.POWERED))
            outputs = EnumSet.allOf(Direction.class);
        else
            outputs = EnumSet.noneOf(Direction.class);

        stack.push(pos);
        scanned.add(pos);
        while (!stack.isEmpty()) {
            BlockPos cur = stack.pop();
            for (Direction d : cur.equals(pos) ? outputs : all) {
                BlockPos next = cur.offset(d);
                if (scanned.contains(next)) continue;
                scanned.add(cur);

                BlockState nextState = world.getBlockState(next);
                if (nextState.getBlock() == ModBlocks.conduit)
                    stack.push(next);
                else if (nextState.getBlock() == ModBlocks.gatedConduit && nextState.get(GatedConduitBlock.POWERED))
                    stack.push(next);
                else if (nextState.getBlock() instanceof BlockConduitConnect) {
                    BlockConduitConnect b = (BlockConduitConnect) nextState.getBlock();
                    if (b.canConnectConduitTo(next, world, d.getOpposite()))
                        out.add(new Entry(b, next, nextState));
                }
            }
        }

        return out;
    }

    public static Object locateSource(BlockView world, BlockPos pos, Predicate<Object> filter) {
        List<Entry> list = listScanConduits(world, pos);
        if(list != null && list.size() > 0) {
            for(Entry e : list) {
                Object value = e.extract(world);
                if(value != null && filter.test(value))
                    return value;
            }
        }
        return null;
    }

    public static BlockPos locateSink(BlockView world, BlockPos pos, Object value) {
        List<Entry> list = listScanConduits(world, pos);
        if(list != null && list.size() > 0) {
            for(Entry e : list) {
                if(e.insert(world, value))
                    return e.pos;
            }
        }
        return null;
    }

    public static final class Entry {
        public final BlockConduitConnect b;
        public final BlockPos pos;
        private BlockState state;

        public Entry(BlockConduitConnect b, BlockPos pos, BlockState state) {
            this.b = b;
            this.pos = pos;
            this.state = state;
        }

        public Object extract(BlockView world) {
            Object r = this.b.extract(this.pos, world);
            if (r != null && world instanceof WorldView) {
                this.updateState((WorldView) world);
            }
            return r;
        }

        public boolean insert(BlockView world, Object value) {
            return this.b.insert(this.pos, world, value);
        }

        public void updateState(WorldView world) {
            this.state = world.getBlockState(this.pos);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || this.getClass() != o.getClass()) return false;
            Entry entry = (Entry) o;
            return Objects.equals(this.b, entry.b) &&
                    Objects.equals(this.pos, entry.pos) &&
                    Objects.equals(this.state, entry.state);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.b, this.pos, this.state);
        }

        @Override
        public String toString() {
            return String.format("Entry { b: %s, pos: %s, state: %s }", this.b, this.pos, this.state);
        }
    }

}
