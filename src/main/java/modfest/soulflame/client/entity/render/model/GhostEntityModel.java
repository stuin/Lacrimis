package modfest.soulflame.client.entity.render.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.Collections;

import modfest.soulflame.entity.GhostEntity;

public class GhostEntityModel extends CompositeEntityModel<GhostEntity> {

    private final LinkedModelPart head;
    private final LinkedModelPart body;
    private final LinkedModelPart upperArmLeft;
    private final LinkedModelPart upperArmRight;
    private final LinkedModelPart lowerArmLeft;
    private final LinkedModelPart lowerArmRight;
    private final LinkedModelPart upperLegLeft;
    private final LinkedModelPart upperLegRight;
    private final LinkedModelPart lowerLegLeft;
    private final LinkedModelPart lowerLegRight;

    public GhostEntityModel() {
        this.head = createPart(this, 0, 0,
                0.0f, 28.0f, 0.0f,
                8.0f, 8.0f, 8.0f,
                0.0f, 24.0f, 0.0f);
        this.body = createPart(this, 0, 0,
                0.0f, 18.0f, 0.0f,
                8.0f, 12.0f, 4.0f,
                0.0f, 24.0f, 0.0f);
        this.upperArmLeft = createPart(this, this.body, 0, 0,
                6.0f, 21.0f, 0.0f,
                4.0f, 6.0f, 4.0f,
                4.0f, 22.0f, 0.0f);
        this.upperArmRight = createPart(this, this.body, 0, 0,
                -6.0f, 21.0f, 0.0f,
                4.0f, 6.0f, 4.0f,
                -4.0f, 22.0f, 0.0f);
        this.lowerArmLeft = createPart(this, this.upperArmLeft, 0, 0,
                6.0f, 15.0f, 0.0f,
                4.0f, 6.0f, 4.0f,
                6.0f, 18.0f, 0.0f);
        this.lowerArmRight = createPart(this, this.upperArmRight, 0, 0,
                -6.0f, 15.0f, 0.0f,
                4.0f, 6.0f, 4.0f,
                -6.0f, 18.0f, 0.0f);
        this.upperLegLeft = createPart(this, this.body, 0, 0,
                2.0f, 9.0f, 0.0f,
                4.0f, 6.0f, 4.0f,
                2.0f, 12.0f, 0.0f);
        this.upperLegRight = createPart(this, this.body, 0, 0,
                -2.0f, 9.0f, 0.0f,
                4.0f, 6.0f, 4.0f,
                -2.0f, 12.0f, 0.0f);
        this.lowerLegLeft = createPart(this, this.upperLegLeft, 0, 0,
                2.0f, 3.0f, 0.0f,
                4.0f, 6.0f, 4.0f,
                2.0f, 6.0f, 0.0f);
        this.lowerLegRight = createPart(this, this.upperLegRight, 0, 0,
                -2.0f, 3.0f, 0.0f,
                4.0f, 6.0f, 4.0f,
                -2.0f, 6.0f, 0.0f);
    }

    private static LinkedModelPart createPart(Model model, int u, int v, float x, float y, float z, float sx, float sy, float sz, float px, float py, float pz) {
        LinkedModelPart part = new LinkedModelPart(model, u, v);
        part.addCuboid(x - sx / 2 - px, y - sy / 2 - py, z - sz / 2 - pz, sx, sy, sz);
        part.setPivot(px, py, pz);
        return part;
    }

    private static LinkedModelPart createPart(Model model, LinkedModelPart parent, int u, int v, float x, float y, float z, float sx, float sy, float sz, float px, float py, float pz) {
        LinkedModelPart part = createPart(model, u, v,
                x - parent.getTotalPivotX(), y - parent.getTotalPivotY(), z - parent.getTotalPivotZ(),
                sx, sy, sz,
                px - parent.getTotalPivotX(), py - parent.getTotalPivotY(), pz - parent.getTotalPivotZ());
        parent.addChild(part);
        part.parent = parent;
        return part;
    }

    @Override
    public Iterable<ModelPart> getParts() {
        return Arrays.asList(this.head, this.body);
    }

    @Override
    public void setAngles(GhostEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        float yaw = limbDistance;

        Vec3d velocity = entity.getVelocity().multiply(2);
        Vec3d bodyRot = getRotationVector(0.0f, yaw);
        Vec3d bodyRotSide = getRotationVector(0.0f, yaw + 90.0f);

        float pitch = (float) velocity.dotProduct(bodyRot);
        float roll = (float) velocity.dotProduct(bodyRotSide);

        this.head.yaw = (float) (-headYaw * Math.PI / 180);
        System.out.println(this.head.yaw);
        this.head.pitch = (float) (headPitch * Math.PI / 180);
        this.body.yaw = (float) (-yaw * Math.PI / 180);
        this.body.pitch = pitch;
        this.body.roll = roll;
        this.upperArmLeft.yaw = 0.0f;
        this.upperArmLeft.pitch = pitch;
        this.upperArmLeft.roll = roll;
        this.upperArmRight.yaw = 0.0f;
        this.upperArmRight.pitch = pitch;
        this.upperArmRight.roll = roll;
        this.lowerArmLeft.yaw = 0.0f;
        this.lowerArmLeft.pitch = pitch;
        this.lowerArmLeft.roll = roll;
        this.lowerArmRight.yaw = 0.0f;
        this.lowerArmRight.pitch = pitch;
        this.lowerArmRight.roll = roll;
        this.upperLegLeft.yaw = 0.0f;
        this.upperLegLeft.pitch = pitch;
        this.upperLegLeft.roll = roll;
        this.upperLegRight.yaw = 0.0f;
        this.upperLegRight.pitch = pitch;
        this.upperLegRight.roll = roll;
        this.lowerLegLeft.yaw = 0.0f;
        this.lowerLegLeft.pitch = pitch;
        this.lowerLegLeft.roll = roll;
        this.lowerLegRight.yaw = 0.0f;
        this.lowerLegRight.pitch = pitch;
        this.lowerLegRight.roll = roll;
    }

    private static Vec3d getRotationVector(float pitch, float yaw) {
        float f = (float) (pitch * Math.PI / 180);
        float g = (float) (-yaw * Math.PI / 180);
        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        float j = MathHelper.cos(f);
        float k = MathHelper.sin(f);
        return new Vec3d(i * j, -k, h * j);
    }

}
