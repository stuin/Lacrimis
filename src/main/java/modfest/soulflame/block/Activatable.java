package modfest.soulflame.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface Activatable {
    boolean activate(World world, BlockPos pos);
}
