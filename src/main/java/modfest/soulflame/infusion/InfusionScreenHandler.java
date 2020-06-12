package modfest.soulflame.infusion;

import java.util.Optional;

import modfest.soulflame.block.entity.InfusionTableEntity;
import modfest.soulflame.init.ModInfusion;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class InfusionScreenHandler extends AbstractRecipeScreenHandler<InfusionInventory> implements InventoryChangedListener {
	private final CraftingResultInventory result;
	private final InfusionTableEntity entity;
	private final InfusionInventory input;
	private final PlayerEntity player;

	public InfusionScreenHandler(int syncId, PlayerEntity player, InfusionTableEntity entity) {
		super(null, syncId);
		this.input = entity.inventory;
		this.result = new CraftingResultInventory();
		this.entity = entity;
		this.player = player;
		this.addProperties(entity.properties);
		this.addSlot(new InfusionResultSlot(player, this.input, this.result, 0, 124, 35));
		if (!entity.getWorld().isClient) {
			this.input.addListener(this);
		}
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
	}

	protected static void updateResult(int syncId, World world, PlayerEntity player, InfusionInventory inventory, CraftingResultInventory resultInventory) {
		if (!world.isClient) {
			ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
			ItemStack itemStack = ItemStack.EMPTY;

			Optional<ShapedInfusionRecipe> optional = world.getServer()
					.getRecipeManager()
					.getFirstMatch(ModInfusion.INFUSION_RECIPE, inventory, world);
			if (optional.isPresent()) {
				ShapedInfusionRecipe recipe = optional.get();
				if (resultInventory.shouldCraftRecipe(world, serverPlayer, recipe)) {
					itemStack = recipe.craft(inventory);
				}
			}

			resultInventory.setStack(0, itemStack);
			serverPlayer.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 0, itemStack));
		}
	}

	@Override
	public void onInventoryChanged(Inventory sender) {
		onContentChanged(sender);
	}

	@Override
	public void onContentChanged(Inventory inventory) {
		super.onContentChanged(inventory);
		updateResult(this.syncId, this.entity.getWorld(), this.player, this.input, this.result);
	}

	@Override
	public void populateRecipeFinder(RecipeFinder finder) {
		this.input.provideRecipeInputs(finder);
	}

	@Override
	public void clearCraftingSlots() {
		this.input.clear();
		this.result.clear();
	}

	@Override
	public boolean matches(Recipe<? super InfusionInventory> recipe) {
		return recipe.matches(this.input, this.player.world);
	}

	@Override
	public void close(PlayerEntity player) {
		super.close(player);
		if (!player.world.isClient) {
			this.input.removeListener(this);
		}
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
			if (index == 0) {
				transfered.getItem().onCraft(transfered, this.entity.getWorld(), player);
				if (!this.insertItem(transfered, 10, 46, true)) {

					return ItemStack.EMPTY;
				}

				slot.onStackChanged(transfered, leftInHand);
			} else if (index >= 10 && index < 46) {
				if (!this.insertItem(transfered, 1, 10, false)) {
					if (index < 37) {
						if (!this.insertItem(transfered, 37, 46, false)) {
							return ItemStack.EMPTY;
						}
					} else if (!this.insertItem(transfered, 10, 37, false)) {
						return ItemStack.EMPTY;
					}
				}
			} else if (!this.insertItem(transfered, 10, 46, false)) {
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
			if (index == 0) {
				player.dropItem(itemStack3, false);
			}
		}

		return leftInHand;
	}

	@Override
	public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
		return slot.inventory != this.result && super.canInsertIntoSlot(stack, slot);
	}

	@Override
	public int getCraftingResultSlotIndex() {
		return 0;
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
		return 10;
	}

	public int getTearsLevel() {
		return this.entity.getLevel();
	}
}
