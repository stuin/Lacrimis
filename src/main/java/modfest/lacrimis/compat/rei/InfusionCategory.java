package modfest.lacrimis.compat.rei;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntList;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.REIHelper;
import me.shedaniel.rei.api.TransferRecipeCategory;
import me.shedaniel.rei.api.widgets.Slot;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import me.shedaniel.rei.server.ContainerInfo;
import me.shedaniel.rei.server.ContainerInfoHandler;
import modfest.lacrimis.init.ModBlocks;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class InfusionCategory implements TransferRecipeCategory<InfusionDisplay> {
    public InfusionCategory() {
    }

    public static int getSlotWithSize(InfusionDisplay recipeDisplay, int num, int craftingGridWidth) {
        int x = num % recipeDisplay.getWidth();
        int y = (num - x) / recipeDisplay.getWidth();
        return craftingGridWidth * y + x;
    }

    public Identifier getIdentifier() {
        return LacrimisPlugin.INFUSION;
    }

    public EntryStack getLogo() {
        return EntryStack.create(ModBlocks.infusionTable);
    }

    public String getCategoryName() {
        return I18n.translate("lacrimis.gui.infusion");
    }

    public List<Widget> setupDisplay(InfusionDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 58, bounds.getCenterY() - 27);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 60, startPoint.y + 18)));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 95, startPoint.y + 19)));
        List<List<EntryStack>> input = display.getInputEntries();
        List<Slot> slots = Lists.newArrayList();

        int i;
        for(i = 0; i < 3; ++i) {
            for(int x = 0; x < 3; ++x) {
                slots.add(Widgets.createSlot(new Point(startPoint.x + 1 + x * 18, startPoint.y + 1 + i * 18)).markInput());
            }
        }

        for(i = 0; i < input.size(); ++i) {
            if (display instanceof ShapedInfusionDisplay) {
                if (!(input.get(i)).isEmpty()) {
                    (slots.get(getSlotWithSize(display, i, 3))).entries(input.get(i));
                }
            } else if (!(input.get(i)).isEmpty()) {
                (slots.get(i)).entries(input.get(i));
            }
        }

        widgets.addAll(slots);
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 95, startPoint.y + 19)).entries(display.getOutputEntries()).disableBackground().markOutput());
        return widgets;
    }

    public void renderRedSlots(MatrixStack matrices, List<Widget> widgets, Rectangle bounds, InfusionDisplay display, IntList redSlots) {
        ContainerInfo<ScreenHandler> info = (ContainerInfo<ScreenHandler>) ContainerInfoHandler.getContainerInfo(this.getIdentifier(), REIHelper.getInstance().getPreviousContainerScreen().getScreenHandler().getClass());
        if (info != null) {
            matrices.push();
            matrices.translate(0.0D, 0.0D, 400.0D);
            Point startPoint = new Point(bounds.getCenterX() - 58, bounds.getCenterY() - 27);
            int width = info.getCraftingWidth(REIHelper.getInstance().getPreviousContainerScreen().getScreenHandler());

            for(Integer slot : redSlots) {
                int i = slot;
                int x = i % width;
                int y = MathHelper.floor((float) i / (float) width);
                DrawableHelper.fill(matrices, startPoint.x + 1 + x * 18, startPoint.y + 1 + y * 18, startPoint.x + 1 + x * 18 + 16, startPoint.y + 1 + y * 18 + 16, 1627324416);
            }

            matrices.pop();
        }
    }
}
