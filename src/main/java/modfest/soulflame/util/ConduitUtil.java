package modfest.soulflame.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Optional;

import modfest.soulflame.block.BlockConduitConnect;
import modfest.soulflame.init.ModBlocks;

public class ConduitUtil {

    public static Entry scanConduits(BlockView world, BlockPos pos) {
        Set<BlockPos> scanned = new HashSet<>();
        Deque<BlockPos> stack = new ArrayDeque<>();

        List<Direction> all = Arrays.asList(Direction.values());

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
        } else if (source.getBlock() == ModBlocks.conduit) {
            outputs = EnumSet.allOf(Direction.class);
        } else {
            outputs = EnumSet.noneOf(Direction.class);
        }

        stack.push(pos);
        scanned.add(pos);
        while (!stack.isEmpty()) {
            BlockPos cur = stack.pop();
            for (Direction d : cur.equals(pos) ? outputs : all) {
                BlockPos next = cur.offset(d);
                if (scanned.contains(next)) continue;
                scanned.add(cur);

                BlockState nextState = world.getBlockState(next);
                if (nextState.getBlock() == ModBlocks.conduit) {
                    stack.push(next);
                } else if (nextState.getBlock() instanceof BlockConduitConnect) {
                    BlockConduitConnect b = (BlockConduitConnect) nextState.getBlock();
                    if (b.canConnectConduitTo(next, world, d.getOpposite())) {
                        return new Entry(b, next, nextState);
                    }
                }
            }
        }

        return null;
    }

    public static Object locateSource(World world, BlockPos pos) {
        if(world == null || world.isClient()) return null;
        ConduitUtil.Entry entry = ConduitUtil.scanConduits(world, pos);
        if(entry != null)
            return entry.extract(world, false);
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

        public Object extract(World world, boolean simulate) {
            Object r = this.b.extract(this.pos, world, simulate);
            if (r != null && simulate) {
                this.updateState(world);
            }
            return r;
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
