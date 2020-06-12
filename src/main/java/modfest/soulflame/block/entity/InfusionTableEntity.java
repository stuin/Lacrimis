package modfest.soulflame.block.entity;

import grondag.fluidity.api.fraction.Fraction;
import grondag.fluidity.base.storage.bulk.SimpleTank;
import modfest.soulflame.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.ScreenHandler;

public class InfusionTableEntity extends BlockEntity {
    private final SimpleTank tank = new SimpleTank(new Fraction(10));

    public InfusionTableEntity() {
        super(ModBlocks.infusionTableEntity);
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
