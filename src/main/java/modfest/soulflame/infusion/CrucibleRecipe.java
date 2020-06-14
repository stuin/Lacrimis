package modfest.soulflame.infusion;

import com.google.gson.JsonObject;

import modfest.soulflame.init.ModBlocks;
import modfest.soulflame.init.ModInfusion;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

public class CrucibleRecipe implements Recipe<InfusionInventory> {
    private final Ingredient base;
    private final int tears;
    private final ItemStack result;
    private final Identifier id;

    public CrucibleRecipe(Identifier id, Ingredient base, int tears, ItemStack result) {
        this.id = id;
        this.base = base;
        this.tears = tears;
        this.result = result;
    }

    public boolean matches(InfusionInventory inv, World world) {
        return this.base.test(inv.getStack(0)) && inv.getTears() >= tears;
    }

    public ItemStack craft(InfusionInventory inv) {
        ItemStack itemStack = this.result.copy();
        CompoundTag compoundTag = inv.getStack(0).getTag();
        if (compoundTag != null) {
            itemStack.setTag(compoundTag.copy());
        }

        return itemStack;
    }

    @Environment(EnvType.CLIENT)
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    public ItemStack getOutput() {
        return this.result;
    }

    public int getTears() {
        return tears;
    }

    @Environment(EnvType.CLIENT)
    public ItemStack getRecipeKindIcon() {
        return new ItemStack(ModBlocks.crucible);
    }

    public Identifier getId() {
        return this.id;
    }

    public RecipeSerializer<?> getSerializer() {
        return ModInfusion.CRUCIBLE_SERIALIZER;
    }

    public RecipeType<?> getType() {
        return ModInfusion.CRUCIBLE_RECIPE;
    }

    public static class Serializer implements RecipeSerializer<CrucibleRecipe> {
        public CrucibleRecipe read(Identifier identifier, JsonObject jsonObject) {
            Ingredient ingredient = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "base"));
            int tears = JsonHelper.getInt(jsonObject, "tears");
            ItemStack itemStack = ShapedRecipe.getItemStack(JsonHelper.getObject(jsonObject, "result"));
            return new CrucibleRecipe(identifier, ingredient, tears, itemStack);
        }

        public CrucibleRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
            int tears = packetByteBuf.readVarInt();
            ItemStack itemStack = packetByteBuf.readItemStack();
            return new CrucibleRecipe(identifier, ingredient, tears, itemStack);
        }

        public void write(PacketByteBuf packetByteBuf, CrucibleRecipe recipe) {
            recipe.base.write(packetByteBuf);
            packetByteBuf.writeVarInt(recipe.tears);
            packetByteBuf.writeItemStack(recipe.result);
        }
    }
}
