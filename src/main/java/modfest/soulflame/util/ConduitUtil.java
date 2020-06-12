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

import modfest.soulflame.block.BlockConduitConnect;
import modfest.soulflame.init.ModBlocks;

import static java.lang.Math.min;
import static java.lang.Math.round;

public class ConduitUtil {

    public static List<Entry> scanConduits(BlockView world, BlockPos pos) {
        Set<Entry> result = new HashSet<>();
        Set<BlockPos> scanned = new HashSet<>();
        Deque<BlockPos> stack = new ArrayDeque<>();

        List<Direction> all = Arrays.asList(Direction.values());

        EnumSet<Direction> outputs;
        BlockState source = world.getBlockState(pos);
        if (source.getBlock() instanceof BlockConduitConnect) {
            outputs = EnumSet.noneOf(Direction.class);
            BlockConduitConnect b = (BlockConduitConnect) source.getBlock();
            for (Direction direction : all) {
                if (b.canConnectConduitTo(source, pos, world, direction)) {
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
                    if (b.canConnectConduitTo(nextState, next, world, d.getOpposite())) {
                        result.add(new Entry(b, next, nextState));
                    }
                }
            }
        }

        return new ArrayList<>(result);
    }

    public static int totalAmount(List<Entry> candidates, World world) {
        if (candidates.isEmpty()) return 0;

        return candidates.stream().mapToInt(e -> e.extract(world, e.getCurrentTearsAmount(world), true)).sum();
    }

    public static int pull(List<Entry> candidates, World world, int amount, boolean simulate) {
        if (candidates.isEmpty()) return 0;

        float[] pcts = new float[candidates.size()];
        int total = 0;
        for (int i = 0; i < candidates.size(); i++) {
            Entry entry = candidates.get(i);
            int a = entry.extract(world, entry.getCurrentTearsAmount(world), true);
            pcts[i] = a;
            total += a;
        }

        for (int i = 0; i < pcts.length; i++) {
            pcts[i] /= total;
        }

        int remaining = amount;

        for (int i = 0; i < candidates.size(); i++) {
            if (remaining == 0) return amount;
            int toPullOut = min(remaining, round(pcts[i] * amount));
            if (toPullOut < 1) continue;
            Entry e = candidates.get(i);
            remaining -= e.extract(world, toPullOut, simulate);
        }

        if (remaining > 0) {
            for (Entry candidate : candidates) {
                remaining -= candidate.extract(world, remaining, simulate);
            }
        }

        return amount - remaining;
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

        public int getCurrentTearsAmount(WorldView world) {
            return this.b.getCurrentTearsAmount(this.state, this.pos, world);
        }

        public int extract(World world, int amount, boolean simulate) {
            int r = this.b.extract(this.state, this.pos, world, amount, simulate);
            if (r > 0 && simulate) {
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
