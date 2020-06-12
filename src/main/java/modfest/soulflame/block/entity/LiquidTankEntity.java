package modfest.soulflame.block.entity;

import grondag.fluidity.api.fraction.Fraction;
import grondag.fluidity.api.storage.Store;
import grondag.fluidity.base.storage.ForwardingStore;
import grondag.fluidity.base.storage.bulk.SimpleTank;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;

public class LiquidTankEntity extends BlockEntity {
    protected final SimpleTank tank;
    private final ForwardingStore wrapper = new ForwardingStore();

    public LiquidTankEntity(BlockEntityType<?> type, int capacity) {
        super(type);
        tank = new SimpleTank(new Fraction(capacity));
        wrapper.setWrapped(tank);
    }

    public double getUsage() {
        return tank.usage();
    }

    public Store getEffectiveStorage() {
        return wrapper;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        tank.readTag(tag);
        super.fromTag(state, tag);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tank.writeTag(tag);
        return super.toTag(tag);
    }
}
