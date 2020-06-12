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
        System.out.println(pos);
        for (int x = -range; x <= range; x++)
            for (int y = -range; y <= range; y++)
                for (int z = -range; z <= range; z++) {
                    Block table = world.getBlockState(pos.add(x, y-1, z)).getBlock();
                    if (table instanceof InfusionTableBlock) {
                        InfusionTableBlock casted = (InfusionTableBlock) table;
                        casted.insert(state, pos.add(x, y-1, z), world, 5, false);
                        System.out.println("Added 5 to " + pos);
                        break;
                    }
                }
    }
}
