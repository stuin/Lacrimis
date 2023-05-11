package com.stuintech.lacrimis.block.entity;

import com.stuintech.lacrimis.crafting.CombinerInventory;
import com.stuintech.lacrimis.crafting.CombinerScreenHandler;
import com.stuintech.lacrimis.entity.ModEntities;
import com.stuintech.lacrimis.item.ModItems;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

public class CombinerEntity extends SoulTankEntity implements ExtendedScreenHandlerFactory {
    public static final int SIZE = 3;
    public static final int CAPACITY = 100;
    public static final int OUTPUT_STACK = 2;
    public static final int[] INPUT_STACKS = {0, 1};
    public final CombinerInventory combinerInventory;

    public CombinerEntity(BlockPos pos, BlockState state) {
        super(ModEntities.combiner, pos, state, CAPACITY, SIZE);
        combinerInventory = new CombinerInventory(this.inventory);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("lacrimis.gui.combiner");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new CombinerScreenHandler(syncId, inv, combinerInventory, ScreenHandlerContext.create(world, pos));
    }

    public EntityType<?> getEntity() {
        return combinerInventory.getEntity();
    }

    public int getCharge() {
        return combinerInventory.getCharge();
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
            combinerInventory.entity = Registry.ENTITY_TYPE.get(Identifier.tryParse(tag.getString("entity")));
        combinerInventory.charge = tag.getInt("charge");
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);

        if(combinerInventory.entity != null)
            tag.putString("entity", Registry.ENTITY_TYPE.getId(combinerInventory.entity).toString());
        else
            tag.putString("entity", "null");
        tag.putInt("charge", combinerInventory.charge);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        String s = "null";
        if(combinerInventory.entity != null)
            s = Registry.ENTITY_TYPE.getId(combinerInventory.entity).toString();
        buf.writeString(s);
        buf.writeInt(combinerInventory.charge);
    }
}
