package modfest.lacrimis.block.rune;

import com.stuintech.socketwrench.fasteners.FastenerBlock;
import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.block.DuctConnectBlock;
import modfest.lacrimis.init.ModBlocks;
import modfest.lacrimis.util.DuctUtil;
import modfest.lacrimis.util.NeighborList;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.List;

public abstract class CenterRuneBlock extends Block implements DuctConnectBlock, FastenerBlock {
    private static final Box TARGET_BOX = new Box(-0.5, -1, -0.5, 1.5, 1, 1.5);
    private static final Box LARGE_BOX = new Box(-1.5, -1, -1.5, 2.5, 1, 2.5);

    public static final IntProperty DUCT;
    public static final BooleanProperty POWERED;

    public final int requiredTears;
    public final int requiredTier;

    public CenterRuneBlock(int requiredTears, int requiredTier) {
        super(ModBlocks.runeSettings);
        this.requiredTears = requiredTears;
        this.requiredTier = requiredTier;
        setDefaultState(getDefaultState().with(DUCT, 8).with(POWERED, false));
    }

    protected int testCage(World world, BlockPos pos, Direction flipped, PlayerEntity player) {
        boolean duct = false;
        int actualTier = 10;
        for(int i = 0; i < 8; i++) {
            BlockPos next = NeighborList.platform[i];
            Block block = world.getBlockState(pos.add(next)).getBlock();
            if(block instanceof BasicRuneBlock) {
                int nextTier = ((BasicRuneBlock) block).testCage(world, pos.add(next), flipped);
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

            //Check for single duct
            if(block instanceof DuctRuneBlock ||
                    block instanceof AdvancedRuneBlock &&
                            world.getBlockState(pos.add(next)).get(AdvancedRuneBlock.PIPE) != 3) {
                if(duct) {
                    world.setBlockState(pos, world.getBlockState(pos).with(DUCT, 8));
                    return error(player, "ducts");
                }
                duct = true;

                //Save duct location
                world.setBlockState(pos, world.getBlockState(pos).with(DUCT, i));
            }
        }
        if(!duct)
            return error(player, "duct");
        return actualTier;
    }

    protected Direction flipside(World world, BlockPos pos) {
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

    protected int actualCost(int tier) {
        if(tier == 3 && requiredTier < 3)
            return requiredTears / 2;
        if(tier > requiredTier)
            return requiredTears - requiredTears / 4;
        return requiredTears;
    }
    
    protected Box getTargetBox(BlockPos pos, Direction flipped, int tier) {
        Box box = tier > 2 ? LARGE_BOX : TARGET_BOX;
        pos = pos.offset(flipped);
        return box.offset(pos.offset(flipped));
    }

    private boolean runOnce(World world, BlockPos pos, BlockPos duct, PlayerEntity player, Direction flipped, int tier) {
        //For all entities on platform
        for(Entity entity : world.getNonSpectatingEntities(Entity.class, getTargetBox(pos, flipped, tier))) {
            if(onActivate(world, pos.offset(flipped), duct, entity, player))
                return true;
        }
        return onActivate(world, pos.offset(flipped), duct, null, player);
    }

    public BlockPos getDuct(World world, BlockPos pos) {
        //Locate duct connection
        int i = world.getBlockState(pos).get(DUCT);
        if(i == 8)
            return null;
        BlockPos duct = pos.add(NeighborList.platform[i]);

        //Adjust duct for tier 3
        if(world.getBlockState(duct).getBlock() instanceof AdvancedRuneBlock)
            return AdvancedRuneBlock.getDuct(world, duct);
        else
            return duct;
    }

    @Override
    public boolean onFasten(PlayerEntity player, World world, BlockPos pos, Vec3d vec3d, Direction direction) {
        if(!player.isSneaking()) {
            activate(world, pos, player);
            return true;
        }
        return false;
    }

    public void activate(World world, BlockPos pos, PlayerEntity player) {
        //Mark powered
        if(player == null && world.isReceivingRedstonePower(pos))
            world.setBlockState(pos, world.getBlockState(pos).with(POWERED, true));

        //Check soul cage setup
        Direction flipped = flipside(world, pos);
        int tier = testCage(world, pos, flipped, player);
        if(tier < requiredTier)
            return;

        //Get duct location
        BlockPos duct = getDuct(world, pos);
        if(duct == null)
            return;

        //Grab required tears
        List<BlockPos> tearsList = DuctUtil.scanDucts(world, duct, true, actualCost(tier), (p, b, r) -> b.extractTears(p, world, r, true));
        if(tearsList.size() > 0) {
            if(runOnce(world, pos, duct, player, flipped, tier)) {
                int goal = actualCost(tier);
                for(BlockPos pos1 : tearsList)
                    goal -= ((DuctConnectBlock)world.getBlockState(pos1).getBlock()).extractTears(pos1, world, goal, false);
            }
        } else if(world.isClient())
            error(player, "tears");
    }

    protected abstract boolean onActivate(World world, BlockPos pos, BlockPos duct, Entity entity, PlayerEntity player);

    @Override
    public boolean canConnectDuctTo(BlockPos pos, WorldAccess world, Direction side) {
        return false;
    }

    @Override
    public int extractTears(BlockPos pos, World world, int request, boolean simulate) {
        return 0;
    }

    @Override
    public boolean insert(BlockPos pos, World world, Object value) {
        return false;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        Block from = world.getBlockState(fromPos).getBlock();
        boolean a = state.get(POWERED);
        boolean b = world.isReceivingRedstonePower(pos);
        boolean c = from instanceof BasicRuneBlock || from instanceof CenterRuneBlock;
        if(!a && b && !c)
            activate(world, pos, null);
        else if(a && !b)
            world.setBlockState(pos, state.with(POWERED, false));
    }

    @Override
    public void appendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);
        tooltip.add(new TranslatableText(Lacrimis.MODID + ".tooltip.rune.tears",
                requiredTears).formatted(Formatting.GRAY));
        tooltip.add(new TranslatableText(Lacrimis.MODID + ".tooltip.rune.tier" + requiredTier).formatted(Formatting.GRAY));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED, DUCT);
    }

    static {
        POWERED = Properties.POWERED;
        DUCT = IntProperty.of("duct", 0, 8);
    }
}
