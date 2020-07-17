package modfest.lacrimis.compat.rei;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntList;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.TransferRecipeCategory;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import modfest.lacrimis.init.ModBlocks;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.util.List;

public class CrucibleCategory implements TransferRecipeCategory<CrucibleDisplay> {
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

    public int getDisplayHeight() {
        return 40;
    }

    public Identifier getIdentifier() {
        return LacrimisPlugin.CRUCIBLE;
    }

    public EntryStack getLogo() {
        return EntryStack.create(ModBlocks.crucible);
    }

    public String getCategoryName() {
        return I18n.translate("lacrimis.gui.crucible");
    }
}
