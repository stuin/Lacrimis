package modfest.soulflame.block.entity;

import modfest.soulflame.util.ConduitUtil;
import modfest.soulflame.util.SoulTank;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;

import java.util.Optional;

public abstract class SoulTankEntity extends BlockEntity implements BlockEntityClientSerializable {
    private final SoulTank tank;

    public SoulTankEntity(BlockEntityType<?> type, int capacity) {
        super(type);
        tank = new SoulTank(capacity);
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
        int in = value.removeTears(SoulTank.TRANSFER);
        int out = addTears(in);
        if(out < in)
            value.addTears(in - out);

        if (this.world != null && !this.world.isClient)
            this.mark();
        return out != 0;
    }

    public int addTears(int value) {
        value = tank.addTears(value);
        if (this.world != null && !this.world.isClient)
            this.mark();
        return value;
    }

    public int removeTears(int value) {
        value = tank.removeTears(value);
        if (this.world != null && !this.world.isClient)
            this.mark();
        return value;
    }
    
    public Optional<SoulTank> locateSource() {
        if(world == null || world.isClient()) return Optional.empty();
        Optional<ConduitUtil.Entry<SoulTank>> entry = ConduitUtil.scanConduits(world, this.getPos());
        return entry.map(soulTankEntry -> soulTankEntry.extract(world, false)).orElse(null);
    }
    
    public void mark() {
        this.markDirty();
        this.sync();
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
