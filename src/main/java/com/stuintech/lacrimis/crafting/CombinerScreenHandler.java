package com.stuintech.lacrimis.crafting;

import com.stuintech.lacrimis.Lacrimis;
import com.stuintech.lacrimis.block.entity.CombinerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Optional;

public class CombinerScreenHandler extends AbstractRecipeScreenHandler<CombinerInventory> implements InventoryChangedListener {
    private static final int OUTPUT_SLOT = 0;
    private final CombinerInventory input;
    private final ScreenHandlerContext context;
    private final PlayerEntity player;
    private boolean updating = false;

    public CombinerScreenHandler(int syncId, PlayerInventory player, PacketByteBuf buf) {
        this(syncId, player, new CombinerInventory(CombinerEntity.CAPACITY, CombinerEntity.SIZE), ScreenHandlerContext.EMPTY);
        String s = buf.readString();
        if(!s.equals("null"))
            input.entity = Registry.ENTITY_TYPE.get(Identifier.tryParse(s));
    }

    public CombinerScreenHandler(int syncId, PlayerInventory player, CombinerInventory inventory, ScreenHandlerContext context) {
        super(ModCrafting.COMBINER_SCREEN_HANDLER, syncId);
        this.input = inventory;
        this.input.inventory.addListener(this);
        this.context = context;
        this.player = player.player;

        this.addSlot(new Slot(inventory, 0, 27, 47));
        this.addSlot(new Slot(inventory, 1, 76, 47));
        this.addSlot(new Slot(inventory, 2, 134, 47));

        int k;
        for(k = 0; k < 3; ++k) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(player, j + k * 9 + 9, 8 + j * 18, 84 + k * 18));
            }
        }

        for(k = 0; k < 9; ++k) {
            this.addSlot(new Slot(player, k, 8 + k * 18, 142));
        }
    }

    @Override
    public void populateRecipeFinder(RecipeMatcher finder) {
        this.input.inventory.provideRecipeInputs(finder);
    }

    @Override
    public void clearCraftingSlots() {
    }

    @Override
    public boolean matches(Recipe<? super CombinerInventory> recipe) {
        return recipe.matches(this.input, this.player.world);
    }

    private void decrement(int i) {
        ItemStack itemStack = input.getStack(i);
        itemStack.decrement(1);
        input.setStack(i, itemStack);
    }

    protected static void updateResult(ScreenHandler handler, World world, PlayerEntity player, CombinerInventory inv) {
        if (!world.isClient) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
            ItemStack itemStack = ItemStack.EMPTY;
            Optional<CombinerRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(ModCrafting.COMBINER_RECIPE, inv, world);
            if (optional.isPresent()) {
                CombinerRecipe recipe = optional.get();
                itemStack = recipe.getOutput();
            }

            inv.setStack(OUTPUT_SLOT, itemStack);
            handler.setPreviousTrackedSlot(OUTPUT_SLOT, itemStack);
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 0, itemStack));
        }
    }

    @Override
    public void onInventoryChanged(Inventory sender) {
        //onContentChanged(sender);
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        Lacrimis.LOGGER.warn("changed");
        this.context.run((world, pos) -> {
            updateResult(this, world, this.player, this.input);
        });
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return input.canPlayerUse(player);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index == 2) {
                if (!insertItem(itemStack2, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickTransfer(itemStack2, itemStack);
            } else if (index != 0 && index != 1) {
                if (index >= 3 && index < 39) {
                    if (!insertItem(itemStack2, 0, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.insertItem(itemStack2, 3, 39, false)) {
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

            slot.onTakeItem(player, itemStack2);
        }

        return itemStack;
    }

    public Text getEntity() {
        if(input.entity != null) {
            Text t = Text.translatable(input.entity.getTranslationKey());
            return Text.translatable(Lacrimis.MODID + ".gui.combiner.entity").append(t);
        }
        return Text.translatable(Lacrimis.MODID + ".gui.combiner.none");
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return OUTPUT_SLOT;
    }

    @Override
    public int getCraftingWidth() {
        return 2;
    }

    @Override
    public int getCraftingHeight() {
        return 1;
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
        return input.inventory.properties.get(InfusionInventory.CAPACITY);
    }

    public int getCurrentTears() {
        return input.inventory.properties.get(InfusionInventory.TEARS);
    }
}
