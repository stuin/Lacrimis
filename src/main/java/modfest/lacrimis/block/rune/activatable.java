package modfest.lacrimis.block.rune;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface activatable {

    public void activate(World world, BlockPos pos, PlayerEntity player);

}
