package modfest.lacrimis.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface Activatable {
    boolean activate(World world, BlockPos pos, PlayerEntity player);
}
