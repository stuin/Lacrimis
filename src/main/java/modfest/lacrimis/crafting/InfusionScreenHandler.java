package modfest.lacrimis.crafting;

import modfest.lacrimis.block.entity.InfusionTableEntity;
import modfest.lacrimis.init.ModCrafting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

public class InfusionScreenHandler extends AbstractRecipeScreenHandler<InfusionInventory> implements InventoryChangedListener {
	private static final int OUTPUT_SLOT = 0;
	private final InfusionInventory input;
	private final World world;

	public InfusionScreenHandler(int syncId, PlayerInventory player) {
		this(syncId, player, new InfusionInventory(InfusionTableEntity.CAPACITY, InfusionTableEntity.SIZE));
	}

	public InfusionScreenHandler(int syncId, PlayerInventory player, InfusionInventory inventory) {
		super(ModCrafting.INFUSION_SCREEN_HANDLER, syncId);
		this.world = player.player.world;
		this.input = inventory;
		this.input.addListener(this);
		this.addProperties(input.properties);

		this.addSlot(new FurnaceOutputSlot(player.player, this.input, InfusionTableEntity.OUTPUT_STACK, 124, 35));
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 3; ++x) {
				this.addSlot(new Slot(this.input, x + y * 3, 30 + x * 18, 17 + y * 18));
			}
		}
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 9; ++x) {
				this.addSlot(new Slot(player, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
			}
		}
		for (int x = 0; x < 9; ++x) {
			this.addSlot(new Slot(player, x, 8 + x * 18, 142));
		}

		onContentChanged(input);
	}

	@Override
	public boolean onButtonClick(PlayerEntity player, int id) {
		if(id == 0)
			input.properties.setSignal(true);
		return super.onButtonClick(player, id);
	}

	@Override
	public void populateRecipeFinder(RecipeMatcher finder) {
		this.input.provideRecipeInputs(finder);
	}

	@Override
	public void clearCraftingSlots() {
	}

	@Override
	public boolean matches(Recipe<? super InfusionInventory> recipe) {
		return recipe.matches(this.input, this.world);
	}

	@Override
	public void close(PlayerEntity player) {
		super.close(player);
		this.input.removeListener(this);
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return this.input.canPlayerUse(player);
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack leftInHand = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasStack()) {
			ItemStack transfered = slot.getStack();
			leftInHand = transfered.copy();
			if (index == OUTPUT_SLOT) {
				transfered.getItem().onCraft(transfered, player.world, player);
				if (!this.insertItem(transfered, input.size(), 46, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickTransfer(transfered, leftInHand);
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

			slot.onTakeItem(player, transfered);
			/*if (index == OUTPUT_SLOT) {
				player.dropItem(itemStack3, false);
			}*/
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

	@Override
	public boolean canInsertIntoSlot(int index) {
		return false;
	}

	public int getRequiredTears() {
		return input.properties.get(InfusionInventory.CAPACITY);
	}

	public int getCurrentTears() {
		return input.properties.get(InfusionInventory.TEARS);
	}
}
