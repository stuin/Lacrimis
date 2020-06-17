package modfest.lacrimis.mixin;

import com.google.common.collect.Lists;
import modfest.lacrimis.infusion.InfusionScreenHandler;
import modfest.lacrimis.init.ModInfusion;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.book.RecipeBook;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ClientRecipeBook.class)
public abstract class ClientRecipeBookMixin extends RecipeBook {

    @Inject(at = @At("HEAD"), cancellable = true, method = "getGroupForRecipe(Lnet/minecraft/recipe/Recipe;)Lnet/minecraft/client/recipebook/RecipeBookGroup;")
    private static void getGroupForRecipe(Recipe<?> recipe, CallbackInfoReturnable<RecipeBookGroup> cir) {
        if(recipe.getType() == ModInfusion.INFUSION_RECIPE || recipe.getType() == ModInfusion.CRUCIBLE_RECIPE)
            cir.setReturnValue(RecipeBookGroup.UNKNOWN);
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "getGroups(Lnet/minecraft/screen/AbstractRecipeScreenHandler;)Ljava/util/List;")
    private static void getGroups(AbstractRecipeScreenHandler<?> handler, CallbackInfoReturnable<List<RecipeBookGroup>> cir) {
        if(handler instanceof InfusionScreenHandler)
            cir.setReturnValue(Lists.newArrayList(RecipeBookGroup.UNKNOWN));
    }
}
