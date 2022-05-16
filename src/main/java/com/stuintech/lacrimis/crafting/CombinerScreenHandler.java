package com.stuintech.lacrimis.crafting;

import com.stuintech.lacrimis.Lacrimis;
import com.stuintech.lacrimis.block.entity.CombinerEntity;
import com.stuintech.lacrimis.item.ModItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class CombinerScreenHandler extends AbstractRecipeScreenHandler<InfusionInventory> implements InventoryChangedListener {
    private static final int OUTPUT_SLOT = 0;
    private final InfusionInventory input;
    private final World world;
    private final BlockPos pos;
    private EntityType<?> entityType;

    public CombinerScreenHandler(int syncId, PlayerInventory player, PacketByteBuf buf) {
        this(syncId, player, new InfusionInventory(CombinerEntity.CAPACITY, CombinerEntity.SIZE), null, buf.readBlockPos());
        String s = buf.readString();
        if(!s.equals("null"))
            entityType = Registry.ENTITY_TYPE.get(Identifier.tryParse(s));
    }

    public CombinerScreenHandler(int syncId, PlayerInventory player, InfusionInventory inventory, EntityType<?> type, BlockPos pos) {
        super(ModCrafting.COMBINER_SCREEN_HANDLER, syncId);
        this.world = player.player.world;
        this.input = inventory;
        this.input.addListener(this);
        this.entityType = type;
        this.pos = pos;

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
        updateResult();
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

    private void decrement(int i) {
        ItemStack itemStack = input.getStack(i);
        itemStack.decrement(1);
        input.setStack(i, itemStack);
    }

    public void updateResult() {
        if(entityType != null && input.getStack(1).getItem() == ModItems.taintedSludge &&
                input.getStack(0).getItem() == ModItems.brokenSpawner && input.tank.getSpace() == 0) {
            if(input.getStack(OUTPUT_SLOT).isEmpty()) {
                //Initialize tags
                NbtCompound[] tags = new NbtCompound[5];
                for(int i = 0; i < tags.length; i++)
                    tags[i] = new NbtCompound();

                //Build tags
                String id = EntityType.getId(entityType).toString();
                tags[4].putString("id", id);
                tags[3].put("Entity", tags[4]);
                tags[3].putInt("Weight", 1);
                NbtList list = new NbtList();
                list.add(tags[3]);
                tags[1].put("SpawnPotentials", list);
                tags[2].putString("id", id);
                tags[1].put("SpawnData", tags[2]);
                tags[0].put("BlockEntityTag", tags[1]);

                //Create spawner item
                ItemStack stack = new ItemStack(Items.SPAWNER);
                MutableText text = new TranslatableText(entityType.getTranslationKey());
                text.append(new TranslatableText(Lacrimis.MODID + ".tooltip.spawner"));
                stack.setNbt(tags[0]);
                stack.setCustomName(text);

                //Set output
                decrement(0);
                decrement(1);
                entityType = null;
                input.tank.setTears(0);
                input.setStack(OUTPUT_SLOT, stack);

                //Clear combinerEntity
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if(blockEntity instanceof CombinerEntity)
                    ((CombinerEntity) blockEntity).type = null;
            }
        }
    }

    @Override
    public void onInventoryChanged(Inventory sender) {
        onContentChanged(sender);
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        if(inventory == input)
            this.updateResult();
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
        if(entityType != null) {
            Text t = new TranslatableText(entityType.getTranslationKey());
            return new TranslatableText(Lacrimis.MODID + ".gui.combiner.entity").append(t);
        }
        return new TranslatableText(Lacrimis.MODID + ".gui.combiner.none");
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
        return input.properties.get(InfusionInventory.CAPACITY);
    }

    public int getCurrentTears() {
        return input.properties.get(InfusionInventory.TEARS);
    }
}
