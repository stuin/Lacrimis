package modfest.soulflame.block.rune;

import modfest.soulflame.SoulFlame;
import modfest.soulflame.block.Activatable;
import modfest.soulflame.block.BlockConduitConnect;
import modfest.soulflame.init.ModBlocks;
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
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public abstract class CenterRuneBlock extends Block implements Activatable, BlockConduitConnect {
    private static final Box TARGET_BOX = new Box(-0.5, 1, -0.5, 1.5, 3, 1.5);

    public static final IntProperty PIPE;

    public CenterRuneBlock() {
        super(ModBlocks.runeSettings);
        setDefaultState(getStateManager().getDefaultState().with(PIPE, 8));
    }

    public boolean testCage(BlockView world, BlockPos pos) {
        boolean pipe = false;
        for(BlockPos next : NeighborList.platform) {
            Block block = world.getBlockState(pos.add(next)).getBlock();
            if(!(block instanceof RuneBlock && ((RuneBlock) block).testCage(world, pos.add(next))))
                return false;

            //Only one pipe allowed
            if(block instanceof PipeConnectorBlock) {
                if(pipe)
                    return false;
                pipe = true;
            }
        }
        return true;
    }

    public boolean activate(World world, BlockPos pos, PlayerEntity player) {
        if(!testCage(world, pos)) {
            error(player, "platform");
            return false;
        }
        //For all entities on platform
        for(Entity entity : world.getEntities(null, TARGET_BOX.offset(pos))) {
            if(entity instanceof LivingEntity)
                return activate(world, pos, (LivingEntity) entity, player);
        }
        return false;
    }

    public abstract boolean activate(World world, BlockPos pos, LivingEntity entity, PlayerEntity player);

    public void error(PlayerEntity player, String code) {
        if(player != null && !player.world.isClient) {
            MutableText text = new TranslatableText(SoulFlame.MODID + ".error." + code);
            Style style = text.getStyle();
            player.sendMessage(text.setStyle(style.withColor(Formatting.RED)), false);
        }
    }
    
    public BlockPos pipePos(World world, BlockPos pos) {
        int i = world.getBlockState(pos).get(PIPE);
        if(i == 8)
            return null;
        return pos.add(NeighborList.platform[i]);
    }

    @Override
    public boolean canConnectConduitTo(BlockPos pos, BlockView world, Direction side) {
        return false;
    }

    @Override
    public Object extract(BlockPos pos, BlockView world) {
        return null;
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
