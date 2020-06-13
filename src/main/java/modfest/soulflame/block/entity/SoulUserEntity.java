package modfest.soulflame.block.entity;

import modfest.soulflame.util.ConduitUtil;
import modfest.soulflame.util.SoulTank;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Tickable;

public abstract class SoulUserEntity extends SoulTankEntity implements Tickable {
    private static final int CHECK_INTERVAL = 10;
    private SoulTank source = null;
    private int check = 0;

    public SoulUserEntity(BlockEntityType<?> type, int capacity) {
        super(type, capacity);
        getTank().disableExtract();
    }

    public void tick() {
        if(world == null || world.isClient() || getRelativeLevel() == 1) return;

        //Locate source tank
        if(source != null && check < CHECK_INTERVAL) {
            transfer(source);
            check++;
        } else {
            Object tank = ConduitUtil.locateSource(world, pos, SoulTank::SOURCE);
            if(tank instanceof SoulTank) {
                source = (SoulTank) tank;
                transfer(source);
                check = 0;
            }
        }
    }
}
