package modfest.lacrimis.crafting;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.block.entity.CombinerEntity;
import modfest.lacrimis.item.ModItems;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class CombinerScreenHandler extends ScreenHandler implements InventoryChangedListener {
    private final SimpleInventory input;
    protected final Inventory output = new CraftingResultInventory();
    private EntityType<?> entityType;
    private BlockPos pos;

    public CombinerScreenHandler(int syncId, PlayerInventory player, PacketByteBuf buf) {
        this(syncId, player, new SimpleInventory(CombinerEntity.SIZE), null, buf.readBlockPos());
        String s = buf.readString();
        if(!s.equals("null"))
            entityType = Registry.ENTITY_TYPE.get(Identifier.tryParse(s));
    }

    public CombinerScreenHandler(int syncId, PlayerInventory player, SimpleInventory inventory, EntityType<?> type, BlockPos pos) {
        super(ModCrafting.COMBINER_SCREEN_HANDLER, syncId);
        this.input = inventory;
        this.input.addListener(this);
        this.entityType = type;
        this.pos = pos;

        this.addSlot(new Slot(inventory, 0, 27, 47));
        this.addSlot(new Slot(inventory, 1, 76, 47));
        this.addSlot(new Slot(output, 2, 134, 47) {
            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                CombinerScreenHandler.this.onTakeOutput(player);
            }
        });

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

    protected void onTakeOutput(PlayerEntity player) {
        decrement(0);
        decrement(1);
        entityType = null;
        output.setStack(0, ItemStack.EMPTY);

        BlockEntity blockEntity = player.world.getBlockEntity(pos);
        if(blockEntity instanceof CombinerEntity)
            ((CombinerEntity) blockEntity).type = null;
    }

    private void decrement(int i) {
        ItemStack itemStack = input.getStack(i);
        itemStack.decrement(1);
        input.setStack(i, itemStack);
    }

    public void updateResult() {
        if(entityType != null && input.getStack(1).getItem() == ModItems.taintedSludge &&
                input.getStack(0).getItem() == ModItems.brokenSpawner) {
            if(output.getStack(0).isEmpty()) {
                String id = EntityType.getId(entityType).toString();
                NbtCompound[] tags = new NbtCompound[5];
                for(int i = 0; i < tags.length; i++)
                    tags[i] = new NbtCompound();

                //Build tags
                tags[4].putString("id", id);
                tags[3].put("Entity", tags[4]);
                tags[3].putInt("Weight", 1);
                NbtList list = new NbtList();
                list.add(tags[3]);
                tags[1].put("SpawnPotentials", list);
                tags[2].putString("id", id);
                tags[1].put("SpawnData", tags[2]);
                tags[0].put("BlockEntityTag", tags[1]);

                //Set output
                ItemStack stack = new ItemStack(Items.SPAWNER);
                MutableText text = new TranslatableText(entityType.getTranslationKey());
                text.append(new TranslatableText(Lacrimis.MODID + ".tooltip.spawner"));
                stack.setNbt(tags[0]);
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
        if(inventory == output && output.getStack(0).isEmpty()) {
            entityType = null;
            this.updateResult();
        }

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
}
