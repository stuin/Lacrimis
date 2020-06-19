package modfest.lacrimis.init;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.fabricmc.fabric.api.container.ContainerFactory;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;

import com.google.gson.annotations.SerializedName;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.block.entity.CombinerEntity;
import modfest.lacrimis.block.entity.InfusionTableEntity;
import modfest.lacrimis.crafting.CombinerScreenHandler;
import modfest.lacrimis.crafting.CrucibleRecipe;
import modfest.lacrimis.crafting.InfusionRecipe;
import modfest.lacrimis.crafting.InfusionScreenHandler;
import modfest.lacrimis.crafting.ShapedInfusionRecipe;
import modfest.lacrimis.crafting.ShapelessInfusionRecipe;

public class ModCrafting {
    public static final Identifier INFUSION_SCREEN_ID = new Identifier(Lacrimis.MODID, "infusion");
    public static final Identifier COMBINER_SCREEN_ID = new Identifier(Lacrimis.MODID, "combiner");

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
        ContainerProviderRegistry.INSTANCE.registerFactory(COMBINER_SCREEN_ID, new ContainerFactory<ScreenHandler>() {
            @Override
            public ScreenHandler create(int syncId, Identifier identifier, PlayerEntity player, PacketByteBuf buf) {
                BlockPos pos = buf.readBlockPos();
                CombinerEntity entity = (CombinerEntity) player.getEntityWorld().getBlockEntity(pos);
                return new CombinerScreenHandler(syncId, player, entity);
            }
        });

        Registry.register(Registry.RECIPE_TYPE, Lacrimis.MODID + ":infusion", INFUSION_RECIPE);
        Registry.register(Registry.RECIPE_TYPE, Lacrimis.MODID + ":crucible", CRUCIBLE_RECIPE);

        Registry.register(Registry.RECIPE_SERIALIZER, Lacrimis.MODID + ":infusion_shaped", SHAPED_INFUSION_SERIALIZER);
        Registry.register(Registry.RECIPE_SERIALIZER, Lacrimis.MODID + ":infusion_shapeless", SHAPELESS_INFUSION_SERIALIZER);
        Registry.register(Registry.RECIPE_SERIALIZER, Lacrimis.MODID + ":crucible", CRUCIBLE_SERIALIZER);
    }

}
