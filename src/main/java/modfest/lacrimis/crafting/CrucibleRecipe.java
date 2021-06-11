package modfest.lacrimis.crafting;

import com.google.gson.JsonObject;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.init.ModCrafting;
import modfest.lacrimis.init.ModItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class CrucibleRecipe extends InfusionRecipe {
    private final Ingredient base;

    public CrucibleRecipe(Identifier id, Ingredient base, int tears, ItemStack result) {
        super(id, tears, result);
        this.base = base;
    }

    @Override
    public RecipeType<?> getType() {
        return ModCrafting.CRUCIBLE_RECIPE;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModCrafting.CRUCIBLE_SERIALIZER;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean fits(int width, int height) {
        return width * height >= 1;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return DefaultedList.ofSize(1, base);
    }

    @Override
    public boolean matches(InfusionInventory inv, World world) {
        ItemStack item = inv.getStack(0);
        if(inv.size() > 1) {
            for(int i = 1; i < 9; i++) {
                if(!inv.getStack(i).isEmpty()) {
                    if(item.isEmpty())
                        item = inv.getStack(i);
                    else
                        return false;
                }
            }
            return base.test(item);
        }
        return base.test(item) && inv.getTears() >= getTears();
    }

    @Override
    public ItemStack craft(InfusionInventory inv) {
        ItemStack itemStack = super.craft(inv);
        NbtCompound compoundTag = inv.getStack(0).getTag();
        if (compoundTag != null) {
            itemStack.setTag(compoundTag.copy());
        }

        return itemStack;
    }

    public Ingredient getBase() {
        return base;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public ItemStack createIcon() {
        ItemStack item = new ItemStack(ModItems.crucible);
        item.getOrCreateTag().putInt("TearLevel", getTears());
        return item;
    }

    public static class Serializer implements RecipeSerializer<CrucibleRecipe> {
        @Override
        public CrucibleRecipe read(Identifier identifier, JsonObject jsonObject) {
            Ingredient ingredient = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "base"));
            int tears = JsonHelper.getInt(jsonObject, "tears");
            ItemStack itemStack = ShapedRecipe.getItem(JsonHelper.getObject(jsonObject, "result")).getDefaultStack();
            return new CrucibleRecipe(identifier, ingredient, tears, itemStack);
        }

        @Override
        public CrucibleRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
            int tears = packetByteBuf.readVarInt();
            ItemStack itemStack = packetByteBuf.readItemStack();
            return new CrucibleRecipe(identifier, ingredient, tears, itemStack);
        }

        @Override
        public void write(PacketByteBuf packetByteBuf, CrucibleRecipe recipe) {
            recipe.base.write(packetByteBuf);
            packetByteBuf.writeVarInt(recipe.getTears());
            packetByteBuf.writeItemStack(recipe.getOutput());
        }
    }
}
