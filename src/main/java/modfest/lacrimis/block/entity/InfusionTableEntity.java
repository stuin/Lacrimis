package modfest.lacrimis.block.entity;

import java.util.Random;

import modfest.lacrimis.infusion.InfusionInventory;
import modfest.lacrimis.init.ModBlockEntityTypes;
import modfest.lacrimis.init.ModParticles;
import modfest.lacrimis.util.ConduitUtil;
import modfest.lacrimis.util.SoulTank;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

public class InfusionTableEntity extends SoulTankEntity implements Inventory, Tickable {
    private static final int TEAR_CAPACITY = 500;

    public final InfusionInventory inventory;

    public InfusionTableEntity() {
        super(ModBlockEntityTypes.infusionTable, TEAR_CAPACITY);
        this.inventory = new InfusionInventory(this, 9);
    }

    @Override
    public void tick() {
        Random random = new Random();
        if (this.world.isClient && this.getTank().getTears() > 0 && random.nextInt(10) == 0) {
            double a = random.nextDouble() * 4 * Math.PI;

            double x = this.pos.getX() + 0.5 + 0.1 * Math.cos(a);
            double z = this.pos.getZ() + 0.5 + 0.1 * Math.sin(a);
            double y = this.pos.getY() + 0.75 + 0.05 * random.nextDouble();

            double dx = 0.005 * Math.cos(a + 1.5 * Math.PI / 2);
            double dz = 0.005 * Math.sin(a + 1.5 * Math.PI / 2);

            this.world.addParticle(ModParticles.PURPLE_MIST, x, y, z, dx, 0.005, dz);

        }
        if (world == null || world.isClient() || getRelativeLevel() == 1)
            return;

        // Locate source
        SoulTank tank = getTank();
        if (tank.getSpace() > 0)
            tank.addTears(ConduitUtil.locateTears(world, pos, tank.getSpace()));
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        this.inventory.clear();
        this.inventory.readTags(tag.getList("Inventory", NbtType.COMPOUND));
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.put("Inventory", this.inventory.getTags());
        return tag;
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
