package com.stuintech.lacrimis.crafting;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class CombinerInventory implements Inventory {
    public static final int MAX = 4;
    public InfusionInventory inventory;
    public EntityType<?> entity = null;
    public int charge = 0;

    public CombinerInventory(InfusionInventory inventory) {
        this.inventory = inventory;
    }

    public CombinerInventory(int capacity, int size) {
        this.inventory = new InfusionInventory(capacity, size);
    }

    public int getTears() {
        return inventory.getTears();
    }

    public EntityType<?> getEntity() {
        return entity;
    }

    public int getCharge() {
        return charge;
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
    public void markDirty() {
        inventory.markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    @Override
    public void clear() {
        inventory.clear();
    }
}
