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

import modfest.lacrimis.block.BlockConduitConnect;
import modfest.lacrimis.block.GatedConduitBlock;
import modfest.lacrimis.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class ConduitUtil {

    public static List<ConduitEntry> listScanConduits(BlockView world, BlockPos pos, boolean extracting) {
        if(world == null) return null;

        Set<BlockPos> scanned = new HashSet<>();
        Deque<BlockPos> stack = new ArrayDeque<>();

        List<Direction> all = Arrays.asList(Direction.values());
        List<ConduitEntry> out = new ArrayList<>();

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
        else if(source.getBlock() == ModBlocks.oneWayConduit)
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
                if (nextState.getBlock() == ModBlocks.conduit)
                    stack.push(next);
                else if (nextState.getBlock() == ModBlocks.gatedConduit && nextState.get(GatedConduitBlock.POWERED))
                    stack.push(next);
                else if(nextState.getBlock() == ModBlocks.oneWayConduit &&
                        (extracting && d == nextState.get(FacingBlock.FACING).getOpposite() ||
                                !extracting && d == nextState.get(FacingBlock.FACING)))
                    stack.push(next);
                else if (nextState.getBlock() instanceof BlockConduitConnect) {
                    BlockConduitConnect b = (BlockConduitConnect) nextState.getBlock();
                    if (b.canConnectConduitTo(next, world, d))
                        out.add(new ConduitEntry(b, next, nextState));
                }
            }
        }

        return out;
    }

    public static Object locateSource(BlockView world, BlockPos pos, Predicate<Object> filter) {
        List<ConduitEntry> list = listScanConduits(world, pos, true);
        if(list != null && list.size() > 0) {
            for(ConduitEntry e : list) {
                Object value = e.extract(world);
                if(value != null && filter.test(value))
                    return value;
            }
        }
        return null;
    }
    
    public static boolean locateTearsStrong(BlockView world, BlockPos pos, int request, boolean simulate) {
        return locateTearsStrong(world, listScanConduits(world, pos, true), request, simulate);
    }

    public static boolean locateTearsStrong(BlockView world, List<ConduitEntry> list, int request, boolean simulate) {
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
        List<ConduitEntry> list = listScanConduits(world, pos, true);
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
        return locateSink(world, listScanConduits(world, pos, false), value);
    }

    public static BlockPos locateSink(BlockView world, List<ConduitEntry> list, Object value) {
        if(list != null && list.size() > 0) {
            for(ConduitEntry e : list) {
                if(e.insert(world, value))
                    return e.pos;
            }
        }
        return null;
    }
}
