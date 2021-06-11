package modfest.lacrimis.crafting;

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
	public final CustomDelegate properties;
	public final SoulTankEntity entity;

	public InfusionInventory(SoulTankEntity entity, int size) {
		super(size);
		this.entity = entity;
		this.properties = new CustomDelegate(entity);
				
		entity.getTank().addListener(this::markDirty);
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
	
	public CraftingInventory setupCrafting() {
		CraftingInventory crafting = new CraftingInventory(new CustomScreenHandler(), 3, 3);
		if(size() > 9) {
			for(int i = 0; i < 9; i++)
				crafting.setStack(i, getStack(i));
		}
		return crafting;
	}

	@Override
    public void readNbtList(NbtList tags) {
		int j;
		for(j = 0; j < this.size(); ++j) {
			this.setStack(j, ItemStack.EMPTY);
		}

		for(j = 0; j < tags.size(); ++j) {
			NbtCompound compoundTag = tags.getCompound(j);
			int k = compoundTag.getByte("Slot") & 255;
			if (k < this.size()) {
				this.setStack(k, ItemStack.fromNbt(compoundTag));
			}
		}

	}

	@Override
    public NbtList toNbtList() {
		NbtList listTag = new NbtList();

		for(int i = 0; i < this.size(); ++i) {
			ItemStack itemStack = this.getStack(i);
			if (!itemStack.isEmpty()) {
				NbtCompound compoundTag = new NbtCompound();
				compoundTag.putByte("Slot", (byte)i);
				itemStack.writeNbt(compoundTag);
				listTag.add(compoundTag);
			}
		}

		return listTag;
	}
	
	private static class CustomDelegate implements PropertyDelegate {
		private final SoulTankEntity entity;
		
		CustomDelegate(SoulTankEntity entity) {
			this.entity = entity;
		}

		@Override
		public int size() {
			return 1;
		}

		@Override
		public int get(int index) {
			switch (index) {
				case 0:
					return entity.getTank().getTears();
				default:
					return 0;
			}
		}

		@Override
		public void set(int index, int value) {
			switch (index) {
				case 0:
					entity.getTank().setTears(value);
					break;
			}
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
