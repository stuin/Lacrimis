package modfest.lacrimis.block.entity;

import modfest.lacrimis.crafting.CombinerScreenHandler;
import modfest.lacrimis.init.ModEntities;
import modfest.lacrimis.init.ModItems;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

public class CombinerEntity extends BlockEntity implements ExtendedScreenHandlerFactory, SidedInventory, BlockEntityClientSerializable {
    public static final int SIZE = 2;
    private final SimpleInventory inventory;
    public EntityType<?> type;

    public CombinerEntity(BlockPos pos, BlockState state) {
        super(ModEntities.combiner, pos, state);
        inventory = new SimpleInventory(SIZE);
        type = null;
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText("lacrimis.gui.combiner");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new CombinerScreenHandler(syncId, inv, inventory, type);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        if(!tag.getString("entity").equals("null"))
            type = Registry.ENTITY_TYPE.get(Identifier.tryParse(tag.getString("entity")));

        this.inventory.clear();
        this.inventory.readNbtList(tag.getList("Inventory", NbtType.COMPOUND));
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        if(type != null)
            tag.putString("entity", Registry.ENTITY_TYPE.getId(type).toString());
        else
            tag.putString("entity", "null");

        tag.put("Inventory", this.inventory.toNbtList());
        return super.writeNbt(tag);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.writeNbt(new NbtCompound());
    }

    @Override
    public void fromClientTag(NbtCompound tag) {
        this.readNbt(tag);
    }

    @Override
    public NbtCompound toClientTag(NbtCompound tag) {
        return this.writeNbt(tag);
    }


    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.getStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return inventory.removeStack(slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return inventory.removeStack(slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.setStack(slot, stack);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        String s = "null";
        if(type != null)
            s = Registry.ENTITY_TYPE.getId(type).toString();

        buf.writeBlockPos(pos);
        buf.writeString(s);
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return new int[] { 0, 1 };
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        if(slot == 0 && stack.isOf(ModItems.brokenSpawner))
            return true;
        if(slot == 1 && stack.isOf(ModItems.taintedSludge))
            return true;
        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return false;
    }
}
