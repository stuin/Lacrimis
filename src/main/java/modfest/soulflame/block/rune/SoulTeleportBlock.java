package modfest.soulflame.block.rune;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class SoulTeleportBlock extends CenterRuneBlock {

    @Override
    public boolean activate(World world, BlockPos pos, LivingEntity entity, PlayerEntity player) {
        return false;
    }

    @Override
    public boolean insert(BlockPos pos, BlockView world, Object value) {
        if(value instanceof LivingEntity && testCage(world, pos)) {
            ((LivingEntity) value).teleport(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            return true;
        }
        return false;
    }
}
