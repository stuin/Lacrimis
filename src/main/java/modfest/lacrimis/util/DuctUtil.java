package modfest.lacrimis.util;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.block.DuctConnectBlock;
import modfest.lacrimis.block.GatedDuctBlock;
import modfest.lacrimis.block.entity.NetworkLinkBlock;
import modfest.lacrimis.block.entity.NetworkLinkEntity;
import modfest.lacrimis.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.*;

public class DuctUtil {

    public static List<BlockPos> scanDucts(World world, BlockPos pos, boolean extracting, int goal, Tester test) {
        if(world == null || world.isClient)
            return new ArrayList<>();

        //Set up block lists
        Set<BlockPos> scanned = new HashSet<>();
        Deque<BlockPos> stack = new ArrayDeque<>();
        List<Direction> all = Arrays.asList(Direction.values());
        List<BlockPos> out = new ArrayList<>();
        NetworksState.NetworkList network = null;
        NetworksState.NetworkList networkOriginal = null;

        //Get initial ducting
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

        //Loop through connected ducts
        stack.push(pos);
        scanned.add(pos);
        while (!stack.isEmpty()) {
            BlockPos cur = stack.pop();
            boolean sided = cur.equals(pos);
            if(world.getBlockState(cur) instanceof DuctConnectBlock) {
                sided = true;
                outputs = EnumSet.noneOf(Direction.class);
                DuctConnectBlock b = (DuctConnectBlock) world.getBlockState(cur).getBlock();
                for (Direction direction : all) {
                    if (b.canConnectDuctTo(cur, world, direction)) {
                        outputs.add(direction);
                    }
                }
            }

            for (Direction d : sided ? outputs : all) {
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
                        //Act on block
                        int a = test.apply(next, b, goal);
                        if(a > 0) {
                            out.add(next);
                            goal -= a;
                            if (goal <= 0) {
                                if(scanned.size() > 100)
                                    Lacrimis.LOGGER.warn(scanned.size() + " ducts scanned from " + pos + " to " + next);
                                return out;
                            }
                        }
                        stack.push(next);
                    }

                    //Get network link
                    if (nextState.getBlock() instanceof NetworkLinkBlock) {
                        NetworkLinkEntity linkEntity = ((NetworkLinkEntity) world.getBlockEntity(next));
                        if (linkEntity != null && linkEntity.isOn()) {
                            if (network == null) {
                                networkOriginal = linkEntity.getNetwork();
                                if(networkOriginal != null) {
                                    network = (NetworksState.NetworkList) networkOriginal.clone();
                                    network.remove(linkEntity.getPos());
                                }
                            } else if (network.color == linkEntity.getColor())
                                network.remove(linkEntity.getPos());
                        }
                    }
                }
            }

            //Fallback to next network link
            if(stack.isEmpty() && network != null) {
                while(network.size() > 0 && !validLink(world, network.get(0), network.color)) {
                    networkOriginal.remove(network.get(0));
                    network.remove(network.get(0));
                }

                if(network.size() > 0) {
                    stack.push(network.get(0));
                    network.remove(network.get(0));
                }
            }
        }

        if(scanned.size() > 100)
            Lacrimis.LOGGER.warn(scanned.size() + " ducts scanned from " + pos);

        return new ArrayList<>();
    }

    private static boolean validLink(World world, BlockPos link, int color) {
        //Check for active network link
        if(world.getBlockState(link).getBlock() instanceof NetworkLinkBlock) {
            NetworkLinkEntity linkEntity = (NetworkLinkEntity) world.getBlockEntity(link);
            return linkEntity != null && linkEntity.getNetwork() != null && linkEntity.getColor() == color;
        }
        return false;
    }

    public static int locateTears(World world, BlockPos pos, int request) {
        return locateTears(world, pos, request, false);
    }

    public static int locateTears(World world, BlockPos pos, int request, boolean simulate) {
        List<BlockPos> list = scanDucts(world, pos, true, 1, (p, b, r) -> b.extractTears(p, world, request, true));
        if(list.size() > 0)
            return ((DuctConnectBlock)world.getBlockState(list.get(0)).getBlock()).extractTears(list.get(0), world, request, simulate);
        return 0;
    }

    public static BlockPos locateSink(World world, BlockPos pos, Object value) {
        List<BlockPos> list = scanDucts(world, pos, false, 1, (p, b, r) -> b.insert(p, world, value) ? 1 : 0);
        if(list.size() > 0)
            return list.get(0);
        else return null;
    }

    @FunctionalInterface
    public interface Tester {
        int apply(BlockPos pos, DuctConnectBlock block, int request);
    }
}
