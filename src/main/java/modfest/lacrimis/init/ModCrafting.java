package modfest.lacrimis.init;

import modfest.lacrimis.crafting.*;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import com.google.gson.annotations.SerializedName;

import modfest.lacrimis.Lacrimis;

public class ModCrafting {
    public static final Identifier INFUSION_SCREEN_ID = new Identifier(Lacrimis.MODID, "infusion");
    public static final Identifier COMBINER_SCREEN_ID = new Identifier(Lacrimis.MODID, "combiner");

    public static ScreenHandlerType<InfusionScreenHandler> INFUSION_SCREEN_HANDLER;
    public static ScreenHandlerType<CombinerScreenHandler> COMBINER_SCREEN_HANDLER;

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

    public static void register() {
        INFUSION_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(INFUSION_SCREEN_ID, InfusionScreenHandler::new);
        COMBINER_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(COMBINER_SCREEN_ID, CombinerScreenHandler::new);

        Registry.register(Registry.RECIPE_TYPE, new Identifier(Lacrimis.MODID, "infusion"), INFUSION_RECIPE);
        Registry.register(Registry.RECIPE_TYPE, new Identifier(Lacrimis.MODID, "crucible"), CRUCIBLE_RECIPE);

        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(Lacrimis.MODID, "infusion_shaped"), SHAPED_INFUSION_SERIALIZER);
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(Lacrimis.MODID, "infusion_shapeless"), SHAPELESS_INFUSION_SERIALIZER);
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(Lacrimis.MODID, "crucible"), CRUCIBLE_SERIALIZER);
    }
}
