package modfest.lacrimis.block;

import com.zundrel.wrenchable.block.BlockWrenchable;
import modfest.lacrimis.block.entity.NetworkLinkEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Arrays;

public class NetworkLinkBlock extends BlockWithEntity implements DuctConnectBlock, BlockWrenchable {
    private static final float[] BLANK = new float[] {1, 1, 1};

    public NetworkLinkBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new NetworkLinkEntity(pos, state);
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onWrenched(World world, PlayerEntity player, BlockHitResult result) {
        NetworkLinkEntity linkEntity = ((NetworkLinkEntity) world.getBlockEntity(result.getBlockPos()));
        if(linkEntity != null) {
            BlockPos pos = result.getBlockPos().up();
            float[] color = BLANK.clone();
            boolean changed = false;
            int l = world.getTopY(Heightmap.Type.WORLD_SURFACE, pos.getX(), pos.getZ());

            int n;
            for (n = 0; n < 10 && pos.getY() <= l; ++n) {
                BlockState blockState = world.getBlockState(pos);
                Block block = blockState.getBlock();
                if (block instanceof Stainable) {
                    float[] fs = ((Stainable)block).getColor().getColorComponents();
                    if (!Arrays.equals(fs, color)) {
                        color[0] = (color[0] + fs[0]) / 2.0F;
                        color[1] = (color[1] + fs[1]) / 2.0F;
                        color[2] = (color[2] + fs[2]) / 2.0F;
                        changed = true;
                    }
                } else if (blockState.getOpacity(world, pos) >= 15 && block != Blocks.BEDROCK) {
                    changed = false;
                    break;
                }

                pos = pos.up();
            }

            linkEntity.setState(changed && !Arrays.equals(color, BLANK), color, world);
        }
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        NetworkLinkEntity linkEntity = ((NetworkLinkEntity) world.getBlockEntity(pos));
        if (linkEntity != null)
            linkEntity.setState(false, null, null);
    }

    @Override
    public boolean canConnectDuctTo(BlockPos pos, WorldAccess world, Direction side) {
        return side != Direction.UP;
    }

    @Override
    public int extractTears(BlockPos pos, World world, int request, boolean simulate) {
        return 0;
    }

    @Override
    public boolean insert(BlockPos pos, World world, Object value) {
        return false;
    }
}
