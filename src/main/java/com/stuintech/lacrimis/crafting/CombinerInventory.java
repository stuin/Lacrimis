package com.stuintech.lacrimis.crafting;

import net.minecraft.entity.EntityType;

public class CombinerInventory extends InfusionInventory {
    public EntityType<?> entity = null;
    public int charge = 0;

    public CombinerInventory(int capacity, int size) {
        super(capacity, size);
    }

    public EntityType<?> getEntity() {
        return entity;
    }

    public int getCharge() {
        return charge;
    }
}
