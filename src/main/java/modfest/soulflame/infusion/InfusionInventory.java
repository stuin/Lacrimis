package modfest.soulflame.infusion;

import modfest.soulflame.block.entity.InfusionTableEntity;
import net.minecraft.inventory.SimpleInventory;

public class InfusionInventory extends SimpleInventory {
	public final InfusionTableEntity entity;

	public InfusionInventory(InfusionTableEntity entity) {
		super(9);
		this.entity = entity;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		this.entity.markDirty();
	}
}
