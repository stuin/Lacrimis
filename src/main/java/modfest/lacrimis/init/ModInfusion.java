package modfest.lacrimis.init;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.block.entity.InfusionTableEntity;
import modfest.lacrimis.infusion.CrucibleRecipe;
import modfest.lacrimis.infusion.InfusionScreen;
import modfest.lacrimis.infusion.InfusionScreenHandler;
import modfest.lacrimis.infusion.ShapedInfusionRecipe;
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

public class ModInfusion {
	public static final Identifier INFUSION_SCREEN_ID = new Identifier(Lacrimis.MODID, "infusion");

	public static final RecipeType<ShapedInfusionRecipe> INFUSION_RECIPE = new RecipeType<ShapedInfusionRecipe>() {
		@Override
		public String toString() {
			return Lacrimis.MODID + ":infusion_shaped";
		}
	};
	public static final RecipeType<CrucibleRecipe> CRUCIBLE_RECIPE = new RecipeType<CrucibleRecipe>() {
		@Override
		public String toString() {
			return Lacrimis.MODID + ":crucible";
		}
	};

	public static final RecipeSerializer<ShapedInfusionRecipe> SHAPED_INFUSION_SERIALIZER = new ShapedInfusionRecipe.Serializer();
	public static final RecipeSerializer<CrucibleRecipe> CRUCIBLE_SERIALIZER = new CrucibleRecipe.Serializer();

	public static void register() {
		ContainerProviderRegistry.INSTANCE.registerFactory(INFUSION_SCREEN_ID, new ContainerFactory<ScreenHandler>() {
			@Override
			public ScreenHandler create(int syncId, Identifier identifier, PlayerEntity player, PacketByteBuf buf) {
				BlockPos pos = buf.readBlockPos();
				InfusionTableEntity entity = (InfusionTableEntity) player.getEntityWorld().getBlockEntity(pos);
				return new InfusionScreenHandler(syncId, player, entity);
			}
		});

		Registry.register(Registry.RECIPE_TYPE, Lacrimis.MODID + ":infusion_shaped", INFUSION_RECIPE);
		Registry.register(Registry.RECIPE_TYPE, Lacrimis.MODID + ":crucible", CRUCIBLE_RECIPE);

		Registry.register(Registry.RECIPE_SERIALIZER, Lacrimis.MODID + ":infusion_shaped", SHAPED_INFUSION_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Lacrimis.MODID + ":crucible", CRUCIBLE_SERIALIZER);
	}

	@Environment(EnvType.CLIENT)
	public static void registerClient() {
		ScreenProviderRegistry.INSTANCE.registerFactory(INFUSION_SCREEN_ID,
				(ContainerScreenFactory<InfusionScreenHandler>) container ->
						new InfusionScreen(container, MinecraftClient.getInstance().player.inventory,
								new TranslatableText(Lacrimis.MODID + ".gui.infusion")));
	}
}
