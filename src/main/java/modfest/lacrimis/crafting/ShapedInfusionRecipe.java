package modfest.lacrimis.crafting;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import modfest.lacrimis.init.ModCrafting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ShapedInfusionRecipe extends InfusionRecipe {
	private final int width;
	private final int height;
	private final DefaultedList<Ingredient> inputs;

	public ShapedInfusionRecipe(Identifier id, int width, int height, DefaultedList<Ingredient> ingredients, int tears, ItemStack output) {
		super(id, tears, output);
		this.width = width;
		this.height = height;
		this.inputs = ingredients;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModCrafting.SHAPED_INFUSION_SERIALIZER;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean fits(int width, int height) {
		return width >= this.width && height >= this.height;
	}


	@Override
	public DefaultedList<Ingredient> getIngredients() {
		return this.inputs;
	}

	@Override
	public boolean matches(InfusionInventory inventory, World world) {
		for (int i = 0; i <= 3 - this.width; ++i) {
			for (int j = 0; j <= 3 - this.height; ++j) {
				if (this.matchesSmall(inventory, i, j, true)) {
					return true;
				}

				if (this.matchesSmall(inventory, i, j, false)) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean matchesSmall(InfusionInventory inv, int offsetX, int offsetY, boolean bl) {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				int k = i - offsetX;
				int l = j - offsetY;
				Ingredient ingredient = Ingredient.EMPTY;
				if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
					if (bl) {
						ingredient = this.inputs.get(this.width - k - 1 + l * this.width);
					} else {
						ingredient = this.inputs.get(k + l * this.width);
					}
				}

				if (!ingredient.test(inv.getStack(i + j * 3))) {
					return false;
				}
			}
		}

		return true;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	private static DefaultedList<Ingredient> getIngredients(String[] pattern, Map<String, Ingredient> key, int width, int height) {
		DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(width * height, Ingredient.EMPTY);
		Set<String> set = Sets.newHashSet(key.keySet());
		set.remove(" ");

		for (int i = 0; i < pattern.length; ++i) {
			for (int j = 0; j < pattern[i].length(); ++j) {
				String string = pattern[i].substring(j, j + 1);
				Ingredient ingredient = key.get(string);
				if (ingredient == null) {
					throw new JsonSyntaxException("Pattern references symbol '" + string + "' but it's not defined in the key");
				}

				set.remove(string);
				defaultedList.set(j + width * i, ingredient);
			}
		}

		if (!set.isEmpty()) {
			throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
		}
		return defaultedList;
	}

	@VisibleForTesting
	static String[] combinePattern(String... lines) {
		int i = Integer.MAX_VALUE;
		int j = 0;
		int k = 0;
		int l = 0;

		for (int m = 0; m < lines.length; ++m) {
			String string = lines[m];
			i = Math.min(i, findNextIngredient(string));
			int n = findNextIngredientReverse(string);
			j = Math.max(j, n);
			if (n < 0) {
				if (k == m) {
					++k;
				}

				++l;
			} else {
				l = 0;
			}
		}

		if (lines.length == l) {
			return new String[0];
		}
		String[] pattern = new String[lines.length - l - k];

		for (int o = 0; o < pattern.length; ++o) {
			pattern[o] = lines[o + k].substring(i, j + 1);
		}

		return pattern;
	}

	private static int findNextIngredient(String pattern) {
		int i;
		for (i = 0; i < pattern.length() && pattern.charAt(i) == ' '; ++i) {
		}

		return i;
	}

	private static int findNextIngredientReverse(String pattern) {
		int i;
		for (i = pattern.length() - 1; i >= 0 && pattern.charAt(i) == ' '; --i) {
		}

		return i;
	}

	private static String[] getPattern(JsonArray json) {
		String[] pattern = new String[json.size()];
		if (pattern.length > 3) {
			throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
		}
		if (pattern.length == 0) {
			throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
		}
		for (int i = 0; i < pattern.length; ++i) {
			String line = JsonHelper.asString(json.get(i), "pattern[" + i + "]");
			if (line.length() > 3) {
				throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
			}

			if (i > 0 && pattern[0].length() != line.length()) {
				throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
			}

			pattern[i] = line;
		}

		return pattern;
	}

	private static Map<String, Ingredient> getComponents(JsonObject json) {
		Map<String, Ingredient> map = Maps.newHashMap();

		for (Entry<String, JsonElement> entry : json.entrySet()) {
			if (entry.getKey().length() != 1) {
				throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
			}

			if (" ".equals(entry.getKey())) {
				throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
			}

			map.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));
		}

		map.put(" ", Ingredient.EMPTY);
		return map;
	}

	public static ItemStack getItemStack(JsonObject json) {
		String itemString = JsonHelper.getString(json, "item");
		Item item = Registry.ITEM.getOrEmpty(new Identifier(itemString)).orElseThrow(() -> {
			return new JsonSyntaxException("Unknown item '" + itemString + "'");
		});
		if (json.has("data")) {
			throw new JsonParseException("Disallowed data tag found");
		} else {
			int i = JsonHelper.getInt(json, "count", 1);
			return new ItemStack(item, i);
		}
	}

	public static class Serializer implements RecipeSerializer<ShapedInfusionRecipe> {
		@Override
		public ShapedInfusionRecipe read(Identifier identifier, JsonObject json) {
			Map<String, Ingredient> key = ShapedInfusionRecipe.getComponents(JsonHelper.getObject(json, "key"));
			String[] pattern = ShapedInfusionRecipe.combinePattern(ShapedInfusionRecipe.getPattern(JsonHelper.getArray(json, "pattern")));
			int width = pattern[0].length();
			int height = pattern.length;
			int tears = JsonHelper.getInt(json, "tears");
			DefaultedList<Ingredient> ingredients = ShapedInfusionRecipe.getIngredients(pattern, key, width, height);
			ItemStack result = ShapedInfusionRecipe.getItemStack(JsonHelper.getObject(json, "result"));
			return new ShapedInfusionRecipe(identifier, width, height, ingredients, tears, result);
		}

		@Override
		public ShapedInfusionRecipe read(Identifier identifier, PacketByteBuf buf) {
			int width = buf.readVarInt();
			int height = buf.readVarInt();
			int tears = buf.readVarInt();
			DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(width * height, Ingredient.EMPTY);

			for (int k = 0; k < ingredients.size(); ++k) {
				ingredients.set(k, Ingredient.fromPacket(buf));
			}

			ItemStack result = buf.readItemStack();
			return new ShapedInfusionRecipe(identifier, width, height, ingredients, tears, result);
		}

		@Override
		public void write(PacketByteBuf buf, ShapedInfusionRecipe recipe) {
			buf.writeVarInt(recipe.width);
			buf.writeVarInt(recipe.height);
			buf.writeVarInt(recipe.getTears());

			for (Ingredient ingredient : recipe.inputs) {
				ingredient.write(buf);
			}

			buf.writeItemStack(recipe.getOutput());
		}
	}
}
