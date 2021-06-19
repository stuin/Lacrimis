package modfest.lacrimis.crafting;

import modfest.lacrimis.util.SoulTank;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.PropertyDelegate;

import modfest.lacrimis.block.entity.SoulTankEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;

public class InfusionInventory extends SimpleInventory {
	public final static int TEARS = 0;
	public final static int CAPACITY = 1;
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

	public record SoulTankDelegate(SoulTank tank) implements PropertyDelegate {

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
			return 0;
		}

		@Override
		public void set(int index, int value) {
			if(index == TEARS)
				tank.setTears(value);
			else if(index == CAPACITY)
				tank.setLimit(value);
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
