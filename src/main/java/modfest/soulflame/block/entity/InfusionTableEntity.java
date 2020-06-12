package modfest.soulflame.block.entity;

import modfest.soulflame.infusion.InfusionInventory;
import modfest.soulflame.init.ModBlockEntityTypes;

import modfest.soulflame.util.SoulTank;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

import java.util.Optional;

public class InfusionTableEntity extends SoulTankEntity implements Tickable {
	private static final int TEAR_CAPACITY = 500;
	private static final int CHECK_INTERVAL = 10;

	private SoulTank source = null;
	private int check = 0;
	public final InfusionInventory inventory;

	public InfusionTableEntity() {
		super(ModBlockEntityTypes.infusionTable, TEAR_CAPACITY);
		this.inventory = new InfusionInventory(this);
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
	public void tick() {
		if(world == null || world.isClient()) return;
		if(source != null && check != CHECK_INTERVAL) {
			transfer(source);
			check++;
		} else {
			Optional<SoulTank> tank = locateSource();
			if(tank.isPresent()) {
				source = tank.get();
				transfer(source);
				check = 0;
			}
		}
	}

}
