package modfest.lacrimis.block.entity;

import modfest.lacrimis.crafting.CombinerScreenHandler;
import modfest.lacrimis.entity.ModEntities;
import modfest.lacrimis.item.ModItems;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
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

import java.util.stream.IntStream;

public class CombinerEntity extends SoulTankEntity implements ExtendedScreenHandlerFactory {
    public static final int SIZE = 3;
    public static final int CAPACITY = 100;
    public static final int OUTPUT_STACK = 2;
    public static final int[] INPUT_STACKS = {0, 1};
    public EntityType<?> type;

    public CombinerEntity(BlockPos pos, BlockState state) {
        super(ModEntities.combiner, pos, state, CAPACITY, SIZE);
        type = null;
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText("lacrimis.gui.combiner");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new CombinerScreenHandler(syncId, inv, inventory, type, pos);
    }

    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.DOWN)
            return new int[]{OUTPUT_STACK};
        return INPUT_STACKS;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        if(this.isValid(slot, stack))
            return (slot == 0 && stack.isOf(ModItems.brokenSpawner)) || (slot == 1 && stack.isOf(ModItems.taintedSludge));
        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        if(!tag.getString("entity").equals("null"))
            type = Registry.ENTITY_TYPE.get(Identifier.tryParse(tag.getString("entity")));
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);

        if(type != null)
            tag.putString("entity", Registry.ENTITY_TYPE.getId(type).toString());
        else
            tag.putString("entity", "null");
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        String s = "null";
        if(type != null)
            s = Registry.ENTITY_TYPE.getId(type).toString();
        buf.writeBlockPos(pos);
        buf.writeString(s);
    }
}
