package modfest.soulflame.block.entity;

import modfest.soulflame.util.ConduitUtil;
import modfest.soulflame.util.SoulTank;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public abstract class SoulUserEntity extends SoulTankEntity implements Tickable {
    private static final int CHECK_INTERVAL = 10;
    private SoulTank source = null;
    private int check = 0;

    public SoulUserEntity(BlockEntityType<?> type, int capacity) {
        super(type, capacity);
    }

    public void tick() {
        if(world == null || world.isClient()) return;
        if(source != null && check != CHECK_INTERVAL) {
            transfer(source);
            check++;
        } else {
            Object tank = ConduitUtil.locateSource(world, pos);
            if(tank instanceof SoulTank) {
                source = (SoulTank) tank;
                transfer(source);
                check = 0;
            }
        }
    }
}
