package modfest.soulflame.client.entity.render.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;

public class LinkedModelPart extends ModelPart {

    public LinkedModelPart parent;
    public boolean flipped;

    public LinkedModelPart(Model model) {
        super(model);
    }

    public LinkedModelPart(Model model, int textureOffsetU, int textureOffsetV) {
        super(model, textureOffsetU, textureOffsetV);
    }

    public LinkedModelPart(int textureWidth, int textureHeight, int textureOffsetU, int textureOffsetV) {
        super(textureWidth, textureHeight, textureOffsetU, textureOffsetV);
    }

    public float getTotalPivotX() {
        return this.parent == null ? this.pivotX : this.parent.getTotalPivotX() + this.pivotX;
    }

    public float getTotalPivotY() {
        return this.parent == null ? this.pivotY : -this.parent.getTotalPivotY() + this.pivotY;
    }

    public float getTotalPivotZ() {
        return this.parent == null ? this.pivotZ : -this.parent.getTotalPivotZ() + this.pivotZ;
    }

}