package modfest.lacrimis.client.render.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Arm;

import java.util.List;
import java.util.Random;

@Environment(EnvType.CLIENT)
public class SoulShellModel<T extends LivingEntity> extends BipedEntityModel<T> {
    private final List<ModelPart> parts;

    public SoulShellModel(ModelPart root) {
        super(root, RenderLayer::getEntityTranslucent);
        this.parts = root.traverse().filter((part) -> !part.isEmpty()).collect(ImmutableList.toImmutableList());
    }

    public void setArmAngle(Arm arm, MatrixStack matrices) {
        ModelPart modelPart = this.getArm(arm);
        float f = 0.5F * (float)(arm == Arm.RIGHT ? 1 : -1);
        modelPart.pivotX += f;
        modelPart.rotate(matrices);
        modelPart.pivotX -= f;
    }
}

