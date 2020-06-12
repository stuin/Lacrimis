package modfest.soulflame.block;

import net.minecraft.block.*;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TearLantern extends LanternBlock implements Tickable {
    private BlockPos pos;
    private World world;
    private BlockState state;

    public TearLantern(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(HANGING, false));
    }

    @Override
    public void tick() {
        int range = 6;
        for (int x = -range; x < range; x++)
                for (int z = -range; z < range; z++) {
                    Block table = world.getBlockState(new BlockPos(x + this.pos.getX(), this.pos.getY() - 1, z + this.pos.getZ())).getBlock();
                    if (table instanceof InfusionTableBlock){
                        InfusionTableBlock tableEntity = new InfusionTableBlock(settings);
                        tableEntity.insert(state, pos, world, 5, true);
                        break;
                    }
                }
    }
}
