package modfest.lacrimis.block.entity;

import modfest.lacrimis.crafting.InfusionInventory;
import modfest.lacrimis.util.SoulTank;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

public abstract class SoulTankEntity extends BlockEntity implements SidedInventory {
    private final SoulTank tank;
    public final InfusionInventory inventory;

    public SoulTankEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int capacity, int inventory) {
        super(type, pos, state);
        tank = new SoulTank(capacity);
        tank.addListener(this::mark);
        this.inventory = new InfusionInventory(tank, inventory);
    }

    public float getRelativeLevel() {
        return tank.getTears() / (float) tank.getCapacity();
    }

    public SoulTank getTank() {
        return tank;
    }

    public void mark() {
        if (this.world != null) {
            this.markDirty();
            //if(!this.world.isClient)
                //this.sync(); // TODO replace with 1.18 equivalent
        }
    }
    
    public int[] getAvailableSlots(Direction side) {
        return IntStream.rangeClosed(0, inventory.size() - 1).toArray();
    }

    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.isValid(slot, stack);
    }

    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return false;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        tank.setTears(tag.getInt("TearLevel"));
        tank.setLimit(tag.getInt("TearLimit"));

        this.inventory.clear();
        this.inventory.readNbtList(tag.getList("Inventory", NbtType.COMPOUND));
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tag.putInt("TearLevel", tank.getTears());
        tag.putInt("TearLimit", tank.getCapacity());

        tag.put("Inventory", this.inventory.toNbtList());
    }

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.getStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return inventory.removeStack(slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return inventory.removeStack(slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.setStack(slot, stack);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    @Override
    public void clear() {
        inventory.clear();
        getTank().setTears(0);
    }
}
