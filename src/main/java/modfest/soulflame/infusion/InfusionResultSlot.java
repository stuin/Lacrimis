package modfest.soulflame.infusion;

import modfest.soulflame.init.ModInfusion;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.util.collection.DefaultedList;

public class InfusionResultSlot extends CraftingResultSlot {
	private PlayerEntity player;
	private InfusionInventory input;

	public InfusionResultSlot(PlayerEntity player, InfusionInventory input, Inventory inventory, int index, int x, int y) {
		super(player, input, inventory, index, x, y);
		this.player = player;
		this.input = input;
	}

	public ItemStack onTakeItem(PlayerEntity player, ItemStack stack) {
		this.onCrafted(stack);
		DefaultedList<ItemStack> defaultedList = player.world.getRecipeManager().getRemainingStacks(ModInfusion.INFUSION_RECIPE, this.input, player.world);

		for (int i = 0; i < defaultedList.size(); ++i) {
			ItemStack itemStack = this.input.getStack(i);
			ItemStack itemStack2 = (ItemStack) defaultedList.get(i);
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
