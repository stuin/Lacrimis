package modfest.lacrimis.infusion;

import com.google.gson.JsonObject;

import modfest.lacrimis.block.entity.InfusionTableEntity;
import modfest.lacrimis.init.ModBlocks;
import modfest.lacrimis.init.ModInfusion;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

public class CrucibleRecipe extends InfusionRecipe {
    private final Ingredient base;

    public CrucibleRecipe(Identifier id, Ingredient base, int tears, ItemStack result) {
        super(id, tears, result);
        this.base = base;
    }

    public RecipeSerializer<?> getSerializer() {
        return ModInfusion.CRUCIBLE_SERIALIZER;
    }

    @Environment(EnvType.CLIENT)
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    public boolean matches(InfusionInventory inv, World world) {
        if(inv.size() > 1) {
            for(int i = 0; i < inv.size() - 1; i++)
                if(base.test(inv.getStack(i)))
                    return true;
                if(!inv.isEmpty())
                    return false;
        }
        return this.base.test(inv.getStack(0)) && inv.getTears() >= getTears();
    }

    @Override
    public ItemStack craft(InfusionInventory inv) {
        ItemStack itemStack = super.craft(inv);
        CompoundTag compoundTag = inv.getStack(0).getTag();
        if (compoundTag != null) {
            itemStack.setTag(compoundTag.copy());
        }

        return itemStack;
    }

    public Ingredient getBase() {
        return base;
    }

    @Environment(EnvType.CLIENT)
    public ItemStack getRecipeKindIcon() {
        return new ItemStack(ModBlocks.crucible);
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
            packetByteBuf.writeVarInt(recipe.getTears());
            packetByteBuf.writeItemStack(recipe.getOutput());
        }
    }
}
