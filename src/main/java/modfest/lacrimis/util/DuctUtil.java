package modfest.lacrimis.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import modfest.lacrimis.block.DuctConnectBlock;
import modfest.lacrimis.block.GatedDuctBlock;
import modfest.lacrimis.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class DuctUtil {

    public static List<DuctEntry> listScanDucts(BlockView world, BlockPos pos, boolean extracting) {
        if(world == null) return null;

        Set<BlockPos> scanned = new HashSet<>();
        Deque<BlockPos> stack = new ArrayDeque<>();

        List<Direction> all = Arrays.asList(Direction.values());
        List<DuctEntry> out = new ArrayList<>();

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
                    if (b.canConnectDuctTo(next, world, d))
                        out.add(new DuctEntry(b, next, nextState));
                }
            }
        }

        return out;
    }

    public static Object locateSource(BlockView world, BlockPos pos, Predicate<Object> filter) {
        List<DuctEntry> list = listScanDucts(world, pos, true);
        if(list != null && list.size() > 0) {
            for(DuctEntry e : list) {
                Object value = e.extract(world);
                if(value != null && filter.test(value))
                    return value;
            }
        }
        return null;
    }

    public static boolean locateTearsStrong(BlockView world, BlockPos pos, int request, boolean simulate) {
        return locateTearsStrong(world, listScanDucts(world, pos, true), request, simulate);
    }

    public static boolean locateTearsStrong(BlockView world, List<DuctEntry> list, int request, boolean simulate) {
        if(list != null && list.size() > 0) {
            int found = 0;

            //Test for requested amount
            for(int i = 0; i < list.size() && found < request; i++)
                found += list.get(i).extractTears(world, request - found, simulate);
            return found >= request;
        }
        return false;
    }

    public static int locateTears(BlockView world, BlockPos pos, int request) {
        List<DuctEntry> list = listScanDucts(world, pos, true);
        if(list != null && list.size() > 0) {
            int found = 0;

            //Extract some amount
            for(int i = 0; i < list.size() && found < request; i++)
                found += list.get(i).extractTears(world, request - found, false);
            return found;
        }
        return 0;
    }

    public static BlockPos locateSink(BlockView world, BlockPos pos, Object value) {
        return locateSink(world, listScanDucts(world, pos, false), value);
    }

    public static BlockPos locateSink(BlockView world, List<DuctEntry> list, Object value) {
        if(list != null && list.size() > 0) {
            for(DuctEntry e : list) {
                if(e.insert(world, value))
                    return e.pos;
            }
        }
        return null;
    }
}
