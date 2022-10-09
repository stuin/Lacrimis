package com.stuintech.lacrimis.crafting;

import com.stuintech.lacrimis.Lacrimis;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModCrafting {
    public static final Identifier INFUSION_ID = new Identifier(Lacrimis.MODID, "infusion");
    public static final Identifier CRUCIBLE_ID = new Identifier(Lacrimis.MODID, "crucible");
    public static final Identifier COMBINER_ID = new Identifier(Lacrimis.MODID, "combiner");

    public static ScreenHandlerType<InfusionScreenHandler> INFUSION_SCREEN_HANDLER;
    public static ScreenHandlerType<CombinerScreenHandler> COMBINER_SCREEN_HANDLER;

    public static final RecipeType<InfusionRecipe> INFUSION_RECIPE = new RecipeType<>() {
        @Override
        public String toString() {
            return INFUSION_ID.toString();
        }
    };
    public static final RecipeType<CrucibleRecipe> CRUCIBLE_RECIPE = new RecipeType<>() {
        @Override
        public String toString() {
            return CRUCIBLE_ID.toString();
        }
    };
    public static final RecipeType<CombinerRecipe> COMBINER_RECIPE = new RecipeType<>() {
        @Override
        public String toString() {
            return COMBINER_ID.toString();
        }
    };

    public static final RecipeSerializer<ShapedInfusionRecipe> SHAPED_INFUSION_SERIALIZER = new ShapedInfusionRecipe.Serializer();
    public static final RecipeSerializer<ShapelessInfusionRecipe> SHAPELESS_INFUSION_SERIALIZER = new ShapelessInfusionRecipe.Serializer();
    public static final RecipeSerializer<CrucibleRecipe> CRUCIBLE_SERIALIZER = new CrucibleRecipe.Serializer();
    public static final RecipeSerializer<CombinerRecipe> COMBINER_SERIALIZER = new CombinerRecipe.Serializer();
    public static final SpecialRecipeSerializer<SpawnerCombinerRecipe> SPAWNER_COMBINER_SERIALIZER = new SpecialRecipeSerializer<>(SpawnerCombinerRecipe::new);

    public static void register() {
        INFUSION_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(INFUSION_ID, InfusionScreenHandler::new);
        COMBINER_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(COMBINER_ID, CombinerScreenHandler::new);

        Registry.register(Registry.RECIPE_TYPE, INFUSION_ID, INFUSION_RECIPE);
        Registry.register(Registry.RECIPE_TYPE, CRUCIBLE_ID, CRUCIBLE_RECIPE);
        Registry.register(Registry.RECIPE_TYPE, COMBINER_ID, COMBINER_RECIPE);

        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(Lacrimis.MODID, "infusion_shaped"), SHAPED_INFUSION_SERIALIZER);
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(Lacrimis.MODID, "infusion_shapeless"), SHAPELESS_INFUSION_SERIALIZER);
        Registry.register(Registry.RECIPE_SERIALIZER, CRUCIBLE_ID, CRUCIBLE_SERIALIZER);
        Registry.register(Registry.RECIPE_SERIALIZER, COMBINER_ID, COMBINER_SERIALIZER);
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(Lacrimis.MODID, "combiner_spawner"), SPAWNER_COMBINER_SERIALIZER);
    }
}
