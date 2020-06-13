package modfest.soulflame.block.entity;

import modfest.soulflame.util.SoulTank;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;

public abstract class SoulTankEntity extends BlockEntity implements BlockEntityClientSerializable {
    private static final int MAX_TRANSFER = 10;
    private final SoulTank tank;

    public SoulTankEntity(BlockEntityType<?> type, int capacity) {
        super(type);
        tank = new SoulTank(capacity);
        tank.addListener(this::mark);
    }

    @Environment(EnvType.CLIENT)
    public float getRelativeLevel() {
        return tank.getTears() / (float) tank.getCapacity();
    }
    
    public int getLevel() {
        return tank.getTears();
    }
    
    public SoulTank getTank() {
        return tank;
    }

    public boolean transfer(SoulTank value) {
        return tank.transfer(value, MAX_TRANSFER);
    }

    public int addTears(int value) {
        value = tank.addTears(value);
        return value;
    }

    public int removeTears(int value) {
        value = tank.removeTears(value);
        return value;
    }
    
    public void mark() {
        if (this.world != null) {
            this.markDirty();
            if(!this.world.isClient)
                this.sync();
        }
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);

        tank.setTears(tag.getInt("TearLevel"));
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putInt("TearLevel", tank.getTears());
        return tag;
    }

    @Override
    public CompoundTag toInitialChunkDataTag() {
        return this.toTag(new CompoundTag());
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        this.fromTag(getCachedState(), tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return this.toTag(tag);
    }
}
