/*
 * This file is licensed under the MIT License, part of Roughly Enough Items.
 * Copyright (c) 2018, 2019, 2020, 2021 shedaniel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package modfest.lacrimis.compat.rei.client;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntList;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.TransferDisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import modfest.lacrimis.compat.rei.CrucibleDisplay;
import modfest.lacrimis.compat.rei.LacrimisPlugin;
import modfest.lacrimis.init.ModBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.List;

@Environment(EnvType.CLIENT)
public class CrucibleCategory implements TransferDisplayCategory<CrucibleDisplay> {

    @Override
    public CategoryIdentifier<? extends CrucibleDisplay> getCategoryIdentifier() {
        return LacrimisPlugin.CRUCIBLE;
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModBlocks.crucible);
    }

    @Override
    public Text getTitle() {
        return new TranslatableText("lacrimis.gui.crucible");
    }
    
    @Override
    public List<Widget> setupDisplay(CrucibleDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.y + 10);
        int tears = display.getTears();
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 9)));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 24, startPoint.y + 8)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 9))
                .entries(display.getOutputEntries().get(0))
                .disableBackground()
                .markOutput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 1, startPoint.y + 1))
                .entries(display.getInputEntries().get(0))
                .markInput());
        widgets.add(Widgets.createLabel(new Point(startPoint.x + 40, startPoint.y + 20),
                new TranslatableText("lacrimis.tooltip.crucible", tears)).noShadow().rightAligned().color(0xFF404040, 0xFFBBBBBB));
        return widgets;
    }
    
    @Override
    public int getDisplayHeight() {
        return 49;
    }

    @Override
    public void renderRedSlots(MatrixStack matrices, List<Widget> widgets, Rectangle bounds, CrucibleDisplay display, IntList redSlots) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.y + 10);
        matrices.push();
        matrices.translate(0, 0, 400);
        if (redSlots.contains(0)) {
            DrawableHelper.fill(matrices, startPoint.x + 1, startPoint.y + 1, startPoint.x + 1 + 16, startPoint.y + 1 + 16, 1090453504);
        }
        matrices.pop();
    }
}
