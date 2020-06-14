package modfest.soulflame.block.rune;

import modfest.soulflame.SoulFlame;
import modfest.soulflame.block.Activatable;
import modfest.soulflame.block.BlockConduitConnect;
import modfest.soulflame.init.ModBlocks;
import modfest.soulflame.util.ConduitEntry;
import modfest.soulflame.util.ConduitUtil;
import modfest.soulflame.util.NeighborList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;

public abstract class CenterRuneBlock extends Block implements Activatable, BlockConduitConnect {
    private static final Box TARGET_BOX = new Box(-0.5, -1, -0.5, 1.5, 1, 1.5);

    public static final IntProperty PIPE;
    public final int requiredTears;
    public final int requiredTier;

    public CenterRuneBlock(int requiredTears, int requiredTier) {
        super(ModBlocks.runeSettings);
        this.requiredTears = requiredTears;
        this.requiredTier = requiredTier;
        setDefaultState(getStateManager().getDefaultState().with(PIPE, 8));
    }

    protected boolean testCage(BlockView world, BlockPos pos, Direction flipped) {
        boolean pipe = false;
        for(BlockPos next : NeighborList.platform) {
            Block block = world.getBlockState(pos.add(next)).getBlock();
            if(!(block instanceof RuneBlock && ((RuneBlock) block).testCage(world, pos.add(next), flipped) >= requiredTier))
                return false;

            //Only one pipe allowed
            if(block instanceof PipeConnectorBlock) {
                if(pipe)
                    return false;
                pipe = true;
            }
        }
        return pipe;
    }

    protected Direction flipside(BlockView world, BlockPos pos) {
        Direction dir = Direction.UP;
        if(world.getBlockState(pos.up()).getBlock() == ModBlocks.flipRune)
            dir = Direction.DOWN;
        return dir;
    }

    protected void error(PlayerEntity player, String code) {
        if(player != null && !player.world.isClient) {
            MutableText text = new TranslatableText(SoulFlame.MODID + ".error." + code);
            Style style = text.getStyle();
            player.sendMessage(text.setStyle(style.withColor(Formatting.RED)), false);
        }
    }

    private boolean runOnce(World world, BlockPos pos, List<ConduitEntry> list, PlayerEntity player, Direction flipped) {
        //For all entities on platform
        pos = pos.offset(flipped);
        for(Entity entity : world.getEntities(null, TARGET_BOX.offset(pos.offset(flipped)))) {
            if(entity instanceof LivingEntity
                    && activate(world, pos, list, (LivingEntity) entity, player))
                return true;
        }
        return activate(world, pos, list, null, player);
    }

    public boolean activate(World world, BlockPos pos, PlayerEntity player) {
        //Check soul cage setup
        Direction flipped = flipside(world, pos);
        if(!testCage(world, pos, flipped)) {
            error(player, "platform");
            return false;
        }

        //Locate pipe connection
        int i = world.getBlockState(pos).get(PIPE);
        if(i == 8)
            return false;
        BlockPos pipe = pos.add(NeighborList.platform[i]);

        //Grab required tears
        List<ConduitEntry> list = ConduitUtil.listScanConduits(world, pipe);
        if(ConduitUtil.locateTearsStrong(world, list, requiredTears, true)) {
            if(runOnce(world, pos, list, player, flipped)) {
                ConduitUtil.locateTearsStrong(world, list, requiredTears, false);
                return true;
            }
        } else
            error(player, "tears");
        return false;
    }

    protected abstract boolean activate(World world, BlockPos pos, List<ConduitEntry> list, LivingEntity entity, PlayerEntity player);

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
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(PIPE);
    }

    static {
        PIPE = Properties.LEVEL_8;
    }
}
