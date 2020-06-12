package modfest.soulflame.infusion;

import modfest.soulflame.init.ModInfusion;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;

public class InfusionResultSlot extends Slot {
	private PlayerEntity player;
	private InfusionInventory input;
	private int amount;

	public InfusionResultSlot(PlayerEntity player, InfusionInventory input, Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
		this.player = player;
		this.input = input;
	}

	@Override
	public boolean canInsert(ItemStack stack) {
		return false;
	}

	@Override
	public ItemStack takeStack(int amount) {
		if (this.hasStack()) {
			this.amount += Math.min(amount, this.getStack().getCount());
		}

		return super.takeStack(amount);
	}

	@Override
	protected void onCrafted(ItemStack stack, int amount) {
		this.amount += amount;
		this.onCrafted(stack);
	}

	@Override
	protected void onTake(int amount) {
		this.amount += amount;
	}

	@Override
	protected void onCrafted(ItemStack stack) {
		if (this.amount > 0) {
			stack.onCraft(this.player.world, this.player, this.amount);
		}

		if (this.inventory instanceof RecipeUnlocker) {
			((RecipeUnlocker) this.inventory).unlockLastRecipe(this.player);
		}

		this.amount = 0;
	}

	@Override
	public ItemStack onTakeItem(PlayerEntity player, ItemStack stack) {
		this.onCrafted(stack);
		DefaultedList<ItemStack> defaultedList = player.world.getRecipeManager().getRemainingStacks(ModInfusion.INFUSION_RECIPE, this.input, player.world);

		for (int i = 0; i < defaultedList.size(); ++i) {
			ItemStack itemStack = this.input.getStack(i);
			ItemStack itemStack2 = defaultedList.get(i);
			if (!itemStack.isEmpty()) {
				this.input.removeStack(i, 1);
				itemStack = this.input.getStack(i);
			}

			if (!itemStack2.isEmpty()) {
				if (itemStack.isEmpty()) {
					this.input.setStack(i, itemStack2);
				}
				if (ItemStack.areItemsEqualIgnoreDamage(itemStack, itemStack2) && ItemStack.areTagsEqual(itemStack, itemStack2)) {
					itemStack2.increment(itemStack.getCount());
					this.input.setStack(i, itemStack2);
				}
				if (!this.player.inventory.insertStack(itemStack2)) {
					this.player.dropItem(itemStack2, false);
				}
			}
		}

		return stack;
	}
}
