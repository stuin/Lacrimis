package modfest.lacrimis.compat.rei;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntList;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.TransferDisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import modfest.lacrimis.crafting.CrucibleRecipe;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.List;

public class CrucibleCategory implements TransferDisplayCategory<CrucibleDisplay> {
    public CrucibleCategory() {
    }

    public List<Widget> setupDisplay(CrucibleDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 13);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 24, startPoint.y + 4)));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 5)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 1, startPoint.y + 5)).entries(display.getInputEntries().get(0)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 5)).entries(display.getOutputEntries()).disableBackground().markOutput());

        widgets.add(Widgets.createLabel(new Point(startPoint.x + 32, startPoint.y + 21), new LiteralText("" + display.getTears())));
        return widgets;
    }

    @Override
    public void renderRedSlots(MatrixStack matrices, List<Widget> list, Rectangle bounds, CrucibleDisplay crucibleDisplay, IntList redSlots) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 27);
        matrices.push();
        matrices.translate(0.0D, 0.0D, 400.0D);
        if (redSlots.contains(0)) {
            DrawableHelper.fill(matrices, startPoint.x + 1, startPoint.y + 1, startPoint.x + 1 + 16, startPoint.y + 1 + 16, 1090453504);
        }

        matrices.pop();
    }

    @Override
    public Renderer getIcon() {
        return null;
    }

    @Override
    public Text getTitle() {
        return new TranslatableText("lacrimis.gui.crucible");
    }

    public int getDisplayHeight() {
        return 40;
    }

    @Override
    public CategoryIdentifier<? extends CrucibleRecipe> getCategoryIdentifier() {
        return null;
    }

    public Identifier getIdentifier() {
        return LacrimisPlugin.CRUCIBLE;
    }

    @Override
    public void renderRedSlots(MatrixStack matrices, List<Widget> widgets, me.shedaniel.math.Rectangle bounds, CrucibleRecipe display, IntList redSlots) {

    }
}
