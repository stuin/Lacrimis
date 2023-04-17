package com.stuintech.lacrimis.crafting;

import com.stuintech.lacrimis.Lacrimis;
import com.stuintech.lacrimis.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SpawnerCombinerRecipe extends CombinerRecipe {
    public SpawnerCombinerRecipe(Identifier id) {
        super(id, Ingredient.ofStacks(ModItems.brokenSpawner.getDefaultStack()), null, 100, Items.SPAWNER.getDefaultStack());
    }

    @Override
    public boolean matches(CombinerInventory inv, World world) {
        return getBase().test(inv.getStack(0)) && getBinding().test(inv.getStack(1));
    }

    @Override
    public ItemStack craft(CombinerInventory inv) {
        NbtCompound[] tags = new NbtCompound[5];
        for(int i = 0; i < tags.length; i++)
            tags[i] = new NbtCompound();

        //Build tags
        String id = EntityType.getId(getEntity()).toString();
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
        ItemStack output = super.craft(inv);
        MutableText text = Text.translatable(getEntity().getTranslationKey());
        text.append(Text.translatable(Lacrimis.MODID + ".tooltip.spawner"));
        output.setNbt(tags[0]);
        output.setCustomName(text);

        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModCrafting.SPAWNER_COMBINER_SERIALIZER;
    }
}
