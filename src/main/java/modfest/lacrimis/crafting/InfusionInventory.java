package modfest.lacrimis.crafting;

import modfest.lacrimis.util.SoulTank;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.PropertyDelegate;

import net.minecraft.screen.ScreenHandler;

public class InfusionInventory extends SimpleInventory {
	public final static int TEARS = 0;
	public final static int CAPACITY = 1;
	public final static int SIGNAL = 2;
	public final SoulTankDelegate properties;
	public final SoulTank tank;

	public InfusionInventory(int capacity, int size) {
		this(new SoulTank(capacity), size);
	}

	public InfusionInventory(SoulTank tank, int size) {
		super(size);
		this.tank = tank;
		this.properties = new SoulTankDelegate(tank);
				
		tank.addListener(this::markDirty);
	}

	public int getTears() {
		return properties.get(TEARS);
	}
	
	public CraftingInventory setupCrafting() {
		CraftingInventory crafting = new CraftingInventory(new CustomScreenHandler(), 3, 3);
		if(size() > 9) {
			for(int i = 0; i < 9; i++)
				crafting.setStack(i, getStack(i));
		}
		return crafting;
	}

	public static class SoulTankDelegate implements PropertyDelegate {
		private final SoulTank tank;
		private boolean signal = false;

		SoulTankDelegate(SoulTank tank) {
			this.tank = tank;
		}

		@Override
		public int size() {
			return 2;
		}

		@Override
		public int get(int index) {
			if(index == TEARS)
				return tank.getTears();
			else if(index == CAPACITY)
				return tank.getCapacity();
			else if(index == SIGNAL)
				return signal ? 1 : 0;
			return 0;
		}

		@Override
		public void set(int index, int value) {
			if(index == TEARS)
				tank.setTears(value);
			else if(index == CAPACITY)
				tank.setLimit(value);
			else if(index == SIGNAL)
				signal = value > 0;
		}

		public boolean hasSignal() {
			return get(SIGNAL) > 0;
		}

		public void setSignal(boolean value) {
			set(SIGNAL, value ? 1 : 0);
		}
	}
	
	private static class CustomScreenHandler extends ScreenHandler {
		protected CustomScreenHandler() {
			super(null, 0);
		}

		@Override
		public boolean canUse(PlayerEntity player) {
			return false;
		}

		@Override
		public void onContentChanged(Inventory inventory) {

		}
	}
}
