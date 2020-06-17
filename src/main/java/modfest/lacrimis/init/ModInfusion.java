package modfest.lacrimis.init;

import com.google.gson.annotations.SerializedName;
import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.block.entity.InfusionTableEntity;
import modfest.lacrimis.client.patchiouli.PageCrucible;
import modfest.lacrimis.client.patchiouli.PageInfusion;
import modfest.lacrimis.infusion.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.ContainerScreenFactory;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.container.ContainerFactory;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import vazkii.patchouli.client.book.ClientBookRegistry;

public class ModInfusion {
	public static final Identifier INFUSION_SCREEN_ID = new Identifier(Lacrimis.MODID, "infusion");

	public static final RecipeType<InfusionRecipe> INFUSION_RECIPE = new RecipeType<InfusionRecipe>() {
		@Override
		public String toString() {
			return Lacrimis.MODID + ":infusion";
		}
	};
	public static final RecipeType<CrucibleRecipe> CRUCIBLE_RECIPE = new RecipeType<CrucibleRecipe>() {
		@Override
		public String toString() {
			return Lacrimis.MODID + ":crucible";
		}
	};

	public static final RecipeSerializer<ShapedInfusionRecipe> SHAPED_INFUSION_SERIALIZER = new ShapedInfusionRecipe.Serializer();
	public static final RecipeSerializer<ShapelessInfusionRecipe> SHAPELESS_INFUSION_SERIALIZER = new ShapelessInfusionRecipe.Serializer();
	public static final RecipeSerializer<CrucibleRecipe> CRUCIBLE_SERIALIZER = new CrucibleRecipe.Serializer();

	@SerializedName("crafting_texture")
	public static final Identifier craftingTexture = new Identifier("lacrimis", "textures/gui/crafting.png");

	public static void register() {
		ContainerProviderRegistry.INSTANCE.registerFactory(INFUSION_SCREEN_ID, new ContainerFactory<ScreenHandler>() {
			@Override
			public ScreenHandler create(int syncId, Identifier identifier, PlayerEntity player, PacketByteBuf buf) {
				BlockPos pos = buf.readBlockPos();
				InfusionTableEntity entity = (InfusionTableEntity) player.getEntityWorld().getBlockEntity(pos);
				return new InfusionScreenHandler(syncId, player, entity);
			}
		});

		Registry.register(Registry.RECIPE_TYPE, Lacrimis.MODID + ":infusion", INFUSION_RECIPE);
		Registry.register(Registry.RECIPE_TYPE, Lacrimis.MODID + ":crucible", CRUCIBLE_RECIPE);

		Registry.register(Registry.RECIPE_SERIALIZER, Lacrimis.MODID + ":infusion_shaped", SHAPED_INFUSION_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Lacrimis.MODID + ":infusion_shapeless", SHAPELESS_INFUSION_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Lacrimis.MODID + ":crucible", CRUCIBLE_SERIALIZER);
	}

	@Environment(EnvType.CLIENT)
	public static void registerClient() {
		//Infusion GUI
		ScreenProviderRegistry.INSTANCE.registerFactory(INFUSION_SCREEN_ID,
				(ContainerScreenFactory<InfusionScreenHandler>) container ->
						new InfusionScreen(container, MinecraftClient.getInstance().player.inventory,
								new TranslatableText(Lacrimis.MODID + ".gui.infusion")));

		//Patchiouli pages
		ClientBookRegistry.INSTANCE.pageTypes.put(Lacrimis.MODID + ":crucible", PageCrucible.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(Lacrimis.MODID + ":infusion", PageInfusion.class);
	}
}
