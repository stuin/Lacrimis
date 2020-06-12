package modfest.soulflame.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.ArrayDeque;
import java.util.ArrayList;
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

    public static <T> Optional<Entry<T>> scanConduits(BlockView world, BlockPos pos) {
        Set<BlockPos> scanned = new HashSet<>();
        Deque<BlockPos> stack = new ArrayDeque<>();

        List<Direction> all = Arrays.asList(Direction.values());

        EnumSet<Direction> outputs;
        BlockState source = world.getBlockState(pos);
        if (source.getBlock() instanceof BlockConduitConnect) {
            outputs = EnumSet.noneOf(Direction.class);
            BlockConduitConnect<?> b = (BlockConduitConnect<?>) source.getBlock();
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
                    BlockConduitConnect<T> b = (BlockConduitConnect<T>) nextState.getBlock();
                    if (b.canConnectConduitTo(next, world, d.getOpposite())) {
                        return Optional.of(new Entry<>(b, next, nextState));
                    }
                }
            }
        }

        return Optional.empty();
    }

    public static <T> boolean found(List<Entry<T>> candidates, World world) {
        if (candidates.isEmpty()) return false;

        return candidates.stream().anyMatch(e -> e.extract(world, true).isPresent());
    }

    public static <T> T pull(Entry<T> candidate, World world, boolean simulate) {
        Optional<T> a = candidate.extract(world, true);
        return a.orElse(null);
    }

    public static final class Entry<T> {
        public final BlockConduitConnect<T> b;
        public final BlockPos pos;
        private BlockState state;

        public Entry(BlockConduitConnect<T> b, BlockPos pos, BlockState state) {
            this.b = b;
            this.pos = pos;
            this.state = state;
        }

        public Optional<T> extract(World world, boolean simulate) {
            Optional<T> r = this.b.extract(this.pos, world, simulate);
            if (r.isPresent() && simulate) {
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
            Entry<T> entry = (Entry<T>) o;
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
