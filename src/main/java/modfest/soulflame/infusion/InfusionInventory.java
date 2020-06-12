package modfest.soulflame.infusion;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;

public class InfusionInventory extends CraftingInventory {
	public final PropertyDelegate properties;
	
	public InfusionInventory(ScreenHandler handler, PropertyDelegate properties) {
		super(handler, 3, 3);
		this.properties = properties;
	}

}
