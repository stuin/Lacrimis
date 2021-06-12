package modfest.lacrimis.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Arm;

@Environment(EnvType.CLIENT)
public class SoulShellModel<T extends LivingEntity> extends BipedEntityModel<T> {
    public SoulShellModel(float scale) {
        super(RenderLayer::getEntityTranslucent, scale, 0.0F, 64, 64);
        this.leftArm = new ModelPart(this, 32, 48);
        this.leftArm.addCuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale);
        this.leftArm.setPivot(5.0F, 2.0F, 0.0F);
        this.leftLeg = new ModelPart(this, 16, 48);
        this.leftLeg.addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale);
        this.leftLeg.setPivot(1.9F, 12.0F, 0.0F);
    }

    public void setArmAngle(Arm arm, MatrixStack matrices) {
        ModelPart modelPart = this.getArm(arm);
        modelPart.rotate(matrices);
    }
}

