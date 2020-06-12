package modfest.soulflame.infusion;

import grondag.fluidity.api.storage.Store;
import modfest.soulflame.block.ModBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

//Mostly copied from CraftingScreenHandler
public class InfusionScreenHandler extends AbstractRecipeScreenHandler<CraftingInventory> {
	private final CraftingInventory input;
	private final CraftingResultInventory result;
	private final ScreenHandlerContext context;
	private final PlayerEntity player;
	public final Store tank;

	public InfusionScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, null, ScreenHandlerContext.EMPTY);
	}

	public InfusionScreenHandler(int syncId, PlayerInventory playerInventory, Store tank, ScreenHandlerContext context) {
		super(null, syncId);
		this.input = new CraftingInventory(this, 3, 3);
		this.result = new CraftingResultInventory();
		this.context = context;
		this.player = playerInventory.player;
		this.tank = tank;
		this.addSlot(new CraftingResultSlot(playerInventory.player, this.input, this.result, 0, 124, 35));

		int m;
		int l;
		for (m = 0; m < 3; ++m) {
			for (l = 0; l < 3; ++l) {
				this.addSlot(new Slot(this.input, l + m * 3, 30 + l * 18, 17 + m * 18));
			}
		}

		for (m = 0; m < 3; ++m) {
			for (l = 0; l < 9; ++l) {
				this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
			}
		}

		for (m = 0; m < 9; ++m) {
			this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
		}

	}

	protected static void updateResult(int syncId, World world, PlayerEntity player, CraftingInventory craftingInventory, CraftingResultInventory resultInventory) {
		if (!world.isClient) {
			ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
			ItemStack itemStack = ItemStack.EMPTY;
			/*
			 * Optional<CraftingRecipe> optional = world.getServer().getRecipeManager()
			 * .getFirstMatch(RecipeType.CRAFTING, craftingInventory, world);
			 * if (optional.isPresent()) {
			 * CraftingRecipe craftingRecipe = (CraftingRecipe)optional.get();
			 * if (resultInventory.shouldCraftRecipe(world, serverPlayerEntity, craftingRecipe)) {
			 * itemStack = craftingRecipe.craft(craftingInventory);
			 * }
			 * }
			 */

			resultInventory.setStack(0, itemStack);
			serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 0, itemStack));
		}
	}

	public void onContentChanged(Inventory inventory) {
		this.context.run((world, blockPos) -> {
			updateResult(this.syncId, world, this.player, this.input, this.result);
		});
	}

	public void populateRecipeFinder(RecipeFinder finder) {
		this.input.provideRecipeInputs(finder);
	}

	public void clearCraftingSlots() {
		this.input.clear();
		this.result.clear();
	}

	public boolean matches(Recipe<? super CraftingInventory> recipe) {
		return recipe.matches(this.input, this.player.world);
	}

	public void close(PlayerEntity player) {
		super.close(player);
		this.context.run((world, blockPos) -> {
			this.dropInventory(player, world, this.input);
		});
	}

	public boolean canUse(PlayerEntity player) {
		return canUse(this.context, player, ModBlocks.infusionTable);
	}

	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = (Slot) this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();
			if (index == 0) {
				this.context.run((world, blockPos) -> {
					itemStack2.getItem().onCraft(itemStack2, world, player);
				});
				if (!this.insertItem(itemStack2, 10, 46, true)) {
					return ItemStack.EMPTY;
				}

				slot.onStackChanged(itemStack2, itemStack);
			} else if (index >= 10 && index < 46) {
				if (!this.insertItem(itemStack2, 1, 10, false)) {
					if (index < 37) {
						if (!this.insertItem(itemStack2, 37, 46, false)) {
							return ItemStack.EMPTY;
						}
					} else if (!this.insertItem(itemStack2, 10, 37, false)) {
						return ItemStack.EMPTY;
					}
				}
			} else if (!this.insertItem(itemStack2, 10, 46, false)) {
				return ItemStack.EMPTY;
			}

			if (itemStack2.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}

			if (itemStack2.getCount() == itemStack.getCount()) {
				return ItemStack.EMPTY;
			}

			ItemStack itemStack3 = slot.onTakeItem(player, itemStack2);
			if (index == 0) {
				player.dropItem(itemStack3, false);
			}
		}

		return itemStack;
	}

	public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
		return slot.inventory != this.result && super.canInsertIntoSlot(stack, slot);
	}

	public int getCraftingResultSlotIndex() {
		return 0;
	}

	public int getCraftingWidth() {
		return this.input.getWidth();
	}

	public int getCraftingHeight() {
		return this.input.getHeight();
	}

	@Environment(EnvType.CLIENT)
	public int getCraftingSlotCount() {
		return 10;
	}
}
