package modfest.lacrimis.mixin;

import modfest.lacrimis.init.ModCrafting;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.book.RecipeBook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientRecipeBook.class)
public abstract class ClientRecipeBookMixin extends RecipeBook {

    @Inject(at = @At("HEAD"), cancellable = true, method = "getGroupForRecipe(Lnet/minecraft/recipe/Recipe;)Lnet/minecraft/client/recipebook/RecipeBookGroup;")
    private static void getGroupForRecipe(Recipe<?> recipe, CallbackInfoReturnable<RecipeBookGroup> cir) {
        if(recipe.getType() == ModCrafting.INFUSION_RECIPE || recipe.getType() == ModCrafting.CRUCIBLE_RECIPE)
            cir.setReturnValue(RecipeBookGroup.UNKNOWN);
    }
}
