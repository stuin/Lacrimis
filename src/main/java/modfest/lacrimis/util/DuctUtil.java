package modfest.lacrimis.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import modfest.lacrimis.block.DuctConnectBlock;
import modfest.lacrimis.block.GatedDuctBlock;
import modfest.lacrimis.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class DuctUtil {

    public static List<BlockPos> scanDucts(BlockView world, BlockPos pos, boolean extracting, int goal, Function<Object, Integer> filter) {
        if(world == null) return null;

        Set<BlockPos> scanned = new HashSet<>();
        Deque<BlockPos> stack = new ArrayDeque<>();
        List<Direction> all = Arrays.asList(Direction.values());
        List<BlockPos> out = new ArrayList<>();

        EnumSet<Direction> outputs;
        BlockState source = world.getBlockState(pos);
        if (source.getBlock() instanceof DuctConnectBlock) {
            outputs = EnumSet.noneOf(Direction.class);
            DuctConnectBlock b = (DuctConnectBlock) source.getBlock();
            for (Direction direction : all) {
                if (b.canConnectDuctTo(pos, world, direction)) {
                    outputs.add(direction);
                }
            }
        } else if (source.getBlock() == ModBlocks.duct)
            outputs = EnumSet.allOf(Direction.class);
        else if (source.getBlock() == ModBlocks.gatedDuct && source.get(GatedDuctBlock.POWERED))
            outputs = EnumSet.allOf(Direction.class);
        else if(source.getBlock() == ModBlocks.oneWayDuct)
            outputs = EnumSet.of(source.get(FacingBlock.FACING));
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
                if (nextState.getBlock() == ModBlocks.duct)
                    stack.push(next);
                else if (nextState.getBlock() == ModBlocks.gatedDuct && nextState.get(GatedDuctBlock.POWERED))
                    stack.push(next);
                else if(nextState.getBlock() == ModBlocks.oneWayDuct &&
                        (extracting && d == nextState.get(FacingBlock.FACING).getOpposite() ||
                                !extracting && d == nextState.get(FacingBlock.FACING)))
                    stack.push(next);
                else if (nextState.getBlock() instanceof DuctConnectBlock) {
                    DuctConnectBlock b = (DuctConnectBlock) nextState.getBlock();
                    if (b.canConnectDuctTo(next, world, d)) {
                        int a = filter.apply(b);
                        if(a > 0) {
                            out.add(next);
                            goal -= a;
                            if (goal <= 0)
                                return out;
                        }
                    }
                }
            }
        }

        return new ArrayList<>();
    }

    public static int locateTears(BlockView world, BlockPos pos, int request, boolean simulate) {
        List<BlockPos> list = scanDucts(world, pos, true, 1, (b) -> ((DuctConnectBlock) b).extractTears(pos, world, request, true));
        if(list.size() > 0)
            return ((DuctConnectBlock)world.getBlockState(list.get(0)).getBlock()).extractTears(pos, world, request, simulate);
        return 0;
    }

    public static int locateTears(BlockView world, BlockPos pos, int request) {
        return locateTears(world, pos, request, false);
    }

    public static BlockPos locateSink(BlockView world, BlockPos pos, Object value) {
        List<BlockPos> list = scanDucts(world, pos, false, 1, (b) -> ((DuctConnectBlock) b).insert(pos, world, value) ? 1 : 0);
        if(list.size() > 0)
            return list.get(0);
        else return null;
    }
}
