package modfest.lacrimis.crafting;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.block.entity.CombinerEntity;
import modfest.lacrimis.init.ModItems;
import modfest.lacrimis.init.ModNetworking;
import modfest.lacrimis.tarot.TarotCardType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class CombinerScreenHandler extends ScreenHandler implements InventoryChangedListener {
    private final CombinerEntity entity;
    protected final Inventory output = new CraftingResultInventory();

    public CombinerScreenHandler(int syncId, PlayerEntity player, CombinerEntity entity) {
        super(null, syncId);
        this.entity = entity;

        if (entity.getWorld() != null && !entity.getWorld().isClient)
            entity.inventory.addListener(this);

        this.addSlot(new Slot(entity.inventory, 0, 27, 47));
        this.addSlot(new Slot(entity.inventory, 1, 76, 47));
        this.addSlot(new FurnaceOutputSlot(player, output, 0, 134, 47) {
            public ItemStack onTakeItem(PlayerEntity player, ItemStack stack) {
                return CombinerScreenHandler.this.onTakeOutput(stack);
            }
        });

        int k;
        for(k = 0; k < 3; ++k) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(player.inventory, j + k * 9 + 9, 8 + j * 18, 84 + k * 18));
            }
        }

        for(k = 0; k < 9; ++k) {
            this.addSlot(new Slot(player.inventory, k, 8 + k * 18, 142));
        }
        onContentChanged(entity.inventory);
    }

    protected ItemStack onTakeOutput(ItemStack stack) {
        decrement(0);
        decrement(1);
        entity.type = null;
        output.setStack(0, ItemStack.EMPTY);
        ModNetworking.sendCombinerNullPacket(entity.getPos());
        return stack;
    }

    private void decrement(int i) {
        ItemStack itemStack = entity.inventory.getStack(i);
        itemStack.decrement(1);
        entity.inventory.setStack(i, itemStack);
    }

    public void updateResult() {
        if(entity.type != null && entity.inventory.getStack(1).getItem() == ModItems.taintedSludge) {
            if(entity.inventory.getStack(0).getItem() == ModItems.baseTarot)
                for(TarotCardType t : TarotCardType.values())
                    if(t.cover == entity.type) {
                        if(output.getStack(0).isEmpty())
                            output.setStack(0, new ItemStack(ModItems.tarotCards.get(t)));
                        return;
                    }
        }
        if(!output.getStack(0).isEmpty())
            output.setStack(0, ItemStack.EMPTY);
    }

    @Override
    public void onInventoryChanged(Inventory sender) {
        onContentChanged(sender);
    }

    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        if(entity.inventory == inventory)
            this.updateResult();
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.entity.inventory.canPlayerUse(player);
    }

    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index == 2) {
                if (!insertItem(itemStack2, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onStackChanged(itemStack2, itemStack);
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
        if(entity.type != null) {
            Text t = new TranslatableText(entity.type.getTranslationKey());
            return new TranslatableText(Lacrimis.MODID + ".gui.combiner.entity").append(t);
        }    
        return new TranslatableText(Lacrimis.MODID + ".gui.combiner.none");
    }
}
