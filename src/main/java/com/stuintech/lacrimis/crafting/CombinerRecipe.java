package com.stuintech.lacrimis.crafting;

import com.google.gson.JsonObject;
import com.stuintech.lacrimis.item.ModItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class CombinerRecipe implements Recipe<CombinerInventory> {
    private static final Ingredient binding = Ingredient.ofStacks(ModItems.taintedSludge.getDefaultStack());
    private static final int charge = 4;
    private final Ingredient base;
    private final EntityType<?> entity;
    private final int tears;
    private final ItemStack result;
    private final Identifier id;

    public CombinerRecipe(Identifier id, Ingredient base, EntityType<?> entity, int tears, ItemStack result) {
        this.id = id;
        this.base = base;
        this.entity = entity;
        this.tears = tears;
        this.result = result;
    }

    @Override
    public RecipeType<?> getType() {
        return ModCrafting.COMBINER_RECIPE;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModCrafting.COMBINER_SERIALIZER;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean fits(int width, int height) {
        return width * height == 2;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public ItemStack getOutput() {
        return result;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(base);
        list.add(binding);
        return list;
    }

    @Override
    public boolean matches(CombinerInventory inv, World world) {
        boolean items = base.test(inv.getStack(0)) && binding.test(inv.getStack(1));
        return items && entity == inv.entity;
    }

    public boolean complete(CombinerInventory inv, World world) {
        return matches(inv, world) && inv.getTears() > getTears() && inv.getCharge() > charge;
    }

    @Override
    public ItemStack craft(CombinerInventory inv) {
        ItemStack itemStack = this.getOutput().copy();
        NbtCompound compoundTag = inv.getStack(0).getNbt();
        if (compoundTag != null) {
            itemStack.setNbt(compoundTag.copy());
        }

        return itemStack;
    }

    public int getTears() {
        return tears;
    }

    public Ingredient getBase() {
        return base;
    }

    public static Ingredient getBinding() {
        return binding;
    }

    public EntityType<?> getEntity() {
        return entity;
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public ItemStack createIcon() {
        ItemStack item = new ItemStack(ModItems.combiner);
        item.getOrCreateNbt().putInt("TearLevel", getTears());
        item.getOrCreateNbt().putString("Entity", getEntity().getTranslationKey());
        return item;
    }

    public static class Serializer implements RecipeSerializer<CombinerRecipe> {
        @Override
        public CombinerRecipe read(Identifier identifier, JsonObject jsonObject) {
            Ingredient ingredient = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "base"));
            int tears = JsonHelper.getInt(jsonObject, "tears");
            String id = JsonHelper.getString(jsonObject, "entity");
            EntityType<?> type = Registry.ENTITY_TYPE.get(Identifier.tryParse(id));
            ItemStack itemStack = ShapedRecipe.getItem(JsonHelper.getObject(jsonObject, "result")).getDefaultStack();
            return new CombinerRecipe(identifier, ingredient, type, tears, itemStack);
        }

        @Override
        public CombinerRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
            int tears = packetByteBuf.readVarInt();
            String id = packetByteBuf.readString();
            EntityType<?> type = Registry.ENTITY_TYPE.get(Identifier.tryParse(id));
            ItemStack itemStack = packetByteBuf.readItemStack();
            return new CombinerRecipe(identifier, ingredient, type, tears, itemStack);
        }

        @Override
        public void write(PacketByteBuf packetByteBuf, CombinerRecipe recipe) {
            recipe.base.write(packetByteBuf);
            packetByteBuf.writeVarInt(recipe.getTears());
            packetByteBuf.writeString(EntityType.getId(recipe.getEntity()).toString());
            packetByteBuf.writeItemStack(recipe.getOutput());
        }
    }
}
