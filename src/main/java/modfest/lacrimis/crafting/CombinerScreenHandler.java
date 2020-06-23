package modfest.lacrimis.crafting;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.block.entity.CombinerEntity;
import modfest.lacrimis.init.ModItems;
import modfest.lacrimis.init.ModNetworking;
import modfest.lacrimis.tarot.TarotCardType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.io.DataInput;
import java.util.HashMap;
import java.util.Map;

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
            @Override
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
            if(entity.inventory.getStack(0).getItem() == ModItems.brokenSpawner)
                if(output.getStack(0).isEmpty()) {
                    String id = EntityType.getId(entity.type).toString();
                    CompoundTag[] tags = new CompoundTag[5];
                    for(int i = 0; i < tags.length; i++)
                        tags[i] = new CompoundTag();

                    //Build tags
                    tags[4].putString("id", id);
                    tags[3].put("Entity", tags[4]);
                    tags[3].putInt("Weight", 1);
                    ListTag list = new ListTag();
                    list.add(tags[3]);
                    tags[1].put("SpawnPotentials", list);
                    tags[2].putString("id", id);
                    tags[1].put("SpawnData", tags[2]);
                    tags[0].put("BlockEntityTag", tags[1]);

                    //Set output
                    ItemStack stack = new ItemStack(Items.SPAWNER);
                    MutableText text = new TranslatableText(entity.type.getTranslationKey());
                    text.append(new TranslatableText(Lacrimis.MODID + ".tooltip.spawner"));
                    stack.setTag(tags[0]);
                    stack.setCustomName(text);
                    //{BlockEntityTag:{SpawnData:{id:"#ID"},SpawnPotentials:[{Entity:{id:"#ID"}, Weight:1}]}}
                    output.setStack(0, stack);
                }
                return;
        }
        if(!output.getStack(0).isEmpty())
            output.setStack(0, ItemStack.EMPTY);
    }

    @Override
    public void onInventoryChanged(Inventory sender) {
        onContentChanged(sender);
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        if(entity.inventory == inventory)
            this.updateResult();
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.entity.inventory.canPlayerUse(player);
    }

    @Override
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
