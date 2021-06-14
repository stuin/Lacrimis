package modfest.lacrimis.client.render.entity;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.entity.SoulShellEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class SoulShellRenderer extends LivingEntityRenderer<SoulShellEntity, SoulShellModel<SoulShellEntity>> {
    public static final Identifier TEXTURE = new Identifier(Lacrimis.MODID, "textures/entity/soul_shell.png");

    public SoulShellRenderer(EntityRendererFactory.Context context) {
        super(context, new SoulShellModel<>(0.0F), 0.5F);
        this.addFeature(new ArmorFeatureRenderer<>(this,
                new BipedEntityModel<>(context.getPart(EntityModelLayers.PLAYER_SLIM_INNER_ARMOR)),
                new BipedEntityModel<>(context.getPart(EntityModelLayers.PLAYER_SLIM_OUTER_ARMOR))));
        this.addFeature(new HeldItemFeatureRenderer<>(this));
        this.addFeature(new HeadFeatureRenderer<>(this, context.getModelLoader()));
        this.addFeature(new ElytraFeatureRenderer<>(this, context.getModelLoader()));
    }

    public void render(SoulShellEntity abstractClientPlayerEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.setModelPose(abstractClientPlayerEntity);
        super.render(abstractClientPlayerEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Vec3d getPositionOffset(SoulShellEntity abstractClientPlayerEntity, float f) {
        return abstractClientPlayerEntity.isInSneakingPose() ? new Vec3d(0.0D, -0.125D, 0.0D) : super.getPositionOffset(abstractClientPlayerEntity, f);
    }

    private void setModelPose(SoulShellEntity abstractClientPlayerEntity) {
        SoulShellModel<SoulShellEntity> playerEntityModel = this.getModel();
        playerEntityModel.setVisible(true);
        BipedEntityModel.ArmPose armPose = getArmPose(abstractClientPlayerEntity, Hand.MAIN_HAND);
        BipedEntityModel.ArmPose armPose2 = getArmPose(abstractClientPlayerEntity, Hand.OFF_HAND);
        if (armPose.isTwoHanded()) {
            armPose2 = abstractClientPlayerEntity.getOffHandStack().isEmpty() ? BipedEntityModel.ArmPose.EMPTY : BipedEntityModel.ArmPose.ITEM;
        }

        if (abstractClientPlayerEntity.getMainArm() == Arm.RIGHT) {
            playerEntityModel.rightArmPose = armPose;
            playerEntityModel.leftArmPose = armPose2;
        } else {
            playerEntityModel.rightArmPose = armPose2;
            playerEntityModel.leftArmPose = armPose;
        }

    }

    private static BipedEntityModel.ArmPose getArmPose(SoulShellEntity abstractClientPlayerEntity, Hand hand) {
        ItemStack itemStack = abstractClientPlayerEntity.getStackInHand(hand);
        if (itemStack.isEmpty()) {
            return BipedEntityModel.ArmPose.EMPTY;
        } else {
            return BipedEntityModel.ArmPose.ITEM;
        }
    }

    public Identifier getTexture(SoulShellEntity abstractClientPlayerEntity) {
        return TEXTURE;
    }

    protected void scale(SoulShellEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f) {
        matrixStack.scale(0.9375F, 0.9375F, 0.9375F);
    }

    protected void setupTransforms(SoulShellEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f, float g, float h) {
        float n;
        float k;
        if (abstractClientPlayerEntity.isFallFlying()) {
            super.setupTransforms(abstractClientPlayerEntity, matrixStack, f, g, h);
            n = (float)abstractClientPlayerEntity.getRoll() + h;
            k = MathHelper.clamp(n * n / 100.0F, 0.0F, 1.0F);
            matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(k * (-90.0F - abstractClientPlayerEntity.getPitch())));

            Vec3d vec3d = abstractClientPlayerEntity.getRotationVec(h);
            Vec3d vec3d2 = abstractClientPlayerEntity.getVelocity();
            double d = vec3d2.horizontalLengthSquared();
            double e = vec3d.horizontalLengthSquared();
            if (d > 0.0D && e > 0.0D) {
                double l = (vec3d2.x * vec3d.x + vec3d2.z * vec3d.z) / Math.sqrt(d * e);
                double m = vec3d2.x * vec3d.z - vec3d2.z * vec3d.x;
                matrixStack.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion((float)(Math.signum(m) * Math.acos(l))));
            }
        } else {
            super.setupTransforms(abstractClientPlayerEntity, matrixStack, f, g, h);
        }

    }
}

