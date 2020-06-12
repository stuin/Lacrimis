package modfest.soulflame.infusion;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.screen.PropertyDelegate;
import modfest.soulflame.block.entity.InfusionTableEntity;
import net.minecraft.inventory.SimpleInventory;

public class InfusionInventory extends SimpleInventory {
	public final PropertyDelegate properties;
	public final InfusionTableEntity entity;

	public InfusionInventory(InfusionTableEntity entity) {
		super(9);
		this.entity = entity;

		entity.getTank().setListener(this::markDirty);

		this.properties = new PropertyDelegate() {
			@Override
			public int size() {
				return 1;
			}

			public int get(int index) {
				switch (index) {
					case 0:
						return entity.getLevel();
					default:
						return 0;
				}
			}

			public void set(int index, int value) {
				switch (index) {
					case 0:
						entity.getTank().setTears(value);
						break;
				}
			}
		};

	}

	@Override
	public void markDirty() {
		super.markDirty();
		this.entity.markDirty();
	}

	public int getTears() {
		return properties.get(0);
	}

	public void removeTears(int tears) {
		entity.getTank().removeTears(tears);
	}

	public void readTags(ListTag tags) {
		int j;
		for(j = 0; j < this.size(); ++j) {
			this.setStack(j, ItemStack.EMPTY);
		}

		for(j = 0; j < tags.size(); ++j) {
			CompoundTag compoundTag = tags.getCompound(j);
			int k = compoundTag.getByte("Slot") & 255;
			if (k >= 0 && k < this.size()) {
				this.setStack(k, ItemStack.fromTag(compoundTag));
			}
		}

	}

	public ListTag getTags() {
		ListTag listTag = new ListTag();

		for(int i = 0; i < this.size(); ++i) {
			ItemStack itemStack = this.getStack(i);
			if (!itemStack.isEmpty()) {
				CompoundTag compoundTag = new CompoundTag();
				compoundTag.putByte("Slot", (byte)i);
				itemStack.toTag(compoundTag);
				listTag.add(compoundTag);
			}
		}

		return listTag;
	}

}
