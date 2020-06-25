package modfest.lacrimis.block.rune;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.block.Activatable;
import modfest.lacrimis.block.BlockConduitConnect;
import modfest.lacrimis.init.ModBlocks;
import modfest.lacrimis.util.ConduitEntry;
import modfest.lacrimis.util.ConduitUtil;
import modfest.lacrimis.util.NeighborList;

public abstract class CenterRuneBlock extends Block implements Activatable, BlockConduitConnect {
    private static final Box TARGET_BOX = new Box(-0.5, -1, -0.5, 1.5, 1, 1.5);
    private static final Box LARGE_BOX = new Box(-1.5, -1, -1.5, 2.5, 1, 2.5);

    public static final IntProperty PIPE;
    public static final BooleanProperty POWERED;

    public final int requiredTears;
    public final int requiredTier;

    public CenterRuneBlock(int requiredTears, int requiredTier) {
        super(ModBlocks.runeSettings);
        this.requiredTears = requiredTears;
        this.requiredTier = requiredTier;
        setDefaultState(getDefaultState().with(PIPE, 8).with(POWERED, false));
    }

    protected int testCage(BlockView world, BlockPos pos, Direction flipped, PlayerEntity player) {
        boolean pipe = false;
        int actualTier = 10;
        for(int i = 0; i < 8; i++) {
            BlockPos next = NeighborList.platform[i];
            Block block = world.getBlockState(pos.add(next)).getBlock();
            if(block instanceof RuneBlock) {
                int nextTier = ((RuneBlock) block).testCage(world, pos.add(next), flipped);
                actualTier = Math.min(nextTier, actualTier);

                //Check error codes
                if(nextTier < requiredTier) {
                    if(nextTier == -1)
                        return error(player, "barrier");
                    if(nextTier == -2)
                        return error(player, "layer");
                    return error(player, "tier");
                }
            } else
                return error(player, "missing");

            //Check for single pipe
            if(block instanceof PipeConnectorBlock ||
                    block instanceof AdvancedRuneBlock &&
                            world.getBlockState(pos.add(next)).get(AdvancedRuneBlock.PIPE) != 3) {
                if(pipe) {
                    if(world instanceof World)
                        ((World) world).setBlockState(pos, world.getBlockState(pos).with(PIPE, 8));
                    return error(player, "pipes");
                }
                pipe = true;

                //Save pipe location
                if(world instanceof World)
                    ((World) world).setBlockState(pos, world.getBlockState(pos).with(PIPE, i));
            }
        }
        if(!pipe)
            return error(player, "pipe");
        return actualTier;
    }

    protected Direction flipside(BlockView world, BlockPos pos) {
        return world.getBlockState(pos.up()).isOf(ModBlocks.flipRune) ? Direction.DOWN : Direction.UP;
    }

    protected int error(PlayerEntity player, String code) {
        if(player != null && !player.world.isClient) {
            MutableText text = new TranslatableText(Lacrimis.MODID + ".error." + code);
            Style style = text.getStyle();
            player.sendMessage(text.setStyle(style.withColor(Formatting.RED)), false);
        }
        return 0;
    }

    private int actualCost(int tier) {
        if(tier == 3 && requiredTier < 3)
            return requiredTears / 2;
        if(tier > requiredTier)
            return requiredTears - requiredTears / 4;
        return requiredTears;
    }

    private boolean runOnce(World world, BlockPos pos, BlockPos pipe, PlayerEntity player, Direction flipped, int tier) {
        Box box = tier > 2 ? LARGE_BOX : TARGET_BOX;
        
        //For all entities on platform
        pos = pos.offset(flipped);
        for(Entity entity : world.getEntities(null, box.offset(pos.offset(flipped)))) {
            if(activate(world, pos, pipe, entity, player))
                return true;
        }
        return activate(world, pos, pipe, null, player);
    }

    public BlockPos getPipe(BlockView world, BlockPos pos) {
        //Locate pipe connection
        int i = world.getBlockState(pos).get(PIPE);
        if(i == 8)
            return null;
        BlockPos pipe = pos.add(NeighborList.platform[i]);

        //Adjust pipe for tier 3
        if(world.getBlockState(pipe).getBlock() instanceof AdvancedRuneBlock)
            return AdvancedRuneBlock.getPipe(world, pipe);
        else
            return pipe;
    }

    @Override
    public boolean activate(World world, BlockPos pos, PlayerEntity player) {
        //Mark powered
        if(player == null && world.isReceivingRedstonePower(pos))
            world.setBlockState(pos, world.getBlockState(pos).with(POWERED, true));

        //Check soul cage setup
        Direction flipped = flipside(world, pos);
        int tier = testCage(world, pos, flipped, player);
        if(tier < requiredTier)
            return false;

        //Get pipe location
        BlockPos pipe = getPipe(world, pos);
        if(pipe == null)
            return false;

        //Grab required tears
        List<ConduitEntry> tearsList = ConduitUtil.listScanConduits(world, pipe, true);
        if(ConduitUtil.locateTearsStrong(world, tearsList, actualCost(tier), true)) {
            if(runOnce(world, pos, pipe, player, flipped, tier)) {
                ConduitUtil.locateTearsStrong(world, tearsList, actualCost(tier), false);
                return true;
            }
        } else
            error(player, "tears");
        return false;
    }

    protected abstract boolean activate(World world, BlockPos pos, BlockPos pipe, Entity entity, PlayerEntity player);

    @Override
    public boolean canConnectConduitTo(BlockPos pos, BlockView world, Direction side) {
        return false;
    }

    @Override
    public Object extract(BlockPos pos, BlockView world) {
        return null;
    }

    @Override
    public int extractTears(BlockPos pos, BlockView world, int request, boolean simulate) {
        return 0;
    }

    @Override
    public boolean insert(BlockPos pos, BlockView world, Object value) {
        return false;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        Block from = world.getBlockState(fromPos).getBlock();
        boolean a = state.get(POWERED);
        boolean b = world.isReceivingRedstonePower(pos);
        boolean c = from instanceof RuneBlock || from instanceof CenterRuneBlock;
        if(!a && b && !c)
            activate(world, pos, null);
        else if(a && !b)
            world.setBlockState(pos, state.with(POWERED, false));
    }

    @Override
    public void buildTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
        super.buildTooltip(stack, world, tooltip, options);
        tooltip.add(new TranslatableText(Lacrimis.MODID + ".tooltip.rune.tears",
                requiredTears).formatted(Formatting.GRAY));
        tooltip.add(new TranslatableText(Lacrimis.MODID + ".tooltip.rune.tier" + requiredTier).formatted(Formatting.GRAY));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED, PIPE);
    }

    static {
        POWERED = Properties.POWERED;
        PIPE = IntProperty.of("pipe", 0, 8);
    }
}
