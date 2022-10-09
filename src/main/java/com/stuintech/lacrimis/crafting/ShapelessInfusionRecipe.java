package com.stuintech.lacrimis.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class ShapelessInfusionRecipe extends InfusionRecipe {
	private final DefaultedList<Ingredient> inputs;

	public ShapelessInfusionRecipe(Identifier id, DefaultedList<Ingredient> ingredients, int tears, ItemStack output) {
		super(id, tears, output);
		this.inputs = ingredients;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModCrafting.SHAPELESS_INFUSION_SERIALIZER;
	}

	@Override
	public boolean fits(int width, int height) {
		return width * height >= this.inputs.size();
	}

	@Override
	public DefaultedList<Ingredient> getIngredients() {
		return this.inputs;
	}

	@Override
	public boolean matches(InfusionInventory inventory, World world) {
		RecipeMatcher recipeFinder = new RecipeMatcher();
		int i = 0;

		for(int j = 0; j < inventory.size(); ++j) {
			ItemStack itemStack = inventory.getStack(j);
			if (!itemStack.isEmpty()) {
				++i;
				recipeFinder.addInput(itemStack, 1);
			}
		}

		return i == inputs.size() && recipeFinder.match(this, null);
	}

	public static class Serializer implements RecipeSerializer<ShapelessInfusionRecipe> {
		@Override
		public ShapelessInfusionRecipe read(Identifier identifier, JsonObject json) {
			DefaultedList<Ingredient> defaultedList = getIngredients(JsonHelper.getArray(json, "ingredients"));
			int tears = JsonHelper.getInt(json, "tears");
			if (defaultedList.isEmpty()) {
				throw new JsonParseException("No ingredients for shapeless recipe");
			} else if (defaultedList.size() > 9) {
				throw new JsonParseException("Too many ingredients for shapeless recipe");
			} else {
				ItemStack itemStack = ShapedRecipe.getItem(JsonHelper.getObject(json, "result")).getDefaultStack();
				return new ShapelessInfusionRecipe(identifier, defaultedList, tears, itemStack);
			}
		}

		private static DefaultedList<Ingredient> getIngredients(JsonArray json) {
			DefaultedList<Ingredient> defaultedList = DefaultedList.of();

			for(int i = 0; i < json.size(); ++i) {
				Ingredient ingredient = Ingredient.fromJson(json.get(i));
				if (!ingredient.isEmpty()) {
					defaultedList.add(ingredient);
				}
			}

			return defaultedList;
		}

		@Override
		public ShapelessInfusionRecipe read(Identifier identifier, PacketByteBuf buf) {
			int tears = buf.readVarInt();
			int i = buf.readVarInt();
			DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i, Ingredient.EMPTY);

			for(int j = 0; j < defaultedList.size(); ++j) {
				defaultedList.set(j, Ingredient.fromPacket(buf));
			}

			ItemStack itemStack = buf.readItemStack();
			return new ShapelessInfusionRecipe(identifier, defaultedList, tears, itemStack);
		}

		@Override
		public void write(PacketByteBuf buf, ShapelessInfusionRecipe recipe) {
			buf.writeVarInt(recipe.getTears());
			buf.writeVarInt(recipe.inputs.size());

			for (Ingredient ingredient : recipe.inputs) {
				ingredient.write(buf);
			}

			buf.writeItemStack(recipe.getOutput());
		}
	}
}
