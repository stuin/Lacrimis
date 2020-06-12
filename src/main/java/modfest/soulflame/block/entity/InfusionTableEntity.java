package modfest.soulflame.block.entity;

import modfest.soulflame.init.ModBlockEntityTypes;
import net.minecraft.screen.PropertyDelegate;

public class InfusionTableEntity extends SoulTankEntity {
	private static final int TEAR_CAPACITY = 500;
	
	public final PropertyDelegate properties;

	public InfusionTableEntity() {
		super(ModBlockEntityTypes.infusionTable, TEAR_CAPACITY);

		this.properties = new PropertyDelegate() {
			@Override
			public int size() {
				return 1;
			}

			public int get(int index) {
				switch (index) {
				case 0:
					return getLevel();
				default:
					return 0;
				}
			}

			public void set(int index, int value) {
				switch (index) {
				case 0:
					getTank().setTears(value);
					break;
				}
			}

		};
	}
}
