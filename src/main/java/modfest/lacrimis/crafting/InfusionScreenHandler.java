package modfest.lacrimis.crafting;

import modfest.lacrimis.block.entity.InfusionTableEntity;
import modfest.lacrimis.client.init.ClientModNetworking;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import net.minecraft.screen.slot.Slot;

public class InfusionScreenHandler extends AbstractRecipeScreenHandler<InfusionInventory> implements InventoryChangedListener {
	private static final int OUTPUT_SLOT = 0;
	private final InfusionInventory input;
	private final InfusionTableEntity entity;
	private final PlayerEntity player;

	public InfusionScreenHandler(int syncId, PlayerEntity player, InfusionTableEntity entity) {
		super(null, syncId);
		this.input = entity.inventory;
		this.entity = entity;
		this.player = player;
		this.addProperties(input.properties);

		if (entity.getWorld() != null && !entity.getWorld().isClient)
			this.input.addListener(this);

		this.addSlot(new FurnaceOutputSlot(player, this.input, InfusionTableEntity.OUTPUT_STACK, 124, 35));
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 3; ++x) {
				this.addSlot(new Slot(this.input, x + y * 3, 30 + x * 18, 17 + y * 18));
			}
		}
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 9; ++x) {
				this.addSlot(new Slot(player.inventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
			}
		}
		for (int x = 0; x < 9; ++x) {
			this.addSlot(new Slot(player.inventory, x, 8 + x * 18, 142));
		}

		onContentChanged(input);
	}

	public void startCrafting() {
		entity.startCrafting = true;
		ClientModNetworking.sendInfusionStartPacket(entity.getPos());
	}

	@Override
	public void populateRecipeFinder(RecipeFinder finder) {
		this.input.provideRecipeInputs(finder);
	}

	@Override
	public void clearCraftingSlots() {
	}

	@Override
	public boolean matches(Recipe<? super InfusionInventory> recipe) {
		return recipe.matches(this.input, this.player.world);
	}

	@Override
	public void close(PlayerEntity player) {
		super.close(player);
		if(entity.getWorld() != null && !entity.getWorld().isClient)
			this.input.removeListener(this);
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return this.entity.inventory.canPlayerUse(player);
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack leftInHand = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack transfered = slot.getStack();
			leftInHand = transfered.copy();
			if (index == OUTPUT_SLOT) {
				transfered.getItem().onCraft(transfered, this.entity.getWorld(), player);
				if (!this.insertItem(transfered, input.size(), 46, true)) {
					return ItemStack.EMPTY;
				}

				slot.onStackChanged(transfered, leftInHand);
			} else if (index >= input.size() && index < 46) {
				if (!this.insertItem(transfered, 1, 9, false)) {
					if (index < 37) {
						if (!this.insertItem(transfered, 37, 46, false)) {
							return ItemStack.EMPTY;
						}
					} else if (!this.insertItem(transfered, input.size(), 37, false)) {
						return ItemStack.EMPTY;
					}
				}
			} else if (!this.insertItem(transfered, input.size(), 46, false)) {
				return ItemStack.EMPTY;
			}

			if (transfered.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}

			if (transfered.getCount() == leftInHand.getCount()) {
				return ItemStack.EMPTY;
			}

			ItemStack itemStack3 = slot.onTakeItem(player, transfered);
			if (index == OUTPUT_SLOT) {
				player.dropItem(itemStack3, false);
			}
		}

		return leftInHand;
	}

	@Override
	public void onInventoryChanged(Inventory sender) {
		onContentChanged(sender);
	}

	@Override
	public int getCraftingResultSlotIndex() {
		return OUTPUT_SLOT;
	}

	@Override
	public int getCraftingWidth() {
		return 3;
	}

	@Override
	public int getCraftingHeight() {
		return 3;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public int getCraftingSlotCount() {
		return input.size();
	}

	@Override
	public RecipeBookCategory getCategory() {
		return null;
	}

	public int getRequiredTears() {
		return this.entity.getTank().getCapacity();
	}

	public int getCurrentTears() {
		return this.entity.getTank().getTears();
	}
}
