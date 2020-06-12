package modfest.soulflame.infusion;

import net.minecraft.screen.PropertyDelegate;
import modfest.soulflame.block.entity.InfusionTableEntity;
import net.minecraft.inventory.SimpleInventory;

public class InfusionInventory extends SimpleInventory {
	public final PropertyDelegate properties;
	public final InfusionTableEntity entity;

	public InfusionInventory(InfusionTableEntity entity) {
		super(9);
		this.entity = entity;

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
}
